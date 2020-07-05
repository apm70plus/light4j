package com.light.oauth2.dto;

import lombok.Data;

@Data
public class AccessTokenResponse {

	private String access_token;
	
	private String token_type;
	
	private int expires_in;
	
	private String refresh_token;
}
