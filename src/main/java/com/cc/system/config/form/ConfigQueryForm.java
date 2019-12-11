/**
 * 
 */
package com.cc.system.config.form;

import com.cc.common.form.QueryForm;

/**
 * @author Administrator
 *
 */
public class ConfigQueryForm extends QueryForm {

	/**
	 * 关键字
	 */
	private String propertyDesc;
	
	/**
	 * 属性名称
	 */
	private String propertyName;

	/**
	 * @return the propertyDesc
	 */
	public String getPropertyDesc() {
		return propertyDesc;
	}

	/**
	 * @param propertyDesc the propertyDesc to set
	 */
	public void setPropertyDesc(String propertyDesc) {
		this.propertyDesc = propertyDesc;
	}

	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @param propertyName the propertyName to set
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	@Override
	public String getSort() {
		if(super.getSort().equals("createTime")){
			return "id";
		}else{
			return super.getSort();
		}
	}

}
