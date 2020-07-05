package com.light.oauth2.service;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;

import com.light.oauth2.bean.LoginUser;

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
	 * 支持的Token类
	 * @return
	 */
	Class<? extends AuthenticationToken> supportTokenClass();
}
