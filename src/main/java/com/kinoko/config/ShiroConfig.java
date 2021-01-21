package com.kinoko.config;


import com.kinoko.shiro.realm.StudentRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    //创建 ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean
            (@Qualifier("manager") DefaultWebSecurityManager manager){
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(manager);

        factoryBean.setUnauthorizedUrl("/noAuth");
        factoryBean.setSuccessUrl("/toDashboard");
        factoryBean.setLoginUrl("/unauthorized");

        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/logout","logout");
        filterMap.put("/static/**","anon");
        filterMap.put("/css/**","anon");
        filterMap.put("/noAuth","anon");
        filterMap.put("/js/**","anon");
        filterMap.put("/img/**","anon");
        filterMap.put("/error","anon");
        filterMap.put("/","anon");
        filterMap.put("/doLogin","anon");

        filterMap.put("/**","authc");

        factoryBean.setFilterChainDefinitionMap(filterMap);
        return factoryBean;
    }

    //创建 DefaultWebSecurityManager
    @Bean(name = "manager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager
            (@Qualifier("realm") StudentRealm studentRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(studentRealm);
        return securityManager;
    }

    //创建 realm 对象
    @Bean(name = "realm")
    public StudentRealm getStudentRealm(@Qualifier("hashedCredentialsMatcher") HashedCredentialsMatcher matcher){

        StudentRealm studentRealm = new StudentRealm();
        studentRealm.setAuthenticationCachingEnabled(false);
        studentRealm.setCredentialsMatcher(matcher);
        return studentRealm;
    }


    /**
     * 密码得md5加密在这里配置
     */
    @Bean("hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //指定加密方式为MD5
        credentialsMatcher.setHashAlgorithmName("MD5");
        //加密次数
        credentialsMatcher.setHashIterations(1);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }
}
