package com.light.oauth2.service;

import com.light.oauth2.bean.TokenBean;
import com.light.oauth2.model.AuthorizationCode;

public interface Oauth2Service {

	/**
	 * 创建授权码
	 *
	 * @param userId
	 * @param clientId
	 */
	AuthorizationCode createAuthCode(String userId, String clientId);

	/**
	 * 校验授权码
	 * 
	 * @param authCode
	 * @return
	 */
	AuthorizationCode getAuthCode(String authCode, String clientId);
	
	/**
	 * 创建访问令牌
	 * 
	 * @param userId
	 * @param scope
	 */
	TokenBean createAccessToken(String userId, String clientId, String scope);
	
	/**
	 * 刷新访问令牌
	 * 
	 * @param userId
	 * @param scope
	 */
	TokenBean refreshAccessToken(String refreshToken);
	
	/**
	 * 创建刷新令牌
	 * 
	 * @param userId
	 * @param scope
	 */
	TokenBean createRefreshToken(String userId, String clientId, String scope);
}
