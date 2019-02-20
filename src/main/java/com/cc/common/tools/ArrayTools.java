/**
 * 
 */
package com.cc.common.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 数组处理工具类
 * @author Administrator
 *
 */
public class ArrayTools {

	/**
	 * 数组是否为空
	 * @param array
	 * @return
	 */
	public static <T> boolean isEmptyOrNull(T[] array) {
		return (array==null || array.length == 0);
	}
	
	/**
	 * 数组转换成List
	 * @param array
	 * @return
	 */
	public static <T> List<T> toList(T[] array){
		List<T> list = new ArrayList<T>();
		if (isEmptyOrNull(array)) {
			return list;
		}
    	for (T t : array) {
			list.add(t);
		}
    	return list;
	}
}
