package com.light.auth.config.web;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.config.AbstractShiroAnnotationProcessorConfiguration;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @since 1.4.0
 */
@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
@ConditionalOnProperty(name = "light.auth.annotations.enabled", matchIfMissing = true)
public class ShiroAnnotationProcessorAutoConfiguration extends AbstractShiroAnnotationProcessorConfiguration {

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    @ConditionalOnMissingBean
    @Override
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = super.defaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    @ConditionalOnMissingBean
    @Override
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        return super.authorizationAttributeSourceAdvisor(securityManager);
    }
}
