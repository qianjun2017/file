/**
 * 
 */
package com.cc.common.tools;

import java.util.Set;

/**
 * Set处理工具类
 * @author Administrator
 *
 */
public class SetTools {

	/**
	 * set是否为空
	 * @param set
	 * @return
	 */
	public static <T> boolean isEmptyOrNull(Set<T> set){
		return (set == null || set.size() == 0);
	}
	
}
