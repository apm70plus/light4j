package com.light.oauth2.realm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.light.oauth2.bean.LoginUser;
import com.light.oauth2.service.AuthenticationService;
import com.light.oauth2.service.AuthorizationService;

/**
 * OAuth2的默认实现类
 */
public class OAuth2AuthorizingRealm extends AuthorizingRealm {
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private AuthorizationService suthorizationService;
	
	private Object lock = new Object();
	private Map<Class<? extends AuthenticationToken>, AuthenticationService> authenticationServices;

	/**
	 * 获取授权信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//查询用户的权限
		LoginUser loginUser = principals.oneByType(LoginUser.class);
		Collection<String> permissions = suthorizationService.getAuthorizationInfo(loginUser);
		//为当前用户设置角色和权限
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		authorizationInfo.addStringPermissions(permissions);
		return authorizationInfo;
	}

	/**
	 * 获取认证信息
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		LoginUser user = getAuthenticationService(authcToken).getAuthenticationInfo(authcToken);
		if (user != null) {
			SimplePrincipalCollection principal = new SimplePrincipalCollection();
			principal.add(user, "OAuth2Realm");
			principal.add(authcToken.getPrincipal(), "AccessToken");
			return new SimpleAuthenticationInfo(principal, authcToken.getCredentials());
		} else {
			throw new AuthenticationException();
		}
	}
	
	@Override
	public boolean supports(AuthenticationToken authcToken) {
		return getAuthenticationServices().keySet().stream().anyMatch(c -> c.isAssignableFrom(authcToken.getClass()));
	}
	
	private AuthenticationService getAuthenticationService(AuthenticationToken authcToken) {
		
		Class<? extends AuthenticationToken> clazz = authcToken.getClass();
		AuthenticationService service = getAuthenticationServices().get(clazz);
		if (service == null) {
			Optional<Class <? extends AuthenticationToken>> matchedClass = getAuthenticationServices().keySet().stream().filter(c -> c.isAssignableFrom(clazz)).findAny();
			Class <? extends AuthenticationToken> findClass = matchedClass.orElseThrow(() -> new RuntimeException("unsupported AuthenticationToken class: " + clazz.getName()));
			service = getAuthenticationServices().get(findClass);
			getAuthenticationServices().put(clazz, service);
		}
		return service;
	}
	
	private Map<Class<? extends AuthenticationToken>, AuthenticationService> getAuthenticationServices() {
		if (authenticationServices == null) {
			synchronized (lock) {
				if (authenticationServices != null) {
					return authenticationServices;
				}
				authenticationServices = new HashMap<>();
				Map<String, AuthenticationService> services = applicationContext.getBeansOfType(AuthenticationService.class);
				services.values().forEach(s -> {
					authenticationServices.put(s.supportTokenClass(), s);
				});
			}
		}
		return authenticationServices;
	}
}
