/**
 * 
 */
package com.cc.common.orm.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author Administrator
 *
 */
public interface CrudMapper<T> extends Mapper<T>,MySqlMapper<T> {

}
