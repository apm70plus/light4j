package com.light.auth.service.impl;

import com.light.auth.authc.SessionToken;
import com.light.auth.bean.LoginUser;
import com.light.auth.service.AuthenticationService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.MutablePrincipalCollection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import java.net.MalformedURLException;
import java.net.URL;

public class SessionAuthenticationService implements AuthenticationService {

	@Value("${light.sessionManager.cookie.name:JSESSIONID}")
	protected String sessionIdCookieName;

	@Value("${light.sessionManager.cookie.maxAge:-1}")
	protected int sessionIdCookieMaxAge;

	@Value("${light.sessionManager.cookie.domain:null}")
	protected String sessionIdCookieDomain;

	@Value("${light.sessionManager.cookie.path:/}")
	protected String sessionIdCookiePath;

	@Value("#{ @environment['light.sessionManager.cookie.secure'] ?: false }")
	protected boolean sessionIdCookieSecure;

	@Override
	public LoginUser getAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		SessionToken token = (SessionToken) authcToken;
		LoginUser user = token.getLoginUserFromSession();
		if (user == null) {
			throw new AuthenticationException("未认证");
		}
		return user;
	}

	@Override
	public void onAuthenticationSuccess(MutablePrincipalCollection principals) {
		RequestAttributes request = RequestContextHolder.getRequestAttributes();
		request.setAttribute(SessionToken.PRINCIPAL_KEY, principals.getPrimaryPrincipal(), RequestAttributes.SCOPE_SESSION);
		writeCookies((ServletRequestAttributes) request);
	}

	@Override
	public Class<? extends AuthenticationToken> supportTokenClass() {
		return SessionToken.class;
	}

	private void writeCookies(ServletRequestAttributes request) {
		if (request.getResponse() == null) {
			return;
		}
		Cookie cookie = new Cookie("JSESSIONID", request.getSessionId());
		try {
			String domain = new URL(request.getRequest().getRequestURL().toString()).getHost();
			cookie.setDomain(domain);
		} catch (MalformedURLException e) {
		}
		cookie.setHttpOnly(true);
		cookie.setMaxAge(sessionIdCookieMaxAge);
		cookie.setPath("/");
		cookie.setSecure(sessionIdCookieSecure);
		request.getResponse().addCookie(cookie);
	}
}