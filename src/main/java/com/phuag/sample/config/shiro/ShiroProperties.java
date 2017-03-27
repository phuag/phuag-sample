package com.phuag.sample.config.shiro;

import org.apache.shiro.realm.Realm;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * Created by vvvvvv on 2017/3/9.
 */
@ConfigurationProperties(prefix ="shiro")
public class ShiroProperties {
    private Class<? extends Realm> realmClass = null;

    private String loginUrl = "/login";

    private String successUrl = "/index";

    private String unauthorizedUrl = "/unauthorized";

    private String hashAlgorithmName = "SHA-1";

    private int hashIterations = 1024;

    private int saltSize = 8;

    private int retryMax = 5;

    private boolean storedCredentialsHexEncoded = true;

    private Map<String, String> filterChainDefinitions;

    private String filerChainSql;

    private String ehCacheManagerConfigFile = "classpath:ehcache/ehcache-shiro.xml";

    public Class<? extends Realm> getRealmClass() {
        return realmClass;
    }

    public void setRealmClass(Class<? extends Realm> realmClass) {
        this.realmClass = realmClass;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }

    public String getHashAlgorithmName() {
        return hashAlgorithmName;
    }

    public void setHashAlgorithmName(String hashAlgorithmName) {
        this.hashAlgorithmName = hashAlgorithmName;
    }

    public int getHashIterations() {
        return hashIterations;
    }

    public void setHashIterations(int hashIterations) {
        this.hashIterations = hashIterations;
    }

    public int getSaltSize() {
        return saltSize;
    }

    public void setSaltSize(int saltSize) {
        this.saltSize = saltSize;
    }

    public int getRetryMax() {
        return retryMax;
    }

    public void setRetryMax(int retryMax) {
        this.retryMax = retryMax;
    }

    public boolean isStoredCredentialsHexEncoded() {
        return storedCredentialsHexEncoded;
    }

    public void setStoredCredentialsHexEncoded(boolean storedCredentialsHexEncoded) {
        this.storedCredentialsHexEncoded = storedCredentialsHexEncoded;
    }

    public Map<String, String> getFilterChainDefinitions() {
        return filterChainDefinitions;
    }

    public void setFilterChainDefinitions(Map<String, String> filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }

    public String getFilerChainSql() {
        return filerChainSql;
    }

    public void setFilerChainSql(String filerChainSql) {
        this.filerChainSql = filerChainSql;
    }

    public String getEhCacheManagerConfigFile() {
        return ehCacheManagerConfigFile;
    }

    public void setEhCacheManagerConfigFile(String ehCacheManagerConfigFile) {
        this.ehCacheManagerConfigFile = ehCacheManagerConfigFile;
    }
}
