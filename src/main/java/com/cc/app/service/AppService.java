/**
 * 
 */
package com.cc.app.service;

import java.util.List;

import com.cc.common.web.Page;
import com.cc.app.bean.AppBean;
import com.cc.app.form.AppQueryForm;

/**
 * @author Administrator
 *
 */
public interface AppService {

	/**
	 * 保存应用
	 * @param appBean
	 */
	void saveApp(AppBean appBean);
	
	/**
	 * 查询应用
	 * @return
	 */
	List<AppBean> queryAppBeanList();
	
	/**
	 * 查询应用
	 * @param code
	 * @return
	 */
	AppBean queryAppBean(String code);
	
	/**
	 * 分页查询应用
	 * @param form
	 * @return
	 */
	Page<AppBean> queryAppBeanPage(AppQueryForm form);

	/**
	 * 删除应用
	 * @param id
	 */
	void deleteApp(Long id);
}
