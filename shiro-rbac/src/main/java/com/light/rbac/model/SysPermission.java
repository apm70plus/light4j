package com.light.rbac.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

import com.light.jpa.domain.AuditEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter@Setter
public class SysPermission extends AuditEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 权限名称
	 */
	private String name = "";
	
	/**
	 * 权限的代码/通配符,对应代码中@RequiresPermissions 的value
	 */
	@NotBlank
	private String code;
	
	/**
	 * 归属菜单，前端判断展示菜单、按钮用
	 */
	private String menuCode = "";
	/**
	 * 菜单的中文释义
	 */
	private String menuName = "";
	/**
	 * 是否菜单必选权限（通常列表权限是必选）
	 */
	private boolean required = false;
}
