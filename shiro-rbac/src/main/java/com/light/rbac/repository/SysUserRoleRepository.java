package com.light.rbac.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.light.rbac.model.SysRole;
import com.light.rbac.model.SysUserRole;

/**
 * SysUserRoleRepository
 */
public interface SysUserRoleRepository extends CrudRepository<SysUserRole, Long>, QuerydslPredicateExecutor<SysUserRole> {

	Iterable<SysUserRole> findAllByUserId(Long id);

	Iterable<SysUserRole> findAllByRole(SysRole role);

}
