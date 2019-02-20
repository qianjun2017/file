/**
 * 
 */
package com.cc.common.exception;

/**
 * @author Administrator
 *
 */
public class LogicException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5873458794492889307L;

	/**
	 * 异常编码
	 */
	private String errCode;
	
	/**
	 * 异常信息
	 */
	private String errContent;

	/**
	 * @param errCode
	 * @param errContent
	 */
	public LogicException(String errCode, String errContent) {
		super();
		this.errCode = errCode;
		this.errContent = errContent;
	}

	/**
	 * @return the errCode
	 */
	public String getErrCode() {
		return errCode;
	}

	/**
	 * @param errCode the errCode to set
	 */
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	/**
	 * @return the errContent
	 */
	public String getErrContent() {
		return errContent;
	}

	/**
	 * @param errContent the errContent to set
	 */
	public void setErrContent(String errContent) {
		this.errContent = errContent;
	}
	
	
}
