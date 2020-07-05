package com.light.rbac.service;

import java.util.List;

import com.light.rbac.model.SysRole;
import com.light.rbac.model.SysUserRole;

/**
 * SysUserRoleService
 */
public interface SysUserRoleService {

    /**
     * 更新用户角色
     *
     * @param userId 用户ID
     * @param roles 角色ID列表
     * @return 
     */
    Iterable<SysUserRole> updateUserRoles(Long userId, List<Long> roles);
    
    /**
     * 收回用户已分配的角色
     * @param role 角色
     */
    void revokeUsersRole(SysRole role);
}
