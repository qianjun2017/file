/**
 * 
 */
package com.cc.system.config.service;

import java.util.List;

import com.cc.common.web.Page;
import com.cc.system.config.bean.SystemConfigBean;
import com.cc.system.config.form.ConfigQueryForm;

/**
 * @author Administrator
 *
 */
public interface SystemConfigService {

	/**
	 * 设置系统参数
	 * @param propertyName
	 * @param propertyValue
	 */
	void setSystemConfig(String propertyName, String propertyValue);

	/**
	 * 保存系统参数
	 * @param systemConfigBean
	 */
	void saveSystemConfig(SystemConfigBean systemConfigBean);
	
	/**
	 * 查询系统参数
	 * @return
	 */
	List<SystemConfigBean> querySystemConfigBeanList();
	
	/**
	 * 查询系统参数
	 * @param propertyName
	 * @return
	 */
	SystemConfigBean querySystemConfigBean(String propertyName);
	
	/**
	 * 查询系统参数
	 * @param prefix
	 * @return
	 */
	List<SystemConfigBean> querySystemConfigBeanList(String prefix);

	/**
	 * 分页查询系统参数
	 * @param form
	 * @return
	 */
	Page<SystemConfigBean> querySystemConfigBeanPage(ConfigQueryForm form);

	/**
	 * 删除系统参数
	 * @param id
	 */
	void deleteSystemConfig(Long id);
}
