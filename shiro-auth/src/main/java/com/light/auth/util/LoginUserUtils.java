package com.light.auth.util;

import java.util.Optional;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;

import com.light.auth.bean.LoginUser;

public final class LoginUserUtils {

	public static LoginUser getLoginUser() {
		return (LoginUser)SecurityUtils.getSubject().getPrincipal();
	}
	
	public static String getLoginUserId() {
		return Optional.ofNullable(getLoginUser()).map(u -> u.getId()).orElse(null);
	}
	
	public static void checkSelf(Object userId) {
		String loginUserId = getLoginUserId();
		if (loginUserId == null) {
			throw new AuthenticationException("请先登录");
		}
		if (!loginUserId.equals(String.valueOf(userId))) {
			throw new AuthorizationException("无权访问他人的数据");
		}
	}
}
