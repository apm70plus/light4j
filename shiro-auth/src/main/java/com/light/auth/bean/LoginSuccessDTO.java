package com.light.auth.bean;

import lombok.Data;

@Data
public class LoginSuccessDTO {
	/**
	 * 用户ID
	 */
	private String userId;
	/**
	 * 用户名
	 */
	private String username;
}
