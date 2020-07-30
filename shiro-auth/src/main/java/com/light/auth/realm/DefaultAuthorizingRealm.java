package com.light.auth.realm;

import com.light.auth.bean.AuthorizationBean;
import com.light.auth.bean.LoginUser;
import com.light.auth.bean.UsernamePasswordToken;
import com.light.auth.service.AuthenticationService;
import com.light.auth.service.AuthorizationService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.MutablePrincipalCollection;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 认证的默认实现类
 */
public class DefaultAuthorizingRealm extends AuthorizingRealm {

	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private AuthorizationService authorizationService;

	private Object lock = new Object();
	private Map<Class<? extends AuthenticationToken>, AuthenticationService> authenticationServices;

	/**
	 * 获取授权信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// 查询用户的权限
		LoginUser loginUser = principals.oneByType(LoginUser.class);
		AuthorizationBean authorizations = authorizationService.getAuthorizationInfo(loginUser);
		// 为当前用户设置角色和权限
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		authorizationInfo.addStringPermissions(authorizations.getPermissions());
		authorizationInfo.addRoles(authorizations.getRoles());
		return authorizationInfo;
	}

	/**
	 * 获取认证信息
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {
		LoginUser user = getAuthenticationService(authcToken).getAuthenticationInfo(authcToken);
		if (user != null) {
			SimplePrincipalCollection principal = new SimplePrincipalCollection();
			principal.add(user, "LoginUser");
			principal.add(authcToken.getPrincipal(), "Principal");
			if (authcToken instanceof UsernamePasswordToken) {
				applySupportedPrincipals(principal);
			}
			return new SimpleAuthenticationInfo(principal, authcToken.getCredentials());
		} else {
			throw new AuthenticationException();
		}
	}
	
	@Override
	public boolean supports(AuthenticationToken authcToken) {
		return getAuthenticationServices().keySet().stream().anyMatch(c -> c.isAssignableFrom(authcToken.getClass()));
	}

	private void applySupportedPrincipals(MutablePrincipalCollection principalCollection) {
		getAuthenticationServices().values().forEach(s -> s.onAuthenticationSuccess(principalCollection));
	}

	private AuthenticationService getAuthenticationService(AuthenticationToken authcToken) {

		Class<? extends AuthenticationToken> clazz = authcToken.getClass();
		AuthenticationService service = getAuthenticationServices().get(clazz);
		if (service == null) {
			Optional<Class<? extends AuthenticationToken>> matchedClass = getAuthenticationServices().keySet().stream()
					.filter(c -> c.isAssignableFrom(clazz)).findAny();
			Class<? extends AuthenticationToken> findClass = matchedClass.orElseThrow(
					() -> new RuntimeException("unsupported AuthenticationToken class: " + clazz.getName()));
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
				Map<String, AuthenticationService> services = applicationContext
						.getBeansOfType(AuthenticationService.class);
				services.values().forEach(s -> {
					authenticationServices.put(s.supportTokenClass(), s);
				});
			}
		}
		return authenticationServices;
	}
}
