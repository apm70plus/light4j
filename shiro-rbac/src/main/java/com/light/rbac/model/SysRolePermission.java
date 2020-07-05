package com.light.rbac.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.light.jpa.domain.AuditEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter@Setter
public class SysRolePermission extends AuditEntity {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	private SysRole role;
	@ManyToOne
	private SysPermission permission;
}
