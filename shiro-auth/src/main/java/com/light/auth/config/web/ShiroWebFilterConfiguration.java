package com.light.auth.config.web;

import com.light.auth.config.ShiroAuthProperties;
import com.light.auth.core.RestShiroFilterFactoryBean;
import com.light.auth.core.ShiroFilterRegistrationBean;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import java.util.List;
import java.util.Map;

/**
 * @since 1.4.0
 */
@Configuration
@EnableConfigurationProperties(ShiroAuthProperties.class)
@ConditionalOnProperty(name = "light.auth.enabled", matchIfMissing = true)
public class ShiroWebFilterConfiguration {

    @Value("${light.auth.unauthorizedUrl:/unauthorized}")
    protected String unauthorizedUrl;
    @Value("${light.auth.unauthenticatedUrl:/unauthenticated}")
    protected String loginUrl;
    @Value("${light.auth.successUrl:/}")
    protected String successUrl;
    @Autowired(required = false)
    protected List<ShiroFilterRegistrationBean> filterRegistrationBeanList;
    @Autowired
    protected SecurityManager securityManager;
    @Autowired
    protected ShiroAuthProperties shiroAuthProperties;

    @Bean
    @ConditionalOnMissingBean
    protected ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean filterFactoryBean = new RestShiroFilterFactoryBean();

        filterFactoryBean.setLoginUrl(loginUrl);
        filterFactoryBean.setSuccessUrl(successUrl);
        filterFactoryBean.setUnauthorizedUrl(unauthorizedUrl);

        filterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> filterChainMap = shiroFilterChainDefinition().getFilterChainMap();
        for (String path : shiroAuthProperties.getPathWhiteList()) {
            filterChainMap.put(path, "anon");
        }
        filterFactoryBean.setFilterChainDefinitionMap(filterChainMap);
        if (filterRegistrationBeanList != null) {
            filterRegistrationBeanList.forEach(registry -> registry.register(filterFactoryBean));
        }

        return filterFactoryBean;
    }

    @Bean(name = "shiroFilterRegistrationBean")
    @ConditionalOnMissingBean
    protected FilterRegistrationBean shiroFilterRegistrationBean() throws Exception {

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ERROR);
        filterRegistrationBean.setFilter((AbstractShiroFilter) shiroFilterFactoryBean().getObject());
        filterRegistrationBean.setOrder(1);

        return filterRegistrationBean;
    }

    @Bean
    @ConditionalOnMissingBean(value = ShiroFilterChainDefinition.class)
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {

        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition("/login", "anon");
        chainDefinition.addPathDefinition("/unauthenticated", "anon");
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
}
