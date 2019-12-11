/**
 * 
 */
package com.cc.system.config.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cc.common.exception.LogicException;
import com.cc.common.tools.ListTools;
import com.cc.common.tools.StringTools;
import com.cc.common.web.Page;
import com.cc.common.web.Response;
import com.cc.system.config.bean.SystemConfigBean;
import com.cc.system.config.form.ConfigQueryForm;
import com.cc.system.config.service.SystemConfigService;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/system/config")
public class SystemConfigController {
	
	@Autowired
	private SystemConfigService systemConfigService;
	
	/**
	 * 新增系统参数
	 * @param propertyMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Response<String> addProperty(@RequestBody Map<String, String> propertyMap){
		Response<String> response = new Response<String>();
		try {
			SystemConfigBean systemConfigBean = new SystemConfigBean();
			String propertyName = propertyMap.get("propertyName");
			if(StringTools.isNullOrNone(propertyName)){
				response.setMessage("系统参数名称不能为空");
				return response;
			}
			systemConfigBean.setPropertyName(propertyName);
			String propertyValue = propertyMap.get("propertyValue");
			if(StringTools.isNullOrNone(propertyValue)){
				response.setMessage("系统参数值不能为空");
				return response;
			}
			systemConfigBean.setPropertyValue(propertyValue);
			systemConfigBean.setPropertyDesc(propertyMap.get("propertyDesc"));
			systemConfigService.saveSystemConfig(systemConfigBean);
			response.setSuccess(Boolean.TRUE);
		} catch (LogicException e) {
			response.setMessage(e.getErrContent());
		} catch (Exception e) {
			response.setMessage("系统内部错误");
			e.printStackTrace();
		}
		return response;
	}


	/**
	 * 修改系统参数
	 * @param propertyMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Response<String> updateProperty(@RequestBody Map<String, String> propertyMap){
		Response<String> response = new Response<String>();
		String id = propertyMap.get("id");
		if(StringTools.isNullOrNone(id)){
			response.setMessage("缺少系统参数主键");
			return response;
		}
		SystemConfigBean systemConfigBean = SystemConfigBean.get(SystemConfigBean.class, Long.valueOf(id));
		if(systemConfigBean == null){
			response.setMessage("系统参数不存在");
			return response;
		}
		try {
			String propertyName = propertyMap.get("propertyName");
			if(StringTools.isNullOrNone(propertyName)){
				response.setMessage("系统参数名称不能为空");
				return response;
			}
			systemConfigBean.setPropertyName(propertyName);
			String propertyValue = propertyMap.get("propertyValue");
			if(StringTools.isNullOrNone(propertyValue)){
				response.setMessage("系统参数值不能为空");
				return response;
			}
			systemConfigBean.setPropertyValue(propertyValue);
			systemConfigBean.setPropertyDesc(propertyMap.get("propertyDesc"));
			systemConfigService.saveSystemConfig(systemConfigBean);
			response.setSuccess(Boolean.TRUE);
		} catch (LogicException e) {
			response.setMessage(e.getErrContent());
		} catch (Exception e) {
			response.setMessage("系统内部错误");
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * 获取系统参数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public Response<Map<String, Object>> queryProperties(){
		Response<Map<String, Object>> response = new Response<Map<String,Object>>();
		Map<String, Object> systemConfigMap = new HashMap<String, Object>();
		List<SystemConfigBean> systemConfigBeanList = systemConfigService.querySystemConfigBeanList();
		if (ListTools.isEmptyOrNull(systemConfigBeanList)) {
			response.setMessage("没有设置系统参数");
			return response;
		}
		for (SystemConfigBean systemConfigBean : systemConfigBeanList) {
			systemConfigMap.put(systemConfigBean.getPropertyName(), systemConfigBean.getPropertyValue());
		}
		response.setData(systemConfigMap);
		response.setSuccess(Boolean.TRUE);
		return response;
	}
	
	/**
	 * 获取相同前缀的系统参数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/prefix", method = RequestMethod.GET)
	public Response<Map<String, Object>> queryProperties(@ModelAttribute ConfigQueryForm form){
		Response<Map<String, Object>> response = new Response<Map<String,Object>>();
		Map<String, Object> systemConfigMap = new HashMap<String, Object>();
		List<SystemConfigBean> systemConfigBeanList = systemConfigService.querySystemConfigBeanList(form.getPropertyName());
		if (ListTools.isEmptyOrNull(systemConfigBeanList)) {
			response.setMessage("没有设置系统参数");
			return response;
		}
		for (SystemConfigBean systemConfigBean : systemConfigBeanList) {
			systemConfigMap.put(systemConfigBean.getPropertyName(), systemConfigBean.getPropertyValue());
		}
		response.setData(systemConfigMap);
		response.setSuccess(Boolean.TRUE);
		return response;
	}
	
	/**
	 * 查询系统参数
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/name", method = RequestMethod.GET)
	public Response<String> querySystemConfigBean(@ModelAttribute ConfigQueryForm form){
		Response<String> response = new Response<String>();
		List<SystemConfigBean> systemConfigBeanList = SystemConfigBean.findAllByParams(SystemConfigBean.class, "propertyName", form.getPropertyName());
		if(ListTools.isEmptyOrNull(systemConfigBeanList)){
			response.setMessage("没有设置系统参数");
			return response;
		}
		response.setData(systemConfigBeanList.get(0).getPropertyValue());
		response.setSuccess(Boolean.TRUE);
		return response;
	}
	
	/**
	 * 分页查询系统参数
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public Page<SystemConfigBean> querySystemConfigBeanPage(@ModelAttribute ConfigQueryForm form){
		Page<SystemConfigBean> page = systemConfigService.querySystemConfigBeanPage(form);
		return page;
	}
	
	/**
	 * 删除系统参数
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete/{id:\\d+}", method = RequestMethod.POST)
	public Response<String> deleteUser(@PathVariable Long id){
		Response<String> response = new Response<String>();
		try {
			systemConfigService.deleteSystemConfig(id);
			response.setSuccess(Boolean.TRUE);
		} catch (LogicException e) {
			response.setMessage(e.getErrContent());
		} catch (Exception e) {
			response.setMessage("系统内部错误");
			e.printStackTrace();
		}
		return response;
	}
}
