package com.light.auth.token;

import com.light.auth.bean.LoginUser;

import lombok.Data;

public interface AccessTokenService {

	/**
	 * 生成新的 access_token
	 * @return
	 */
	AccessToken newToken();
	
	/**
	 * 为用户分配 access_token
	 * @param accessToken
	 * @param user
	 * @return
	 */
	AccessToken allocateToken(LoginUser user);
	
	/**
	 * 根据 access_token 获取用户信息
	 * @param accessToken
	 * @return
	 */
	LoginUser getUserByToken(String accessToken);
	
	/**
	 * 将用户的access_token置为无效
	 * @param user
	 */
	void invalidUserToken(LoginUser user);
	
	/**
	 * 刷新access_token
	 * @param accessToken
	 * @return
	 */
	AccessToken refreshToken(String accessToken);
	
	@Data
	public static class AccessToken {
		private String token;
		/** 超时时间，单位（秒） */
		private int expires;
	}
}
