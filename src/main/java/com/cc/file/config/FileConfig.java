/**
 * 
 */
package com.cc.file.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 *
 */
@Component
@ConfigurationProperties(prefix="file")
public class FileConfig {

	/**
	 * 文件保存路径
	 */
	private String path;
	
	/**
	 * 静态文件访问路径
	 */
	private String location;
	
	/**
	 * 允许的文件扩展名
	 */
	private String allowedExt;
	
	/**
	 * 禁止的文件扩展名
	 */
	private String deniedExt;
	
	/**
	 * 分散文件存放
	 */
	private boolean disperse;
	
	/**
	 * 是否包含appCode
	 */
	private boolean appCode;

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return the allowedExt
	 */
	public String getAllowedExt() {
		return allowedExt;
	}

	/**
	 * @return the deniedExt
	 */
	public String getDeniedExt() {
		return deniedExt;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @param allowedExt the allowedExt to set
	 */
	public void setAllowedExt(String allowedExt) {
		this.allowedExt = allowedExt;
	}

	/**
	 * @param deniedExt the deniedExt to set
	 */
	public void setDeniedExt(String deniedExt) {
		this.deniedExt = deniedExt;
	}

	/**
	 * @return the disperse
	 */
	public boolean isDisperse() {
		return disperse;
	}

	/**
	 * @param disperse the disperse to set
	 */
	public void setDisperse(boolean disperse) {
		this.disperse = disperse;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the appCode
	 */
	public boolean isAppCode() {
		return appCode;
	}

	/**
	 * @param appCode the appCode to set
	 */
	public void setAppCode(boolean appCode) {
		this.appCode = appCode;
	}
}
