package com.light.auth.config;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.util.StringUtils;

import com.light.auth.realm.DefaultAuthorizingRealm;
import com.light.config.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ComponentScan(basePackages = "com.light.auth")
public class ShiroAuthAutoConfiguration {
	@Autowired
	private ShiroAuthProperties properties;
	
	@Bean
	public Realm realm() {
		log.info(Constants.CONFIG_LOG_MARK, "Shiro auth configuration enabled");
		DefaultAuthorizingRealm realm = new DefaultAuthorizingRealm();
		return realm;
	}

	@Bean
	@ConditionalOnProperty(name = "framework.auth.redis.cacheEnable", havingValue = "true", matchIfMissing = false)
	public RedisCacheManager cacheManager() {
		RedisCacheManager redisCacheManager = new RedisCacheManager();
		redisCacheManager.setRedisManager(redisManager());
		return redisCacheManager;
	}

	@Bean
	@ConditionalOnProperty(name = "framework.auth.redis")
	public RedisManager redisManager() {
		RedisManager redisManager = new RedisManager();
		redisManager.setHost(properties.getRedis().host);
		if (!StringUtils.isEmpty(properties.getRedis().pass)) {
			redisManager.setPassword(properties.getRedis().pass);
		}
		redisManager.setTimeout(10000);
		return redisManager;
	}

	@Bean
	@ConditionalOnMissingBean(value = ShiroFilterChainDefinition.class)
	public ShiroFilterChainDefinition defaultFilterChainDefinition() {
		DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
		chainDefinition.addPathDefinition("/login", "anon");
		chainDefinition.addPathDefinition("/logout", "anon");
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
		chainDefinition.addPathDefinition("/*.jar", "anon"); // allow WebStart to pull the jars for the swing app
		chainDefinition.addPathDefinition("/remoting/**", "anon"); // protected using SecureRemoteInvocationExecutor
		chainDefinition.addPathDefinition("/**", "authc");
		return chainDefinition;
	}
	
    /**
     * 下面的代码是添加注解支持
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
}
