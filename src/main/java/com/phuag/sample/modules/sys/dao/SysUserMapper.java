package com.phuag.sample.modules.sys.dao;

import com.phuag.sample.common.persistence.CrudDao;
import com.phuag.sample.modules.sys.model.SysRole;
import com.phuag.sample.modules.sys.model.SysUser;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value="sysUserMapper")
public interface SysUserMapper extends CrudDao<SysUser> {

//    SysUser selectByPrimaryKey(String id);

    SysUser selectSysUserByLoginName(String loginName);

    List<SysRole> getSysUserRolesByUser(SysUser user);
}