/**
 * 
 */
package com.cc.common.orm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cc.common.exception.LogicException;
import com.cc.common.orm.entity.BaseEntity;
import com.cc.common.orm.mapper.CrudMapper;
import com.cc.common.spring.SpringContextUtil;
import com.cc.common.tools.StringTools;
import com.cc.common.utils.GenericsUtils;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;

/**
 * orm映射基类
 * @author Administrator
 *
 */
public abstract class BaseOrm<T extends BaseEntity> {
	
	private CrudMapper<T> crudMapper;
	
	public abstract Object getId();
	
	private CrudMapper<T> getCrudMapper(){
		if(crudMapper==null){
			Class<?> entityClass = GenericsUtils.getSuperClassGenricType(getClass());
			String simpleName = entityClass.getSimpleName();
			crudMapper = (CrudMapper<T>) SpringContextUtil.getBean(simpleName+"Mapper");
		}
		return crudMapper;
	}

	/**
	 * 保存
	 * @return
	 */
	@Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
	public int save(){
		int result = 0;
		if(this.getId()!=null){
			result = getCrudMapper().updateByPrimaryKey((T) this);
		}else {
			result = getCrudMapper().insert((T) this);
		}
		return result;
	}
	
	/**
	 * 新增
	 * @return
	 */
	@Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
	public int insert(){
		return getCrudMapper().insert((T) this);
	}
	
	/**
	 * 更新，只更新非null的字段
	 * @return
	 */
	@Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
	public int update(){
		return getCrudMapper().updateByPrimaryKeySelective((T) this);
	}
	
	/**
	 * 更新，更新全部字段
	 * @return
	 */
	@Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
	public int updateForce(){
		return getCrudMapper().updateByPrimaryKey((T) this);
	}
	
	/**
	 * 删除
	 * @return
	 */
	@Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
	public int delete(){
		return getCrudMapper().deleteByPrimaryKey(this);
	}
	
	/**
	 * 删除
	 * @param tClass
	 * @param example
	 * @return
	 */
	@Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
	public static <T> int deleteByExample(Class<T> tClass, Object example) {
		CrudMapper<T> mapper = (CrudMapper<T>) SpringContextUtil.getBean(tClass.getSimpleName()+"Mapper");
		return mapper.deleteByExample(example);
	}
	
	/**
	 * 查询
	 * @param tClass
	 * @param id
	 * @return
	 */
	public static <T> T get(Class<T> tClass, Object id){
		CrudMapper<T> mapper = (CrudMapper<T>) SpringContextUtil.getBean(tClass.getSimpleName()+"Mapper");
		return mapper.selectByPrimaryKey(id);
	}
	
	/**
	 * 查询列表
	 * @param tClass
	 * @param values
	 * @return
	 */
	public static <T> List<T> findAllByParams(Class<T> tClass, Object... values){
		if (values.length%2!=0) {
			throw new LogicException("E001", "参数格式错误");
		}
		Map<String, Object> paramMap  = new HashMap<String, Object>();
		for(int i = 0; i < values.length/2; i ++){
			paramMap.put(StringTools.toString(values[2*i]), values[2*i+1]);
		}
		return findAllByMap(tClass, paramMap);
	}
	
	/**
	 * 查询列表
	 * @param tClass
	 * @param paramMap
	 * @return
	 */
	public static <T> List<T> findAllByMap(Class<T> tClass, Map<String, Object> paramMap){
		Example example = new Example(tClass);
		Example.Criteria criteria = example.createCriteria();
		if (paramMap!=null && !paramMap.isEmpty()) {
			String sort = StringTools.toString(paramMap.get("sort"));
			paramMap.remove("sort");
			String order = StringTools.toString(paramMap.get("order"));
			paramMap.remove("order");
			if (!StringTools.isAnyNullOrNone(new String[]{sort, order})) {
				PageHelper.orderBy(String.format("%s %s", sort, order));
			}
			Iterator<String> iterator = paramMap.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				criteria.andEqualTo(key, paramMap.get(key));
			}
		}
		return findByExample(tClass, example);
	}
	
	/**
	 * 查询列表
	 * @param tClass
	 * @param example
	 * @return
	 */
	public static <T> List<T> findByExample(Class<T> tClass, Object example){
		CrudMapper<T> mapper = (CrudMapper<T>) SpringContextUtil.getBean(tClass.getSimpleName()+"Mapper");
		return mapper.selectByExample(example);
	}
	
	/**
	 * 更新数据
	 * @param example
	 * @return
	 */
	public int updateByExample(Object example){
		return getCrudMapper().updateByExampleSelective((T) this, example);
	}
}
