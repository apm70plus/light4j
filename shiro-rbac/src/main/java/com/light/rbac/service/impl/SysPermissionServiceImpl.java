package com.light.rbac.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.light.exception.NotFoundException;
import com.light.rbac.model.SysPermission;
import com.light.rbac.model.SysRolePermission;
import com.light.rbac.repository.SysPermissionRepository;
import com.light.rbac.repository.SysRolePermissionRepository;
import com.light.rbac.service.SysPermissionService;

import lombok.NonNull;

/**
 * SysPermissionService 实现类
 */
@Service
@Transactional
public class SysPermissionServiceImpl implements SysPermissionService {

    @Autowired
    private SysPermissionRepository sysPermissionRepository;
    @Autowired
    private SysRolePermissionRepository sysRolePermissionRepository;

    @Override
    @Transactional(readOnly = true)
    public SysPermission get(@NonNull Long id) {
        final  Optional<SysPermission> model = sysPermissionRepository.findById(id);
        return model.orElseThrow(() -> new NotFoundException(String.format("查找的资源[%s]不存在.", id)));
    }

    @Override
    public SysPermission create(SysPermission model) {
        return sysPermissionRepository.save(model);
    }

    @Override
    public SysPermission update(SysPermission model) {
        return sysPermissionRepository.save(model);
    }

    @Override
    public void delete(@NonNull Long id) {
    	Optional<SysPermission> optional = this.sysPermissionRepository.findById(id);
    	if (optional.isPresent()) {
    		SysPermission permission = optional.get();
    		Iterable<SysRolePermission> allocatedPermissions = sysRolePermissionRepository.findAllByPermission(permission);
        	sysRolePermissionRepository.deleteAll(allocatedPermissions);
            sysPermissionRepository.delete(permission);
    	}
    }
}
