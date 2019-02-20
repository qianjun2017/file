/**
 * 
 */
package com.cc.file.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cc.common.exception.LogicException;
import com.cc.common.tools.ListTools;
import com.cc.common.web.Response;
import com.cc.file.service.FileService;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/file")
public class FileController {

	@Autowired
	private FileService fileService;
	
	/**
	 * 上传文件
	 * @param httpRequest
	 * @param hhtpResponse
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/up", method = RequestMethod.POST)
	public Response<String[]> uploadImages(HttpServletRequest httpRequest, HttpServletResponse hhtpResponse){
		Response<String[]> response = new Response<String[]>();
		try {
			fileService.uploadFile(httpRequest, hhtpResponse);
			Object object = httpRequest.getAttribute("urls");
			if (object==null || !(object instanceof List<?>)) {
				response.setMessage("上传图片失败");
				return response;
			}
			List<String> urlList = (List<String>) object;
			if (ListTools.isEmptyOrNull(urlList)) {
				response.setMessage("上传图片失败");
				return response;
			}
			String[] urls = new String[urlList.size()];
			for (String url : urlList) {
				urls[urlList.indexOf(url)] = url;
			}
			response.setData(urls);
			response.setSuccess(Boolean.TRUE);
		} catch (LogicException e) {
			response.setMessage(e.getErrContent());
		} catch (Exception e) {
			response.setMessage("上传图片异常");
			e.printStackTrace();
		}
		return response;
	}
}
