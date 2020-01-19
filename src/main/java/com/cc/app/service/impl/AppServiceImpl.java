/**
 * 
 */
package com.cc.app.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cc.common.exception.LogicException;
import com.cc.common.tools.ListTools;
import com.cc.common.tools.StringTools;
import com.cc.common.web.Page;
import com.cc.app.bean.AppBean;
import com.cc.app.form.AppQueryForm;
import com.cc.app.service.AppService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;

/**
 * @author Administrator
 *
 */
@Service
public class AppServiceImpl implements AppService {

	@Override
	public void saveApp(AppBean appBean) {
		int row = appBean.save();
		if (row!=1) {
			throw new LogicException("E001", "保存失败");
		}
	}

	@Override
	public List<AppBean> queryAppBeanList() {
		List<AppBean> appBeanList = AppBean.findAllByParams(AppBean.class);
		return appBeanList;
	}

	@Override
	public AppBean queryAppBean(String code) {
		return AppBean.findOneByParams(AppBean.class, "code", code);
	}

	@Override
	public Page<AppBean> queryAppBeanPage(AppQueryForm form) {
		Page<AppBean> page = new Page<AppBean>();
		Example example = new Example(AppBean.class);
		Example.Criteria criteria = example.createCriteria();
		if(!StringTools.isNullOrNone(form.getCode())){
			criteria.andLike("code", "%"+form.getCode()+"%");
		}
		if(!StringTools.isNullOrNone(form.getName())){
			criteria.andLike("name", "%"+form.getName()+"%");
		}
		PageHelper.orderBy(String.format("%s %s", form.getSort(), form.getOrder()));
		PageHelper.startPage(form.getPage(), form.getPageSize());
		List<AppBean> appBeanList = AppBean.findByExample(AppBean.class, example);
		PageInfo<AppBean> pageInfo = new PageInfo<AppBean>(appBeanList);
		if (ListTools.isEmptyOrNull(appBeanList)) {
			page.setMessage("没有查询到相关应用数据");
			return page;
		}
		page.setPage(pageInfo.getPageNum());
		page.setPages(pageInfo.getPages());
		page.setPageSize(pageInfo.getPageSize());
		page.setTotal(pageInfo.getTotal());
		page.setData(appBeanList);
		page.setSuccess(Boolean.TRUE);
		return page;
	}

	@Override
	public void deleteApp(Long id) {
		AppBean appBean = new AppBean();
		appBean.setId(id);
		int row = appBean.delete();
		if(row!=1){
			throw new LogicException("E001", "删除应用失败");
		}
	}

}
