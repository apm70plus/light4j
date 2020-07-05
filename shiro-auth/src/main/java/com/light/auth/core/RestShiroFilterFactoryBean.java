package com.light.auth.core;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;

public class RestShiroFilterFactoryBean extends ShiroFilterFactoryBean {

	public RestShiroFilterFactoryBean() {
        super();
    }
	
	@Override
    protected AbstractShiroFilter createInstance() throws Exception {
		AbstractShiroFilter filter = super.createInstance();
		PathMatchingFilterChainResolver oldResolver = (PathMatchingFilterChainResolver)filter.getFilterChainResolver();
		RestPathMatchingFilterChainResolver resolver = new RestPathMatchingFilterChainResolver();
		resolver.setFilterChainManager(oldResolver.getFilterChainManager());
		filter.setFilterChainResolver(resolver);
		return filter;
    }
}
