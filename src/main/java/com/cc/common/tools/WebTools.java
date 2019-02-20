/**
 * 
 */
package com.cc.common.tools;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 *
 */
public class WebTools {

	/**
	 * 获取请求数据
	 * @param request
	 * @return
	 */
	public static String readBody(HttpServletRequest request){
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = request.getReader();
			String line = null;
			while((line = reader.readLine())!=null){
				buffer.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return buffer.substring(0);
	}
}
