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
	public Response<Object> uploadImages(HttpServletRequest httpRequest, HttpServletResponse hhtpResponse){
		Response<Object> response = new Response<Object>();
		try {
			fileService.uploadFile(httpRequest, hhtpResponse);
			Object fileMapList = httpRequest.getAttribute("fileMapList");
			if (fileMapList==null || !(fileMapList instanceof List<?>)) {
				response.setMessage("上传文件失败");
				return response;
			}
			response.setData(fileMapList);
			response.setSuccess(Boolean.TRUE);
		} catch (LogicException e) {
			response.setMessage(e.getErrContent());
		} catch (Exception e) {
			response.setMessage("上传文件异常");
			e.printStackTrace();
		}
		return response;
	}
}
