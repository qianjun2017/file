/**
 * 
 */
package com.cc.app.web;

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
import com.cc.common.tools.DateTools;
import com.cc.common.tools.ListTools;
import com.cc.common.tools.StringTools;
import com.cc.common.web.Page;
import com.cc.common.web.Response;
import com.cc.app.bean.AppBean;
import com.cc.app.form.AppQueryForm;
import com.cc.app.service.AppService;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/app")
public class AppController {
	
	@Autowired
	private AppService appService;
	
	/**
	 * 新增应用
	 * @param appMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Response<String> addApp(@RequestBody Map<String, String> appMap){
		Response<String> response = new Response<String>();
		try {
			AppBean appBean = new AppBean();
			String code = appMap.get("code");
			if(StringTools.isNullOrNone(code)){
				response.setMessage("请输入应用编码");
				return response;
			}
			AppBean sameCodeAppBean = appService.queryAppBean(code);
			if(sameCodeAppBean!=null){
				response.setMessage("应用编码已存在，请重新输入");
				return response;
			}
			appBean.setCode(code);
			String name = appMap.get("name");
			if(StringTools.isNullOrNone(name)){
				response.setMessage("请输入应用名称");
				return response;
			}
			appBean.setName(name);
			appBean.setCreateTime(DateTools.now());
			appService.saveApp(appBean);
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
	 * 修改应用
	 * @param appMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Response<String> updateApp(@RequestBody Map<String, String> appMap){
		Response<String> response = new Response<String>();
		String id = appMap.get("id");
		if(StringTools.isNullOrNone(id)){
			response.setMessage("请选择应用");
			return response;
		}
		AppBean appBean = AppBean.get(AppBean.class, Long.valueOf(id));
		if(appBean == null){
			response.setMessage("应用不存在或已删除");
			return response;
		}
		try {
			String code = appMap.get("code");
			if(StringTools.isNullOrNone(code)){
				response.setMessage("请输入应用编码");
				return response;
			}
			List<AppBean> sameCodeAppBeanList = AppBean.findAllByParams(AppBean.class, "code", code);
			for(AppBean sameCodeAppBean: sameCodeAppBeanList){
				if(!sameCodeAppBean.getId().equals(appBean.getId()) && sameCodeAppBean.getCode().equals(code)){
					response.setMessage("应用编码已存在，请重新输入");
					return response;
				}
			}
			appBean.setCode(code);
			String name = appMap.get("name");
			if(StringTools.isNullOrNone(name)){
				response.setMessage("请输入应用名称");
				return response;
			}
			appBean.setName(name);
			appService.saveApp(appBean);
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
	 * 查询应用
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/code", method = RequestMethod.GET)
	public Response<AppBean> queryAppBean(@ModelAttribute AppQueryForm form){
		Response<AppBean> response = new Response<AppBean>();
		List<AppBean> appBeanList = AppBean.findAllByParams(AppBean.class, "code", form.getCode());
		if(ListTools.isEmptyOrNull(appBeanList)){
			response.setMessage("应用不存在或已删除");
			return response;
		}
		response.setData(appBeanList.get(0));
		response.setSuccess(Boolean.TRUE);
		return response;
	}
	
	/**
	 * 分页查询应用
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public Page<AppBean> queryAppPage(@ModelAttribute AppQueryForm form){
		Page<AppBean> page = appService.queryAppBeanPage(form);
		return page;
	}
	
	/**
	 * 删除应用
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete/{id:\\d+}", method = RequestMethod.POST)
	public Response<String> deleteApp(@PathVariable Long id){
		Response<String> response = new Response<String>();
		try {
			appService.deleteApp(id);
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
	 * 查询应用列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public Response<Object> queryAllApp(){
		Response<Object> response = new Response<Object>();
		List<AppBean> appBeanList = appService.queryAppBeanList();
		if(ListTools.isEmptyOrNull(appBeanList)){
			response.setMessage("没有查询到相关应用数据");
			return response;
		}
		response.setData(appBeanList);
		response.setSuccess(Boolean.TRUE);
		return response;
	}
}
