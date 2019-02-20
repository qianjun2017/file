/**
 * 
 */
package com.cc.file.strategy;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Administrator
 *
 */
public abstract class FileStrategy {

	/**
	 * 上传文件
	 * @param inputStream
	 * @param path 绝对目录
	 * @param subPath 子目录
	 * @param fileName
	 * @return
	 */
	public abstract String uploadFile(InputStream inputStream, String path, String subPath, String fileName);
	
	/**
	 * 下载文件
	 * @param outputStream
	 * @param path
	 * @param subPath 子目录
	 * @param fileName
	 */
	public abstract void downloadFile(OutputStream outputStream, String path, String subPath, String fileName);
	
}
