package com.light.auth.service;

import com.light.auth.bean.LoginUser;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.MutablePrincipalCollection;

/**
 * 身份认证服务
 * @author liuyg
 *
 */
public interface AuthenticationService {

	/**
	 * 获取用户认证信息
	 * 
	 * @param authcToken
	 * @return
	 * @throws AuthenticationException
	 */
	LoginUser getAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException;

	/**
	 * 用户身份认证成功后处理
	 */
	default void onAuthenticationSuccess(MutablePrincipalCollection principals) {};

	/**
	 * 支持的Token类
	 * @return
	 */
	Class<? extends AuthenticationToken> supportTokenClass();
}
