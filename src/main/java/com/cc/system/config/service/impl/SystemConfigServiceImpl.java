/**
 * 
 */
package com.cc.system.config.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cc.common.exception.LogicException;
import com.cc.common.tools.ListTools;
import com.cc.common.tools.StringTools;
import com.cc.common.web.Page;
import com.cc.system.config.bean.SystemConfigBean;
import com.cc.system.config.form.ConfigQueryForm;
import com.cc.system.config.service.SystemConfigService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;

/**
 * @author Administrator
 *
 */
@Service
public class SystemConfigServiceImpl implements SystemConfigService {

	@Override
	@Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
	public void setSystemConfig(String propertyName, String propertyValue) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("propertyName", propertyName);
		List<SystemConfigBean> propertyList = SystemConfigBean.findAllByMap(SystemConfigBean.class, paramMap);
		SystemConfigBean systemConfigBean = new SystemConfigBean();
		systemConfigBean.setPropertyValue(propertyValue);
		if (ListTools.isEmptyOrNull(propertyList)) {
			systemConfigBean.setPropertyName(propertyName);
			int row = systemConfigBean.save();
			if (row!=1) {
				throw new LogicException("E001", "设置属性["+propertyName+"]失败");
			}
		}else {
			if (propertyList.size()>1) {
				throw new LogicException("E002", "属性["+propertyName+"]不唯一");
			}
			Example example = new Example(SystemConfigBean.class);
			Example.Criteria criteria = example.createCriteria();
			criteria.andEqualTo("propertyName", propertyName);
			int row = systemConfigBean.updateByExample(example);
			if (row!=1) {
				throw new LogicException("E003", "更新属性["+propertyName+"]失败");
			}
		}
	}

	@Override
	public void saveSystemConfig(SystemConfigBean systemConfigBean) {
		int row = systemConfigBean.save();
		if (row!=1) {
			throw new LogicException("E001", "保存属性["+systemConfigBean.getPropertyName()+"]失败");
		}
	}

	@Override
	public List<SystemConfigBean> querySystemConfigBeanList() {
		List<SystemConfigBean> systemConfigBeanList = SystemConfigBean.findAllByParams(SystemConfigBean.class);
		return systemConfigBeanList;
	}

	@Override
	public SystemConfigBean querySystemConfigBean(String propertyName) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("propertyName", propertyName);
		List<SystemConfigBean> propertyList = SystemConfigBean.findAllByMap(SystemConfigBean.class, paramMap);
		if (ListTools.isEmptyOrNull(propertyList)) {
			return null;
		}
		return propertyList.get(0);
	}

	@Override
	public List<SystemConfigBean> querySystemConfigBeanList(String prefix) {
		if (StringTools.isNullOrNone(prefix)) {
			return null;
		}
		Example example = new Example(SystemConfigBean.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andLike("propertyName", prefix+".%");
		List<SystemConfigBean> systemConfigBeanList = SystemConfigBean.findByExample(SystemConfigBean.class, example);
		return systemConfigBeanList;
	}

	/* (non-Javadoc)
	 * @see com.cc.system.config.service.SystemConfigService#querySystemConfigBeanPage(com.cc.system.config.form.ConfigQueryForm)
	 */
	@Override
	public Page<SystemConfigBean> querySystemConfigBeanPage(ConfigQueryForm form) {
		Page<SystemConfigBean> page = new Page<SystemConfigBean>();
		Example example = new Example(SystemConfigBean.class);
		Example.Criteria criteria = example.createCriteria();
		if(!StringTools.isNullOrNone(form.getPropertyName())){
			criteria.andLike("propertyName", "%"+form.getPropertyName()+"%");
		}
		if(!StringTools.isNullOrNone(form.getPropertyDesc())){
			criteria.andLike("propertyDesc", "%"+form.getPropertyDesc()+"%");
		}
		PageHelper.orderBy(String.format("%s %s", form.getSort(), form.getOrder()));
		PageHelper.startPage(form.getPage(), form.getPageSize());
		List<SystemConfigBean> systemConfigBeanList = SystemConfigBean.findByExample(SystemConfigBean.class, example);
		PageInfo<SystemConfigBean> pageInfo = new PageInfo<SystemConfigBean>(systemConfigBeanList);
		if (ListTools.isEmptyOrNull(systemConfigBeanList)) {
			page.setMessage("没有查询到相关系统参数数据");
			return page;
		}
		page.setPage(pageInfo.getPageNum());
		page.setPages(pageInfo.getPages());
		page.setPageSize(pageInfo.getPageSize());
		page.setTotal(pageInfo.getTotal());
		page.setData(systemConfigBeanList);
		page.setSuccess(Boolean.TRUE);
		return page;
	}

	@Override
	public void deleteSystemConfig(Long id) {
		SystemConfigBean systemConfigBean = new SystemConfigBean();
		systemConfigBean.setId(id);
		int row = systemConfigBean.delete();
		if(row!=1){
			throw new LogicException("E001", "删除系统参数失败");
		}
	}

}
