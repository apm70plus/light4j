package com.light.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "framework.auth")
public class ShiroAuthProperties {

	// 认证类型：session，accessToken，jwtToken
	private String type;
	// token超时时间（秒）
	public int tokenExpire;
	// JWT 配置
	private JwtProperties jwt = new JwtProperties();
	// redis 配置
	private RedisProperties redis = new RedisProperties();
	
	public static class RedisProperties {
		@Getter@Setter
		public String host;
		@Getter@Setter
		public String pass;
	}
	
	public static class JwtProperties {
		// JWT密钥
		@Getter@Setter
		public String secret;
	}
}
