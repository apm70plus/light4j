package com.light.oauth2.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class TokenBean {

	@JsonIgnore
	private String token;
	
	private String userId;
	
	private String scope;
	
	private String client;
	
	private int timeout;
}
