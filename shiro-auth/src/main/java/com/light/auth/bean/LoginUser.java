package com.light.auth.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class LoginUser implements Serializable {
	private static final long serialVersionUID = 1L;
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
