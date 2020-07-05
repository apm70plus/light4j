package com.light.auth.token.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.light.auth.bean.LoginUser;
import com.light.auth.token.JwtTokenService;
import com.light.util.JsonUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtTokenServiceImpl implements JwtTokenService {

	private String secret;
	private int expirSeconds;

	public JwtTokenServiceImpl(String secret, int expirSeconds) {
		this.secret = secret;
		this.expirSeconds = expirSeconds;
	}

	@Override
	public JwtToken allocateToken(LoginUser user) {
		String JWT = Jwts.builder()
				// 保存权限（角色）
				.claim("userInfo", JsonUtils.pojoToJson(user))
				// 用户名写入标题
				.setSubject(user.getName())
				// 有效期设置
				.setExpiration(new Date(System.currentTimeMillis() + (expirSeconds * 1000)))
				// 签名设置
				.signWith(SignatureAlgorithm.HS512, secret).compact();
		JwtToken token = new JwtToken();
		token.setToken(JWT);
		token.setExpires(expirSeconds);
		return token;
	}

	@Override
	public LoginUser getUserByToken(String jwtToken) {
		if (StringUtils.isBlank(jwtToken)) {
			return null;
		}
		// 解析 Token
		Claims claims = Jwts.parser()
				// 验签
				.setSigningKey(secret)
				// 去掉 Bearer
				.parseClaimsJws(jwtToken).getBody();

		// 拿用户信息
		String userJson = (String) claims.get("userInfo");
		LoginUser user = JsonUtils.jsonToPojo(userJson, LoginUser.class);
		return user;
	}

	@Override
	public JwtToken refreshToken(String jwtToken) {
		LoginUser user = getUserByToken(jwtToken);
		return allocateToken(user);
	}

}
