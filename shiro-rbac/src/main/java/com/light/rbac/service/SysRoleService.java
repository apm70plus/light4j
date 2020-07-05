package com.light.rbac.service;

import com.light.rbac.dto.SysRoleUpdateDTO;
import com.light.rbac.model.SysRole;
import com.light.rbac.model.SysRolePermission;

/**
 * SysRoleService
 */
public interface SysRoleService {

    /**
     * 根据ID获取资源
     *
     * @param id 资源实例ID
     * @return Id所指向的资源实例
     * @throws 当Id所指向的资源不存在时，抛CustomRuntimeException异常
     */
    SysRole get(Long id);
    
    /**
     * 根据ID获取资源权限详细
     *
     * @param id 资源实例ID
     * @return Id所指向的资源实例权限详细
     * @throws 当Id所指向的资源不存在时，抛CustomRuntimeException异常
     */
    Iterable<SysRolePermission> getPermissions(Long id);

    /**
     * 创建
     *
     * @param sysRoleDTO 资源实例
     * @return 创建后的对象
     */
    SysRole create(SysRoleUpdateDTO sysRoleDTO);

    /**
     * 更新
     *
     * @param model 编辑后的资源实例
     * @return 修改后的对象
     */
    SysRole update(SysRoleUpdateDTO model);
    
    /**
     * 删除
     *
     * @param id 资源实例ID
     */
    void delete(Long id);

}
