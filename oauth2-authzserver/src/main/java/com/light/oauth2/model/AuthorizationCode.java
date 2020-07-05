package com.light.oauth2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * 授权码
 * 
 * @author liuyg
 */
@Getter
@Setter
public class AuthorizationCode {
	/**
	 * 授权码
	 */
	@JsonIgnore
	private String code;
	
	/**
	 * 客户端ID
	 */
	@JsonProperty(value="ci")
	private String clientId;
	
	/**
	 * 用戶ID
	 */
	@JsonProperty(value="ui")
	private String userId;
}
