package com.daliu.sample.service;

import org.springframework.stereotype.Service;

import com.light.auth.bean.AuthorizationBean;
import com.light.auth.bean.LoginUser;
import com.light.auth.service.AuthorizationService;

/**
 * 授权服务实现
 * @author daliu
 *
 */
@Service
public class AuthorizationServiceImpl implements AuthorizationService {

	@Override
	public AuthorizationBean getAuthorizationInfo(LoginUser loginUser) {
		// TODO 查询用户的权限
		return null;
	}

}
