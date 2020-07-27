package com.light.auth.config.jwt;

import com.light.auth.config.ShiroAuthProperties;
import com.light.auth.core.ShiroFilterRegistrationBean;
import com.light.auth.filter.JwtAuthenticationFilter;
import com.light.auth.service.impl.JwtTokenAuthenticationService;
import com.light.auth.token.JwtTokenService;
import com.light.auth.token.impl.JwtTokenServiceImpl;
import org.apache.shiro.spring.web.config.ShiroWebConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfigureBefore(ShiroWebConfiguration.class)
@ConditionalOnProperty(name="light.auth.type", havingValue="jwtToken", matchIfMissing=false)
public class ShiroJwtTokenAutoConfiguration {

    @Bean
    public JwtTokenAuthenticationService jwtTokenAuthenticationService() {
        return new JwtTokenAuthenticationService();
    }

    @Bean
    public JwtTokenService jwtTokenService(ShiroAuthProperties properties) {
        return new JwtTokenServiceImpl(properties.getJwt().secret, properties.tokenExpire);
    }

    @Bean
    public ShiroFilterRegistrationBean jwtAuthenticationFilterRegistration() {
        return new ShiroFilterRegistrationBean(new JwtAuthenticationFilter(), "authc");
    }
}
