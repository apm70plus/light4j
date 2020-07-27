package com.light.auth.config.session;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.*;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.SimpleSessionFactory;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.web.config.AbstractShiroWebConfiguration;
import org.apache.shiro.spring.web.config.ShiroWebConfiguration;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.util.List;

@AutoConfigureBefore(ShiroWebConfiguration.class)
@ConditionalOnProperty(name="light.auth.type", havingValue="session", matchIfMissing=false)
public class ShiroSessionAutoConfiguration extends AbstractShiroWebConfiguration {

    @Autowired
    private org.springframework.cache.CacheManager springCacheManager;

    @Bean
    @ConditionalOnMissingBean
    @Override
    public SubjectDAO subjectDAO() {
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        subjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator());
        return subjectDAO;
    }

    @Bean
    @ConditionalOnMissingBean
    @Override
    protected SessionStorageEvaluator sessionStorageEvaluator() {
        return new DefaultSessionStorageEvaluator();
    }

    @Bean
    @ConditionalOnMissingBean
    @Override
    protected SessionFactory sessionFactory() {
        return new SimpleSessionFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    @Override
    protected SessionDAO sessionDAO() {
        return new ShiroSessionDAO();
    }

    @Bean
    @ConditionalOnMissingBean
    @Override
    protected SessionManager sessionManager() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        sessionManager.setSessionDAO(sessionDAO());
        sessionManager.setSessionFactory(sessionFactory());
        sessionManager.setDeleteInvalidSessions(true);
        return sessionManager;
    }

    @Bean
    @ConditionalOnMissingBean
    @Override
    public SessionsSecurityManager securityManager(List<Realm> realms) {
        SessionsSecurityManager securityManager = createSecurityManager();
        securityManager.setAuthenticator(authenticator());
        securityManager.setAuthorizer(authorizer());
        securityManager.setRealms(realms);
        securityManager.setSessionManager(sessionManager());
        securityManager.setEventBus(eventBus);
        securityManager.setCacheManager(cacheManager());
        return securityManager;
    }

    @Bean
    @ConditionalOnMissingBean(name = "sessionCookieTemplate")
    protected Cookie sessionCookieTemplate() {
        return super.sessionCookieTemplate();
    }

    @Bean
    @ConditionalOnMissingBean
    protected RememberMeManager rememberMeManager() {
        return super.rememberMeManager();
    }

    @Bean
    @ConditionalOnMissingBean(name = "rememberMeCookieTemplate")
    protected Cookie rememberMeCookieTemplate() {
        return super.rememberMeCookieTemplate();
    }

    protected SessionsSecurityManager createSecurityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setSubjectDAO(subjectDAO());
        securityManager.setSubjectFactory(super.subjectFactory());
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    private CacheManager cacheManager() {
        return new ShiroCacheManager(springCacheManager);
    }
}
