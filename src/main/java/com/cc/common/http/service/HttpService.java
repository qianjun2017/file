/**
 * 
 */
package com.cc.common.http.service;

import java.util.Map;

/**
 * @author Administrator
 *
 */
public interface HttpService {

	/**
	 * get请求
	 * @param url 资源地址
	 * @param paramMap 请求参数
	 * @param encoding 编码
	 * @return
	 */
	String get(String url, Map<String, Object> paramMap, String encoding);
	
	/**
	 * post请求
	 * @param url 资源地址
	 * @param paramMap 请求参数
	 * @param encoding 编码
	 * @return
	 */
	String post(String url, Map<String, Object> paramMap, String encoding);
}
