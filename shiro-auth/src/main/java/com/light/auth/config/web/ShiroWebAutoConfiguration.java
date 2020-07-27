package com.light.auth.config.web;

import com.light.auth.config.handler.ShiroExceptionHandler;
import com.light.config.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.mgt.*;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.config.AbstractShiroWebConfiguration;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @since 1.4.0
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "light.auth.web.enabled", matchIfMissing = true)
public class ShiroWebAutoConfiguration extends AbstractShiroWebConfiguration {

    @PostConstruct
    protected void log() {
        log.info(Constants.CONFIG_LOG_MARK, "Shiro auth configuration enabled");
    }

    @Bean
    @ConditionalOnMissingBean
    @Override
    protected AuthenticationStrategy authenticationStrategy() {
        return super.authenticationStrategy();
    }

    @Bean
    @ConditionalOnMissingBean
    @Override
    protected Authenticator authenticator() {
        return super.authenticator();
    }

    @Bean
    @ConditionalOnMissingBean
    @Override
    protected Authorizer authorizer() {
        return super.authorizer();
    }

    @Bean
    @ConditionalOnMissingBean
    @Override
    protected SubjectDAO subjectDAO() {
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        subjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator());
        return subjectDAO;
    }

    @Bean
    @ConditionalOnMissingBean
    @Override
    protected SubjectFactory subjectFactory() {
        return super.subjectFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    @Override
    protected SessionStorageEvaluator sessionStorageEvaluator() {
        DefaultWebSessionStorageEvaluator evaluator = new DefaultWebSessionStorageEvaluator();
        evaluator.setSessionStorageEnabled(false);
        return evaluator;
    }

    @Bean
    @ConditionalOnMissingBean
    protected SessionsSecurityManager securityManager(List<Realm> realms) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setSubjectDAO(subjectDAO());
        securityManager.setRealms(realms);
        return securityManager;
    }

    @Bean
    protected ShiroExceptionHandler ShiroExceptionHandler() {
        return new ShiroExceptionHandler();
    }
}
