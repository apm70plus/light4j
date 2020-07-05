package com.light.auth.core;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestPathMatchingFilterChainResolver extends PathMatchingFilterChainResolver {

	private static transient final Logger log = LoggerFactory.getLogger(RestPathMatchingFilterChainResolver.class);
	
	public FilterChain getChain(ServletRequest request, ServletResponse response, FilterChain originalChain) {
        FilterChainManager filterChainManager = getFilterChainManager();
        if (!filterChainManager.hasChains()) {
            return null;
        }

        String requestURI = getPathWithinApplication(request);

        //the 'chain names' in this implementation are actually path patterns defined by the user.  We just use them
        //as the chain name for the FilterChainManager's requirements
        for (String pathPattern : filterChainManager.getChainNames()) {
        	String pattern = pathPattern;
        	if (pathPattern.charAt(0) != '/') {
        		int index = pathPattern.indexOf("/", 3);
        		String method = pathPattern.substring(0, index).toUpperCase();
        		if (!WebUtils.toHttp(request).getMethod().toUpperCase().equals(method)) {
        			continue;
        		}
        		pattern = pathPattern.substring(index);
        	}
        	
            // If the path does match, then pass on to the subclass implementation for specific checks:
            if (pathMatches(pattern, requestURI)) {
                if (log.isTraceEnabled()) {
                    log.trace("Matched path pattern [" + pathPattern + "] for requestURI [" + requestURI + "].  " +
                            "Utilizing corresponding filter chain...");
                }
                return filterChainManager.proxy(originalChain, pathPattern);
            }
        }

        return null;
    }
}
