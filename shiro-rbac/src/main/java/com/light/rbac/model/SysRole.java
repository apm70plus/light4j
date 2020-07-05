package com.light.rbac.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

import com.light.jpa.domain.AuditEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter@Setter
public class SysRole extends AuditEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 角色名
	 */
	@NotBlank
	@Column(nullable = false, length = 100, unique = true)
	private String name;

	/**
	 * 角色编码
	 */
	@NotBlank
	@Column(nullable = false, length = 20, unique = true)
	private String code;
}
