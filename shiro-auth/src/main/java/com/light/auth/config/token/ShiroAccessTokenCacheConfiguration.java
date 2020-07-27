package com.light.auth.config.token;

import com.hazelcast.config.MapConfig;
import com.light.auth.config.ShiroAuthProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ShiroAuthProperties.class)
@ConditionalOnProperty(name="light.auth.type", havingValue="accessToken", matchIfMissing=false)
public class ShiroAccessTokenCacheConfiguration {

    @Autowired
    private ShiroAuthProperties shiroAuthProperties;

    @Bean
    public MapConfig shiroAccessTokenCacheConfig() {
        MapConfig config = new MapConfig("shiro:accessTokens");
        config.setTimeToLiveSeconds(shiroAuthProperties.tokenExpire);
        config.setMaxIdleSeconds(shiroAuthProperties.tokenExpire);
        return config;
    }

    @Bean
    public MapConfig shiroRefreshTokenCacheConfig() {
        MapConfig config = new MapConfig("shiro:refreshTokens");
        config.setTimeToLiveSeconds(3600 * 24 * 30);
        config.setMaxIdleSeconds(3600 * 24 * 30);
        return config;
    }
}
