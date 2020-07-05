package com.light.auth.service;

import com.light.auth.bean.AuthorizationBean;
import com.light.auth.bean.LoginUser;

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
	AuthorizationBean getAuthorizationInfo(LoginUser loginUser);
}
