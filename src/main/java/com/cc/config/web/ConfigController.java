/**
 * 
 */
package com.cc.config.web;

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
import com.cc.config.bean.ConfigBean;
import com.cc.config.form.ConfigQueryForm;
import com.cc.config.service.ConfigService;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/config")
public class ConfigController {
	
	@Autowired
	private ConfigService configService;
	
	/**
	 * 新增参数
	 * @param propertyMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Response<String> addProperty(@RequestBody Map<String, String> propertyMap){
		Response<String> response = new Response<String>();
		try {
			ConfigBean configBean = new ConfigBean();
			String propertyName = propertyMap.get("propertyName");
			if(StringTools.isNullOrNone(propertyName)){
				response.setMessage("参数名称不能为空");
				return response;
			}
			configBean.setPropertyName(propertyName);
			String propertyValue = propertyMap.get("propertyValue");
			if(StringTools.isNullOrNone(propertyValue)){
				response.setMessage("参数值不能为空");
				return response;
			}
			configBean.setPropertyValue(propertyValue);
			configBean.setPropertyDesc(propertyMap.get("propertyDesc"));
			configService.saveConfig(configBean);
			response.setSuccess(Boolean.TRUE);
		} catch (LogicException e) {
			response.setMessage(e.getErrContent());
		} catch (Exception e) {
			response.setMessage("内部错误");
			e.printStackTrace();
		}
		return response;
	}


	/**
	 * 修改参数
	 * @param propertyMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Response<String> updateProperty(@RequestBody Map<String, String> propertyMap){
		Response<String> response = new Response<String>();
		String id = propertyMap.get("id");
		if(StringTools.isNullOrNone(id)){
			response.setMessage("缺少参数主键");
			return response;
		}
		ConfigBean configBean = ConfigBean.get(ConfigBean.class, Long.valueOf(id));
		if(configBean == null){
			response.setMessage("参数不存在");
			return response;
		}
		try {
			String propertyName = propertyMap.get("propertyName");
			if(StringTools.isNullOrNone(propertyName)){
				response.setMessage("参数名称不能为空");
				return response;
			}
			configBean.setPropertyName(propertyName);
			String propertyValue = propertyMap.get("propertyValue");
			if(StringTools.isNullOrNone(propertyValue)){
				response.setMessage("参数值不能为空");
				return response;
			}
			configBean.setPropertyValue(propertyValue);
			configBean.setPropertyDesc(propertyMap.get("propertyDesc"));
			configService.saveConfig(configBean);
			response.setSuccess(Boolean.TRUE);
		} catch (LogicException e) {
			response.setMessage(e.getErrContent());
		} catch (Exception e) {
			response.setMessage("内部错误");
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * 获取参数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public Response<Map<String, Object>> queryProperties(){
		Response<Map<String, Object>> response = new Response<Map<String,Object>>();
		Map<String, Object> configMap = new HashMap<String, Object>();
		List<ConfigBean> configBeanList = configService.queryConfigBeanList();
		if (ListTools.isEmptyOrNull(configBeanList)) {
			response.setMessage("没有设置参数");
			return response;
		}
		for (ConfigBean configBean : configBeanList) {
			configMap.put(configBean.getPropertyName(), configBean.getPropertyValue());
		}
		response.setData(configMap);
		response.setSuccess(Boolean.TRUE);
		return response;
	}
	
	/**
	 * 获取相同前缀的参数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/prefix", method = RequestMethod.GET)
	public Response<Map<String, Object>> queryProperties(@ModelAttribute ConfigQueryForm form){
		Response<Map<String, Object>> response = new Response<Map<String,Object>>();
		Map<String, Object> configMap = new HashMap<String, Object>();
		List<ConfigBean> configBeanList = configService.queryConfigBeanList(form.getPropertyName());
		if (ListTools.isEmptyOrNull(configBeanList)) {
			response.setMessage("没有设置参数");
			return response;
		}
		for (ConfigBean configBean : configBeanList) {
			configMap.put(configBean.getPropertyName(), configBean.getPropertyValue());
		}
		response.setData(configMap);
		response.setSuccess(Boolean.TRUE);
		return response;
	}
	
	/**
	 * 查询参数
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/name", method = RequestMethod.GET)
	public Response<String> queryConfigBean(@ModelAttribute ConfigQueryForm form){
		Response<String> response = new Response<String>();
		List<ConfigBean> configBeanList = ConfigBean.findAllByParams(ConfigBean.class, "propertyName", form.getPropertyName());
		if(ListTools.isEmptyOrNull(configBeanList)){
			response.setMessage("没有设置参数");
			return response;
		}
		response.setData(configBeanList.get(0).getPropertyValue());
		response.setSuccess(Boolean.TRUE);
		return response;
	}
	
	/**
	 * 分页查询参数
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public Page<ConfigBean> queryConfigBeanPage(@ModelAttribute ConfigQueryForm form){
		Page<ConfigBean> page = configService.queryConfigBeanPage(form);
		return page;
	}
	
	/**
	 * 删除参数
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete/{id:\\d+}", method = RequestMethod.POST)
	public Response<String> deleteUser(@PathVariable Long id){
		Response<String> response = new Response<String>();
		try {
			configService.deleteConfig(id);
			response.setSuccess(Boolean.TRUE);
		} catch (LogicException e) {
			response.setMessage(e.getErrContent());
		} catch (Exception e) {
			response.setMessage("内部错误");
			e.printStackTrace();
		}
		return response;
	}
}
