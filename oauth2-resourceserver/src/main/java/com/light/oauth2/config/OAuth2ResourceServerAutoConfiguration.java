package com.light.oauth2.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.light.config.Constants;
import com.light.oauth2.cache.Oauth2UserCache;
import com.light.oauth2.cache.Oauth2UserPermissionsCache;
import com.light.oauth2.cache.impl.Oauth2UserCacheRedisImpl;
import com.light.oauth2.cache.impl.Oauth2UserPermissionsCacheRedisImpl;
import com.light.oauth2.filter.OAuth2AuthenticationFilter;
import com.light.oauth2.realm.OAuth2AuthorizingRealm;
import com.light.oauth2.service.AuthenticationService;
import com.light.oauth2.service.AuthorizationService;
import com.light.oauth2.service.impl.AccessTokenAuthorizationServiceImpl;
import com.light.oauth2.service.impl.AuthenticationServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class OAuth2ResourceServerAutoConfiguration {
	
	@PostConstruct
	public void init() {
		log.info(Constants.CONFIG_LOG_MARK, "OAuth2 ResourceServer Enabled");
	}
	
    @Bean("securityManager")
    public DefaultWebSecurityManager getManager(Realm realm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        // 使用自己的realm
        manager.setRealm(realm);
        //manager.setSubjectFactory(new ShiroSubjectFactory());

        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        manager.setSubjectDAO(subjectDAO);

        return manager;
    }
    
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean factory(DefaultWebSecurityManager securityManager, @Autowired(required=false)ShiroFilterChainDefinition shiroFilterChainDefinition) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        // 添加自己的过滤器并且取名为jwtauthc
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("oauthc", new OAuth2AuthenticationFilter());
        factoryBean.setFilters(filterMap);

        factoryBean.setSecurityManager(securityManager);
        //factoryBean.setUnauthorizedUrl("/401");

        /*
         * 自定义url规则
         * http://shiro.apache.org/web.html#urls-
         */
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition("/login", "anon");
        chainDefinition.addPathDefinition("/logout", "logout");
        chainDefinition.addPathDefinition("/favicon.ico", "anon");
        chainDefinition.addPathDefinition("/logo.png", "anon");
        chainDefinition.addPathDefinition("/shiro.css", "anon");
        chainDefinition.addPathDefinition("/*.html", "anon");
        chainDefinition.addPathDefinition("/*.css", "anon");
        chainDefinition.addPathDefinition("/*.png", "anon");
        chainDefinition.addPathDefinition("/*.js", "anon");
        chainDefinition.addPathDefinition("/v2/api-docs", "anon");
        chainDefinition.addPathDefinition("/webjars/**", "anon");
        chainDefinition.addPathDefinition("/swagger-resources", "anon");
        chainDefinition.addPathDefinition("/swagger-resources/**", "anon");
        chainDefinition.addPathDefinition("/*.jar", "anon"); //allow WebStart to pull the jars for the swing app
        chainDefinition.addPathDefinition("/remoting/**", "anon"); // protected using SecureRemoteInvocationExecutor
        chainDefinition.addPathDefinition("/**", "oauthc");
        if (shiroFilterChainDefinition != null) {
        	chainDefinition.addPathDefinitions(shiroFilterChainDefinition.getFilterChainMap());
        }
        factoryBean.setFilterChainDefinitionMap(chainDefinition.getFilterChainMap());
        return factoryBean;
    }
    
    /**
     * 下面的代码是添加注解支持
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        // https://zhuanlan.zhihu.com/p/29161098
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
    
    @Bean
    public Realm realm() {
    	OAuth2AuthorizingRealm realm = new OAuth2AuthorizingRealm();
        realm.setCachingEnabled(false);
        return realm;
    }
    
    @Bean
    public Oauth2UserCache oauth2UserCache() {
    	return new Oauth2UserCacheRedisImpl();
    }
    
    @Bean
    public Oauth2UserPermissionsCache oauth2UserPermissionsCache() {
    	return new Oauth2UserPermissionsCacheRedisImpl();
    }
    
    @Bean
    public AuthorizationService accessTokenAuthorizationService() {
    	return new AccessTokenAuthorizationServiceImpl();
    }
    
    @Bean
    public AuthenticationService authenticationService() {
    	return new AuthenticationServiceImpl();
    }
}
