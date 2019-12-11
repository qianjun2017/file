/**
 * 
 */
package com.cc.file.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cc.common.exception.LogicException;
import com.cc.file.config.FileConfig;
import com.cc.file.service.FileService;
import com.cc.file.strategy.FileStrategy;
import com.cc.system.config.bean.SystemConfigBean;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;

import com.cc.common.tools.ListTools;
import com.cc.common.tools.StringTools;
import com.cc.common.utils.UUIDUtils;

/**
 * @author Administrator
 *
 */
@Service
public class FileServiceImpl implements FileService {
	
	/**
	 * 文件配置
	 */
	@Autowired
	private FileConfig fileConfig;
	
	/**
	 * 文件存储策略
	 */
	@Autowired
	private FileStrategy strategy;
	
	@Override
	public void uploadFile(HttpServletRequest request, HttpServletResponse response) {
		try {
			String appCode = request.getHeader("appcode");
			List<SystemConfigBean> fileAppCodeSystemConfigBeanList = SystemConfigBean.findAllByParams(SystemConfigBean.class, "propertyName", "file.appCode");
			if(ListTools.isEmptyOrNull(fileAppCodeSystemConfigBeanList) || "true".equals(fileAppCodeSystemConfigBeanList.get(0).getPropertyValue())){
				if(StringTools.isNullOrNone(appCode)){
					throw new LogicException("E001", "未知应用，禁止上传");
				}
				List<SystemConfigBean> appCodeSystemConfigBeanList = SystemConfigBean.findAllByParams(SystemConfigBean.class, "propertyName", appCode);
		    	if(ListTools.isEmptyOrNull(appCodeSystemConfigBeanList)){
		    		throw new LogicException("E002", "未知应用，禁止上传");
		    	}
			}
			List<String> urls = new ArrayList<String>();
			String type = request.getParameter("type");
			String size = null;
			Boolean keep = Boolean.FALSE;
			if("image".equals(type)){
				size = request.getParameter("size");
				String k = request.getParameter("keep");
				if(!StringTools.isNullOrNone(k) && "true".equals(k)){
					keep = Boolean.TRUE;
				}
			}
			List<MultipartFile> fileList = ((MultipartHttpServletRequest)request).getFiles("file");
			for (MultipartFile file : fileList) {
				String fileName = file.getOriginalFilename();
				int lastIndex = fileName.lastIndexOf("\\");
				if(lastIndex != -1) {
					fileName = fileName.substring(lastIndex + 1);
				}
				checkFileExt(fileName);
				fileName = makeFileName(fileName);
				InputStream inputStream = file.getInputStream();
				if("image".equals(type) && !StringTools.isNullOrNone(size)){
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					Builder<? extends InputStream> builder = Thumbnails.of(inputStream).outputQuality(1.f);
					String[] wh = size.split("x");
					int width = Integer.parseInt(wh[0]);
					int height = width;
					if(wh.length>1){
						height = Integer.parseInt(wh[1]);
					}
					builder.size(width, height).keepAspectRatio(keep).toOutputStream(out);
					inputStream = new ByteArrayInputStream(out.toByteArray());
				}
				urls.add(strategy.uploadFile(inputStream, fileConfig.getPath(), getSubPath(request, appCode, fileName), fileName));
			}
			request.setAttribute("urls", urls);
		} catch (IOException e) {
			e.printStackTrace();
			throw new LogicException("E004", "文件上传异常");
		}
	}

	@Override
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
		String appCode = request.getHeader("appCode");
		String fileName = request.getParameter("fileName");
		try {
			strategy.downloadFile(response.getOutputStream(), fileConfig.getPath(), getSubPath(request, appCode, fileName), fileName);
			fileName = getRealFileName(fileName);
			response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
		} catch (IOException e) {
			e.printStackTrace();
			throw new LogicException("E001", "文件下载异常");
		}
	}
	
	/**
	 * 检查文件扩展名
	 * @param fileName
	 */
	public void checkFileExt(String fileName){
		if (StringTools.isAllNullOrNone(new String[]{fileConfig.getAllowedExt(),fileConfig.getDeniedExt()})) {
			return;
		}
		int lastIndexOf = fileName.lastIndexOf(".");
		String ext = null;
		if (lastIndexOf!=-1) {
			ext = fileName.substring(lastIndexOf+1);
		}
		if (StringTools.isNullOrNone(ext)) {
			throw new LogicException("E001", "无法判断文件类型");
		}
		if (!StringTools.isNullOrNone(fileConfig.getAllowedExt())) {
			String[] allowedExts = fileConfig.getAllowedExt().split(",");
			boolean allowed = false;
			for (String allowedExt : allowedExts) {
				if (ext.equalsIgnoreCase(allowedExt)) {
					allowed = true;
				}
			}
			if(!allowed){
				throw new LogicException("E002", "文件类型不正确");
			}
		}
		if (!StringTools.isNullOrNone(fileConfig.getDeniedExt())) {
			String[] deniedExts = fileConfig.getDeniedExt().split(",");
			boolean denied = false;
			for (String deniedExt : deniedExts) {
				if (ext.equalsIgnoreCase(deniedExt)) {
					denied = true;
				}
			}
			if(denied){
				throw new LogicException("E002", "文件类型不正确");
			}
		}
	}
	
	/**
	 * 获取保存文件的文件夹
	 * @param request
	 * @param appCode
	 * @param fileName
	 * @return
	 */
	public String getSubPath(HttpServletRequest request, String appCode, String fileName){
		StringBuffer buffer = new StringBuffer();
		String type = request.getParameter("type");
		if(!StringTools.isNullOrNone(appCode)){
			buffer.append("/").append(appCode);
		}
		if(!StringTools.isNullOrNone(type)){
			buffer.append("/").append(type);
		}
		if (fileConfig.isDisperse()) {
			buffer.append(disperseFile(fileName));
		}
		return buffer.substring(0);
	}
	
	/**
	 * 打散文件存放目录，防止单个文件夹存放过多文件
	 * @param fileName
	 * @return
	 */
	public String disperseFile(String fileName){
		int hashCode = fileName.hashCode();
		String dir1 = Integer.toHexString(hashCode & 0xF);
		String dir2 = Integer.toHexString(hashCode >>> 4 & 0xF);
		return "/"+dir1+"/"+dir2;
	}
	
	/**
	 * 重命名文件，防止文件重名
	 * @param fileName
	 * @return
	 */
	public String makeFileName(String fileName){
		return UUIDUtils.getUuid()+"_"+fileName;
	}
	
	/**
	 * 获取文件的真实名称
	 * @param fileName
	 * @return
	 */
	public String getRealFileName(String fileName){
		if (fileName.contains("_")) {
			return fileName.substring(fileName.indexOf("_")+1);
		}
		return fileName;
	}
}
