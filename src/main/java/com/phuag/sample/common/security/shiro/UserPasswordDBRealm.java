package com.phuag.sample.common.security.shiro;

/**
 * Created by Administrator on 2016/3/24 0024.
 */

import com.phuag.sample.common.config.Global;
import com.phuag.sample.common.utils.Encodes;
import com.phuag.sample.modules.sys.model.SysRole;
import com.phuag.sample.modules.sys.model.SysUser;
import com.phuag.sample.modules.sys.service.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class UserPasswordDBRealm extends AuthorizingRealm {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 认证回调函数, 登录时调用
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String username = token.getUsername();
        SysUser user = sysUserService.getSysUserByLoginName(username);
        if (user == null) {
            throw new UnknownAccountException("用户不存在");// 没找到帐号
        }
        if(!Global.YES.equals(user.getLoginFlag())){
            throw new DisabledAccountException("用户不允许登录");//不允许登录
        }
        //salt与用户那边的输入没有关系，仅作为隐藏真实密码原文做的变换
        byte[] salt = Encodes.decodeHex(user.getPassword().substring(0, 16));
        //准备准确的认证信息，后面shiro使用用户提交的token信息与真实的认证信息进行比对。
        return new SimpleAuthenticationInfo(user, // 用户
                user.getPassword().substring(16), // 密码
                ByteSource.Util.bytes(salt),//salt
                getName() // realm name
        );

    }

    /**
     * 授权查询回调函数, 进行鉴权时调用
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
        SysUser principal = (SysUser) getAvailablePrincipal(principals);
        String username = (String) principals.getPrimaryPrincipal();
        if (!Global.TRUE.equals(Global.getConfig("user.multiAccountLogin"))){
            //不允许多用户同时登录，同一用户则需踢掉前面登录上的session
            Collection<Session> sessions =sysUserService.getSessionDao().getActiveSessions();
            if (sessions.size() > 0){
                // 如果是登录进来的，则踢出已在线用户
                if (SecurityUtils.getSubject().isAuthenticated()){
                    for (Session session : sessions){
                        sysUserService.getSessionDao().delete(session);
                    }
                }
                // 记住我进来的，并且当前用户已登录，则退出当前用户提示信息。
                else{
                    SecurityUtils.getSubject().logout();
                    throw new AuthenticationException("msg:账号已在其它地方登录，请重新登录。");
                }
            }

        }
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        SysUser user = sysUserService.getSysUserByLoginName(username);
        List<SysRole> uroles = sysUserService.getSysUserRolesByUser(user);
        Set<String> perms = new HashSet<String>();
//        for (SysRole role : uroles) {
//            Set<Resource> resources = role.getResources();
//            for (Resource resource : resources) {
//                Object permission = resource.getPerms();
//                if (permission == null
//                        || StringUtils.isEmpty(permission.toString())) {
//                    continue;
//                }
//                perms.add(StringUtils.substringBetween(permission.toString(),
//                        "perms[", "]"));
//            }
//        }
        authorizationInfo.setStringPermissions(perms);
        return authorizationInfo;
    }
}