package com.light.auth.config.web;

import com.light.auth.realm.DefaultAuthorizingRealm;
import org.apache.shiro.event.EventBus;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.ShiroEventBusBeanPostProcessor;
import org.apache.shiro.spring.config.AbstractShiroBeanConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @since 1.4.0
 */
@Configuration
@ConditionalOnProperty(name = "light.auth.enabled", matchIfMissing = true)
public class ShiroBeanAutoConfiguration extends AbstractShiroBeanConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @Override
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return super.lifecycleBeanPostProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    @Override
    protected EventBus eventBus() {
        return super.eventBus();
    }

    @Bean
    @ConditionalOnMissingBean
    @Override
    public ShiroEventBusBeanPostProcessor shiroEventBusAwareBeanPostProcessor() {
        return super.shiroEventBusAwareBeanPostProcessor();
    }

    @Bean
    public Realm realm() {
        DefaultAuthorizingRealm realm = new DefaultAuthorizingRealm();
        return realm;
    }
}
