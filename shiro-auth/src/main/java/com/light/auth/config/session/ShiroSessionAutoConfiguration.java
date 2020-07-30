package com.light.auth.config.session;

import com.light.auth.core.ShiroFilterRegistrationBean;
import com.light.auth.filter.SessionAuthenticationFilter;
import com.light.auth.service.impl.SessionAuthenticationService;
import org.apache.shiro.spring.web.config.ShiroWebConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfigureBefore(ShiroWebConfiguration.class)
@ConditionalOnProperty(name="light.auth.type", havingValue="session", matchIfMissing=false)
public class ShiroSessionAutoConfiguration {
    @Bean
    public SessionAuthenticationService sessionAuthenticationService() {
        return new SessionAuthenticationService();
    }

    @Bean
    public ShiroFilterRegistrationBean oauth2AuthenticationFilter() {
        return new ShiroFilterRegistrationBean(new SessionAuthenticationFilter(), "authc");
    }
}
