package com.light.oauth2.service;

import java.util.Collection;

import com.light.oauth2.bean.LoginUser;

/**
 * 授权服务
 * @author liuyg
 *
 */
public interface AuthorizationService {

	/**
	 * 获取用户权限
	 * 
	 * @param principals
	 * @return
	 */
	Collection<String> getAuthorizationInfo(LoginUser loginUser);
}
