/**
 * 
 */
package com.cc.file.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Administrator
 *
 */
public interface FileService {

	/**
	 * 上传文件
	 * @param request
	 * @param response
	 */
	void uploadFile(HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 下载文件
	 * @param request
	 * @param response
	 */
	void downloadFile(HttpServletRequest request, HttpServletResponse response);
}
