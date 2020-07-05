package com.light.oauth2.service.impl;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import com.light.oauth2.dto.AccessTokenResponse;
import com.light.oauth2.service.TokenService;
import com.light.oauth2.type.GrantType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

	@Value("${oauth.client.id}")
	private String clientId;
	@Value("${oauth.client.secret}")
	private String secret;
	@Value("${oauth.authzserver.uri}")
	private String oauthzServerUri;
	
	private String accessToken;
	private String refreshToken;
	private long expired;
	
	@Override
	public String getAccessToken() {
		if (accessToken == null) {
			AccessTokenResponse token = applyAccessToken();
			this.accessToken = token.getAccess_token();
			this.expired = System.currentTimeMillis() + (token.getExpires_in() * 1000);
			this.refreshToken = token.getRefresh_token();
			return accessToken;
		} else if (expired - System.currentTimeMillis() < 10000L) {
			AccessTokenResponse token = refreshAccessToken();
			if (token != null) {// 刷新失败
				this.accessToken = token.getAccess_token();
				this.expired = System.currentTimeMillis() + (token.getExpires_in() * 1000);
			} else {
				AccessTokenResponse newToken = applyAccessToken();
				this.accessToken = newToken.getAccess_token();
				this.expired = System.currentTimeMillis() + (newToken.getExpires_in() * 1000);
				this.refreshToken = newToken.getRefresh_token();
			}
			return accessToken;
		} else {
			return accessToken;
		}
	}
	
	private AccessTokenResponse applyAccessToken() {
		RestTemplate template = new RestTemplate();
		String urlTemplate = "%s?grant_type=%s&client_id=%s&time=%s&signature=%s";
		String time = String.valueOf(System.currentTimeMillis());
		String url = String.format(urlTemplate, oauthzServerUri, GrantType.CLIENT_CREDENTIALS, clientId, time, createSignature(time));
		ResponseEntity<AccessTokenResponse> response = template.postForEntity(url, null, AccessTokenResponse.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return response.getBody();
		} else {
			throw new RuntimeException("获取AccessToken失败，错误码：" + response.getStatusCodeValue());
		}
	}
	
	private AccessTokenResponse refreshAccessToken() {
	    RestTemplate template = new RestTemplate();
		String urlTemplate = "%s?grant_type=%s&refresh_token=%s";
		String url = String.format(urlTemplate, GrantType.REFRESH_TOKEN, refreshToken);
		ResponseEntity<AccessTokenResponse> response = template.postForEntity(url, null, AccessTokenResponse.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return response.getBody();
		} else {
			log.warn("刷新AccessToken失败，错误码: " + response.getStatusCodeValue());
			return null;
		}
	}
	
	private String createSignature(String time) {
		byte[] bs = HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_256, secret.getBytes()).doFinal(time.getBytes());
		String signature = Base64Utils.encodeToString(bs);
		return signature;
	}
}
