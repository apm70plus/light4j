package com.light.auth.config.token;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.light.auth.config.ShiroAuthProperties;
import com.light.auth.core.ShiroFilterRegistrationBean;
import com.light.auth.filter.OAuth2AuthenticationFilter;
import com.light.auth.service.impl.AccessTokenAuthenticationService;
import com.light.auth.token.AccessTokenService;
import com.light.auth.token.TokenRepository;
import com.light.auth.token.impl.AccessTokenServiceImpl;
import com.light.auth.token.impl.HazelcastTokenRepositoryImpl;
import com.light.auth.token.impl.RedisTokenRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@EnableConfigurationProperties(ShiroAuthProperties.class)
@ConditionalOnProperty(name="light.auth.type", havingValue="accessToken", matchIfMissing=false)
public class ShiroAccessTokenAutoConfiguration {

    @Autowired
    private ShiroAuthProperties properties;

    @Bean
    @ConditionalOnClass(StringRedisTemplate.class)
    @ConditionalOnProperty(name="light.auth.store", havingValue="redis", matchIfMissing=false)
    public TokenRepository redisTokenRepository(StringRedisTemplate stringRedisTemplate) {
        RedisTokenRepositoryImpl tokenRepository = new RedisTokenRepositoryImpl(stringRedisTemplate, properties.getTokenExpire());
        return tokenRepository;
    }

    @Bean
    @ConditionalOnProperty(name="light.auth.store", havingValue="hazelcast", matchIfMissing=false)
    public TokenRepository hazelcastTokenRepository(HazelcastInstance hazelcastInstance) {
        hazelcastInstance.getConfig().getMapConfig("shiro:accessTokens");
        IMap<String, String> map = hazelcastInstance.getMap("shiro:accessTokens");
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

    @Bean
    public ShiroFilterRegistrationBean oauth2AuthenticationFilter() {
        return new ShiroFilterRegistrationBean(new OAuth2AuthenticationFilter(), "authc");
    }
}
