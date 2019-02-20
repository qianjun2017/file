/**
 * 
 */
package com.cc.common.tools;

import java.util.List;

/**
 * List处理工具类
 * @author Administrator
 *
 */
public class ListTools {

	/**
	 * list是否为空
	 * @param list
	 * @return
	 */
	public static <T> boolean isEmptyOrNull(List<T> list){
		return (list == null || list.size() == 0);
	}
	
	/**
	 * list转string
	 * @param list
	 * @return
	 */
	public static <T> String toString(List<T> list) {
		StringBuffer buffer = new StringBuffer("[");
		for (T t : list) {
			buffer.append(StringTools.toString(t)+",");
		}
		if (!isEmptyOrNull(list)) {
			buffer.deleteCharAt(buffer.lastIndexOf(","));
		}
		buffer.append("]");
		return buffer.substring(0);
	}
}
