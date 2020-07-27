package com.light.auth.core;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;

import javax.servlet.Filter;

public class ShiroFilterRegistrationBean {

    private Filter filter;
    private String name;

    public ShiroFilterRegistrationBean(Filter filter, String name) {
        this.filter = filter;
        this.name = name;
    }

    public void register(ShiroFilterFactoryBean shiroFilterFactoryBean) {
        shiroFilterFactoryBean.getFilters().put(name, filter);
    }
}
