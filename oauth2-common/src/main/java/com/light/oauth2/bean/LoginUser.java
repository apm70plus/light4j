package com.light.oauth2.bean;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class LoginUser {
	/**
	 * 用户ID
	 */
	private String id;
	/**
	 * 用户名
	 */
	private String name;
	/**
	 * 是否管理员
	 */
	private boolean admin;
	/**
	 * 扩展信息
	 */
	private Map<String, Object> extension = new HashMap<>();
}
