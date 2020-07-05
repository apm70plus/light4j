package com.light.auth.config;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import com.light.auth.core.RestShiroFilterFactoryBean;

@ConditionalOnProperty(name="framework.auth.type", havingValue="session", matchIfMissing=false)
public class ShiroSessionAuthConfiguration {

    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(
    		DefaultWebSecurityManager securityManager,
    		ShiroFilterChainDefinition shiroFilterChainDefinition) {
    	RestShiroFilterFactoryBean factoryBean = new RestShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);
        //自定义url规则 http://shiro.apache.org/web.html#urls-
        factoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition.getFilterChainMap());
    	return factoryBean;
    }
	
    @Bean
    @ConditionalOnBean(value = RedisManager.class)
    public RedisSessionDAO sessionDAO(RedisManager redisManager) {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager);
        return redisSessionDAO;
    }
}
