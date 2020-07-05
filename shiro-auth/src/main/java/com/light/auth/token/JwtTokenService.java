package com.light.auth.token;

import com.light.auth.bean.LoginUser;

import lombok.Data;

public interface JwtTokenService {

	/**
	 * 为用户分配 access_token
	 * @param jwtToken
	 * @param user
	 * @return
	 */
	JwtToken allocateToken(LoginUser user);
	
	/**
	 * 根据 access_token 获取用户信息
	 * @param jwtToken
	 * @return
	 */
	LoginUser getUserByToken(String jwtToken);
	
	/**
	 * 刷新access_token
	 * @param jwtToken
	 * @return
	 */
	JwtToken refreshToken(String jwtToken);
	
	@Data
	public static class JwtToken {
		private String token;
		/** 超时时间，单位（秒） */
		private int expires;
	}
}
