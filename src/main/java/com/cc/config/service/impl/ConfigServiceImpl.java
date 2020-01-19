/**
 * 
 */
package com.cc.config.service.impl;

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
import com.cc.config.bean.ConfigBean;
import com.cc.config.form.ConfigQueryForm;
import com.cc.config.service.ConfigService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;

/**
 * @author Administrator
 *
 */
@Service
public class ConfigServiceImpl implements ConfigService {

	@Override
	@Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
	public void setConfig(String propertyName, String propertyValue) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("propertyName", propertyName);
		List<ConfigBean> propertyList = ConfigBean.findAllByMap(ConfigBean.class, paramMap);
		ConfigBean configBean = new ConfigBean();
		configBean.setPropertyValue(propertyValue);
		if (ListTools.isEmptyOrNull(propertyList)) {
			configBean.setPropertyName(propertyName);
			int row = configBean.save();
			if (row!=1) {
				throw new LogicException("E001", "设置属性["+propertyName+"]失败");
			}
		}else {
			if (propertyList.size()>1) {
				throw new LogicException("E002", "属性["+propertyName+"]不唯一");
			}
			Example example = new Example(ConfigBean.class);
			Example.Criteria criteria = example.createCriteria();
			criteria.andEqualTo("propertyName", propertyName);
			int row = configBean.updateByExample(example);
			if (row!=1) {
				throw new LogicException("E003", "更新属性["+propertyName+"]失败");
			}
		}
	}

	@Override
	public void saveConfig(ConfigBean configBean) {
		int row = configBean.save();
		if (row!=1) {
			throw new LogicException("E001", "保存属性["+configBean.getPropertyName()+"]失败");
		}
	}

	@Override
	public List<ConfigBean> queryConfigBeanList() {
		List<ConfigBean> configBeanList = ConfigBean.findAllByParams(ConfigBean.class);
		return configBeanList;
	}

	@Override
	public ConfigBean queryConfigBean(String propertyName) {
		return ConfigBean.findOneByParams(ConfigBean.class, "propertyName", propertyName);
	}

	@Override
	public List<ConfigBean> queryConfigBeanList(String prefix) {
		if (StringTools.isNullOrNone(prefix)) {
			return null;
		}
		Example example = new Example(ConfigBean.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andLike("propertyName", prefix+".%");
		List<ConfigBean> configBeanList = ConfigBean.findByExample(ConfigBean.class, example);
		return configBeanList;
	}

	@Override
	public Page<ConfigBean> queryConfigBeanPage(ConfigQueryForm form) {
		Page<ConfigBean> page = new Page<ConfigBean>();
		Example example = new Example(ConfigBean.class);
		Example.Criteria criteria = example.createCriteria();
		if(!StringTools.isNullOrNone(form.getPropertyName())){
			criteria.andLike("propertyName", "%"+form.getPropertyName()+"%");
		}
		if(!StringTools.isNullOrNone(form.getPropertyDesc())){
			criteria.andLike("propertyDesc", "%"+form.getPropertyDesc()+"%");
		}
		PageHelper.orderBy(String.format("%s %s", form.getSort(), form.getOrder()));
		PageHelper.startPage(form.getPage(), form.getPageSize());
		List<ConfigBean> configBeanList = ConfigBean.findByExample(ConfigBean.class, example);
		PageInfo<ConfigBean> pageInfo = new PageInfo<ConfigBean>(configBeanList);
		if (ListTools.isEmptyOrNull(configBeanList)) {
			page.setMessage("没有查询到相关参数数据");
			return page;
		}
		page.setPage(pageInfo.getPageNum());
		page.setPages(pageInfo.getPages());
		page.setPageSize(pageInfo.getPageSize());
		page.setTotal(pageInfo.getTotal());
		page.setData(configBeanList);
		page.setSuccess(Boolean.TRUE);
		return page;
	}

	@Override
	public void deleteConfig(Long id) {
		ConfigBean configBean = new ConfigBean();
		configBean.setId(id);
		int row = configBean.delete();
		if(row!=1){
			throw new LogicException("E001", "删除参数失败");
		}
	}

}
