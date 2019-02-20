/**
 * 
 */
package com.cc.common.utils;

import java.util.UUID;

/**
 * @author Administrator
 *
 */
public class UUIDUtils {

	/**
	 * 生成uuid
	 * @return
	 */
	public static String getUuid(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
}
