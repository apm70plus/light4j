package com.light.rbac.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.light.rbac.model.SysUser;

/**
 * SysUserRepository
 */
public interface SysUserRepository extends CrudRepository<SysUser, Long>, QuerydslPredicateExecutor<SysUser> {

}