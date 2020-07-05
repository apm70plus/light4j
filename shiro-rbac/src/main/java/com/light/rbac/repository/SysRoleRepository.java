package com.light.rbac.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.light.rbac.model.SysRole;

/**
 * SysRoleRepository
 */
public interface SysRoleRepository extends CrudRepository<SysRole, Long>, QuerydslPredicateExecutor<SysRole> {

}
