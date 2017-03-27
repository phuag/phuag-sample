/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.phuag.sample.common.persistence;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * DAO支持类实现
 * @author ThinkGem
 * @version 2014-05-16
 */
public interface BaseDao<T> extends Mapper<T>, MySqlMapper<T> {

}