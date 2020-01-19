/**
 * 
 */
package com.cc.app.form;

import com.cc.common.form.QueryForm;

/**
 * @author Administrator
 *
 */
public class AppQueryForm extends QueryForm {
	
	/**
	 * 应用编码
	 */
	private String code;

	/**
	 * 应用名称
	 */
	private String name;
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
