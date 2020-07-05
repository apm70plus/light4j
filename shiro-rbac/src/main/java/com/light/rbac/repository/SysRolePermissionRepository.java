package com.light.rbac.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.light.rbac.model.SysPermission;
import com.light.rbac.model.SysRole;
import com.light.rbac.model.SysRolePermission;

/**
 * SysRolePermissionRepository
 */
public interface SysRolePermissionRepository extends CrudRepository<SysRolePermission, Long>, QuerydslPredicateExecutor<SysRolePermission> {

	Iterable<SysRolePermission> findAllByRole(SysRole role);
	
	Iterable<SysRolePermission> findAllByPermission(SysPermission permission);
}
