package com.light.rbac.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.light.rbac.model.SysPermission;

/**
 * SysPermissionRepository
 */
public interface SysPermissionRepository extends CrudRepository<SysPermission, Long>, QuerydslPredicateExecutor<SysPermission> {

}
