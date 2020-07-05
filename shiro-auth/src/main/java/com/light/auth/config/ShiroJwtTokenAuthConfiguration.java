package com.light.auth.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.light.auth.core.RestShiroFilterFactoryBean;
import com.light.auth.filter.JwtAuthenticationFilter;
import com.light.auth.service.impl.JwtTokenAuthenticationService;
import com.light.auth.token.JwtTokenService;
import com.light.auth.token.impl.JwtTokenServiceImpl;

@Configuration
@ConditionalOnProperty(name="framework.auth.type", havingValue="jwtToken", matchIfMissing=false)
public class ShiroJwtTokenAuthConfiguration {

	@Value("${shiro.loginUrl:/unauthorized}")
	private String unauthorizedUrl;
	
    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(Realm realm) {
    	DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        // 使用自己的realm
        manager.setRealm(realm);
        // 关闭shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        manager.setSubjectDAO(subjectDAO);
        return manager;
    }
    
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(
    		DefaultWebSecurityManager securityManager,
    		ShiroFilterChainDefinition shiroFilterChainDefinition) {
        ShiroFilterFactoryBean factoryBean = new RestShiroFilterFactoryBean();

        // 添加自己的过滤器并且取名为jwtauthc
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("authc", new JwtAuthenticationFilter());
        factoryBean.setFilters(filterMap);
        factoryBean.setSecurityManager(securityManager);
        //factoryBean.setUnauthorizedUrl(unauthorizedUrl);
        factoryBean.setLoginUrl(unauthorizedUrl);
        //自定义url规则 http://shiro.apache.org/web.html#urls-
        factoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition.getFilterChainMap());
        return factoryBean;
    }
    
    @Bean
    public JwtTokenAuthenticationService jwtTokenAuthenticationService() {
    	return new JwtTokenAuthenticationService();
    }
    
    @Bean
    public JwtTokenService jwtTokenService(ShiroAuthProperties properties) {
    	return new JwtTokenServiceImpl(properties.getJwt().secret, properties.tokenExpire);
    }
}
