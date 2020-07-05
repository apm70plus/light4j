package com.light.oauth2.authc;

import org.apache.shiro.SecurityUtils;

import com.light.oauth2.bean.LoginUser;

public final class LoginUserUtils {

	public static LoginUser getLoginUser() {
		return (LoginUser)SecurityUtils.getSubject().getPrincipals().oneByType(LoginUser.class);
	}
	
	public static String getLoginUserId() {
		return getLoginUser().getId();
	}
	
	public static String getAccessToken() {
		return SecurityUtils.getSubject().getPrincipals().oneByType(String.class);
	}
}
