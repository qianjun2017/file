/**
 * 
 */
package com.cc.file.strategy.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.stereotype.Component;

import com.cc.common.exception.LogicException;
import com.cc.file.strategy.FileStrategy;

/**
 * @author Administrator
 *
 */
@Component
public class NativeFileStrategy extends FileStrategy {

	@Override
	public String uploadFile(InputStream inputStream, String path, String subPath, String fileName) {
		File dir = new File(path+subPath);
		if (!dir.exists() && !dir.mkdirs()) {
			throw new LogicException("E001", "创建文件目录失败");
		}
		try {
			OutputStream outputStream = new FileOutputStream(new File(path+subPath, fileName));
			byte buffer[] = new byte[1024];
			int len = 0;
			while((len = inputStream.read(buffer)) > 0){
				outputStream.write(buffer, 0, len);
			}
			outputStream.close();
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "/files"+subPath+"/"+fileName;
	}

	@Override
	public void downloadFile(OutputStream outputStream, String path, String subPath, String fileName) {
		File file = new File(path+subPath, fileName);
		if (!file.exists()) {
			throw new LogicException("E001", "文件不存在或已被删除");
		}
		try {
			InputStream inputStream = new FileInputStream(file);
			byte buffer[] = new byte[1024];
			int len = 0;
			while((len = inputStream.read(buffer)) > 0){
				outputStream.write(buffer, 0, len);
			}
			outputStream.close();
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
