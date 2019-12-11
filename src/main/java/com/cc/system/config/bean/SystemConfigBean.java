/**
 * 
 */
package com.cc.system.config.bean;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cc.common.orm.BaseOrm;
import com.cc.common.orm.entity.BaseEntity;

/**
 * @author Administrator
 *
 */
@Table(name="t_system_config")
public class SystemConfigBean extends BaseOrm<SystemConfigBean> implements BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2129819761022692110L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 属性名称
	 */
	private String propertyName;
	
	/**
	 * 属性值
	 */
	private String propertyValue;

	/**
	 * 属性描述
	 */
	private String propertyDesc;
	
	@Override
	public Long getId() {
		return id;
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

	/**
	 * @return the propertyValue
	 */
	public String getPropertyValue() {
		return propertyValue;
	}

	/**
	 * @param propertyValue the propertyValue to set
	 */
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

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
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
}
