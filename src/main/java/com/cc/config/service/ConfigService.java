/**
 * 
 */
package com.cc.config.service;

import java.util.List;

import com.cc.common.web.Page;
import com.cc.config.bean.ConfigBean;
import com.cc.config.form.ConfigQueryForm;

/**
 * @author Administrator
 *
 */
public interface ConfigService {

	/**
	 * 设置参数
	 * @param propertyName
	 * @param propertyValue
	 */
	void setConfig(String propertyName, String propertyValue);

	/**
	 * 保存参数
	 * @param configBean
	 */
	void saveConfig(ConfigBean configBean);
	
	/**
	 * 查询参数
	 * @return
	 */
	List<ConfigBean> queryConfigBeanList();
	
	/**
	 * 查询参数
	 * @param propertyName
	 * @return
	 */
	ConfigBean queryConfigBean(String propertyName);
	
	/**
	 * 查询参数
	 * @param prefix
	 * @return
	 */
	List<ConfigBean> queryConfigBeanList(String prefix);

	/**
	 * 分页查询参数
	 * @param form
	 * @return
	 */
	Page<ConfigBean> queryConfigBeanPage(ConfigQueryForm form);

	/**
	 * 删除参数
	 * @param id
	 */
	void deleteConfig(Long id);
}
