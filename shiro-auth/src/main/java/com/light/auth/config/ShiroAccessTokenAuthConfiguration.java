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
import org.crazycake.shiro.RedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.light.auth.core.RestShiroFilterFactoryBean;
import com.light.auth.filter.OAuth2AuthenticationFilter;
import com.light.auth.service.impl.AccessTokenAuthenticationService;
import com.light.auth.token.AccessTokenService;
import com.light.auth.token.TokenRepository;
import com.light.auth.token.impl.AccessTokenServiceImpl;
import com.light.auth.token.impl.HazelcastTokenRepositoryImpl;
import com.light.auth.token.impl.RedisTokenRepositoryImpl;

@Configuration
@ConditionalOnProperty(name="framework.auth.type", havingValue="accessToken", matchIfMissing=false)
public class ShiroAccessTokenAuthConfiguration {

	@Value("${shiro.loginUrl:/unauthorized}")
	private String unauthorizedUrl;
	@Autowired
	private ShiroAuthProperties properties;
	
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
        filterMap.put("authc", new OAuth2AuthenticationFilter());
        factoryBean.setFilters(filterMap);
        factoryBean.setSecurityManager(securityManager);
        //factoryBean.setUnauthorizedUrl(unauthorizedUrl);
        factoryBean.setLoginUrl(unauthorizedUrl);
        //自定义url规则 http://shiro.apache.org/web.html#urls-
        factoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition.getFilterChainMap());
        return factoryBean;
    }
    
    @Bean
    @ConditionalOnBean(value = RedisManager.class)
	public TokenRepository redisTokenRepository(RedisManager redisManager) {
    	RedisTokenRepositoryImpl tokenRepository = new RedisTokenRepositoryImpl(redisManager, properties.getTokenExpire());
		return tokenRepository;
	}
    
    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(value = RedisManager.class)
	public HazelcastInstance hazelcastInstance() {
		Config config = new Config();
		config.getMapConfig("accessTokens").setTimeToLiveSeconds(properties.getTokenExpire());
		return Hazelcast.newHazelcastInstance(config);                  
	}
    
    @Bean
    @ConditionalOnMissingBean(value = RedisManager.class)
	public TokenRepository hazelcastTokenRepository() {
    	HazelcastInstance instance = hazelcastInstance();
    	IMap<String, String> map = instance.getMap("accessTokens");
    	HazelcastTokenRepositoryImpl tokenRepository = new HazelcastTokenRepositoryImpl(map);
		return tokenRepository;
	}
    
    @Bean
    public AccessTokenService accessTokenService(TokenRepository tokenRepository) {
    	AccessTokenServiceImpl accessTokenService = new AccessTokenServiceImpl(tokenRepository, properties.getTokenExpire());
    	return accessTokenService;
    }
    
    @Bean
    public AccessTokenAuthenticationService accessTokenAuthenticationService() {
    	return new AccessTokenAuthenticationService();
    }
}
