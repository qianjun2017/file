/**
 * 
 */
package com.cc.file.service.impl;

import java.io.IOException;
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
			List<String> urls = new ArrayList<String>();
			List<MultipartFile> fileList = ((MultipartHttpServletRequest)request).getFiles("file");
			for (MultipartFile file : fileList) {
				String fileName = file.getOriginalFilename();
				int lastIndex = fileName.lastIndexOf("\\");
				if(lastIndex != -1) {
					fileName = fileName.substring(lastIndex + 1);
				}
				checkFileExt(fileName);
				fileName = makeFileName(fileName);
				urls.add(strategy.uploadFile(file.getInputStream(), fileConfig.getPath(), getSubPath(request, fileName), fileName));
			}
			request.setAttribute("urls", urls);
		} catch (IOException e) {
			e.printStackTrace();
			throw new LogicException("E004", "文件上传异常");
		}
	}

	@Override
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
		String fileName = request.getParameter("fileName");
		try {
			strategy.downloadFile(response.getOutputStream(), fileConfig.getPath(), getSubPath(request, fileName), fileName);
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
	 * @param fileName
	 * @return
	 */
	public String getSubPath(HttpServletRequest request, String fileName){
		StringBuffer buffer = new StringBuffer();
		String type = request.getParameter("type");
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
