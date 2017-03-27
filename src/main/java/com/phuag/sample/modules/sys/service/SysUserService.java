package com.phuag.sample.modules.sys.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.phuag.sample.common.security.Salt;
import com.phuag.sample.common.utils.Encodes;
import com.phuag.sample.config.shiro.ShiroProperties;
import com.phuag.sample.modules.sys.dao.SysUserMapper;
import com.phuag.sample.modules.sys.model.SysRole;
import com.phuag.sample.modules.sys.model.SysUser;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2016/3/27 0027.
 */
@Service
@Transactional
public class SysUserService {

    @Autowired
    private ShiroProperties properties;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SessionDAO sessionDao;

    public SessionDAO getSessionDao() {
        return sessionDao;
    }

    public int insertSysUser(SysUser sysUser) {
        return sysUserMapper.insert(sysUser);
    }

    public SysUser getSysUserById(String id) {
        return sysUserMapper.selectByPrimaryKey(id);
    }
    public Page<SysUser> getAllSysUser(Pageable page) {
        PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        //TODO need to add sort function ,but the pagerhelper is not support well.
        //PageHelper.orderBy("STAFF_NAME desc");
        return (Page<SysUser>) sysUserMapper.selectAll();
    }


    public SysUser getSysUserByLoginName(String username) {
        return sysUserMapper.selectSysUserByLoginName(username);
    }

    public int updateByPrimaryKey(SysUser record){
        return sysUserMapper.updateByPrimaryKey(record);
    }

    public List<SysRole> getSysUserRolesByUser(SysUser user) {
        return sysUserMapper.getSysUserRolesByUser(user);
    }

    public SysUser updatePassword(SysUser user){
        String hashedPassword = entryptPassword(user.getPassword());
        user.setPassword(hashedPassword);
        sysUserMapper.updateByPrimaryKeySelective(user);
        return user;
    }

    /**
     * 生成安全的密码，生成随机的16位salt并经 过1024次sha-1 hash
     */
    public  String entryptPassword(String plainPassword) {
        byte[] salt = Salt.generateSalt(properties.getSaltSize());
//        byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, HASH_INTERATIONS);
        byte[] hashPassword = new SimpleHash(properties.getHashAlgorithmName(),plainPassword,salt,properties.getHashIterations()).getBytes();
        return Encodes.encodeHex(salt)+ Encodes.encodeHex(hashPassword);
    }
}
