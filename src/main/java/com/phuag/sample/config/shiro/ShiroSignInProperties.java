package com.phuag.sample.config.shiro;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by vvvvvv on 2017/3/10.
 */
@ConfigurationProperties(prefix = "shiro.sign-in")
public class ShiroSignInProperties {

    /**
     * 登录请求的用户名字段名
     */
    private String userParam = "username";

    /**
     * 登录请求的密码字段名
     */
    private String passwordParam = "password";

    private String rememberMeParam = "rememberMe";

    public String getUserParam() {
        return userParam;
    }

    public void setUserParam(String userParam) {
        this.userParam = userParam;
    }

    public String getPasswordParam() {
        return passwordParam;
    }

    public void setPasswordParam(String passwordParam) {
        this.passwordParam = passwordParam;
    }

    public String getRememberMeParam() {
        return rememberMeParam;
    }

    public void setRememberMeParam(String rememberMeParam) {
        this.rememberMeParam = rememberMeParam;
    }
}
