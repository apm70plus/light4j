package com.light.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ConfigurationProperties(prefix = "light.auth")
public class ShiroAuthProperties {

	// 认证类型：session，accessToken，jwtToken
	private String type;
	// token超时时间（秒）
	public int tokenExpire;
	// JWT 配置
	private JwtProperties jwt = new JwtProperties();
	// redis 配置
	private RedisProperties redis = new RedisProperties();
	// 路径白名单
	private Set<String> pathWhiteList = new HashSet<>();
	
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
