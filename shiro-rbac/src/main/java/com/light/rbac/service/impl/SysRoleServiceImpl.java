package com.light.rbac.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.light.exception.NotFoundException;
import com.light.rbac.dto.SysRoleUpdateDTO;
import com.light.rbac.model.SysPermission;
import com.light.rbac.model.SysRole;
import com.light.rbac.model.SysRolePermission;
import com.light.rbac.repository.SysPermissionRepository;
import com.light.rbac.repository.SysRolePermissionRepository;
import com.light.rbac.repository.SysRoleRepository;
import com.light.rbac.service.SysRoleService;

import lombok.NonNull;

/**
 * SysRoleService 实现类
 */
@Service
@Transactional
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleRepository sysRoleRepository;
    @Autowired
    private SysRolePermissionRepository sysRolePermissionRepository;
    @Autowired
    private SysPermissionRepository sysPermissionRepository;

    @Override
    @Transactional(readOnly = true)
    public SysRole get(@NonNull Long id) {
        final  Optional<SysRole> model = sysRoleRepository.findById(id);
        return model.orElseThrow(() -> new NotFoundException(String.format("查找的资源[%s]不存在.", id)));
    }

    @Override
    public SysRole create(SysRoleUpdateDTO createDTO) {
    	final SysRole role = new SysRole();
    	role.setName(createDTO.getName());
    	role.setCode(createDTO.getCode());
    	this.sysRoleRepository.save(role);
    	addPermissions(role, createDTO.getPermissions());
        return role;
    }

    @Override
    public SysRole update(SysRoleUpdateDTO updateDTO) {
        SysRole role = this.get(updateDTO.getId());
        role.setName(updateDTO.getName());
        role.setCode(updateDTO.getCode());
        removeAllPermissions(role);
    	addPermissions(role, updateDTO.getPermissions());
        return sysRoleRepository.save(role);
    }


    @Override
    public void delete(@NonNull Long id) {
        Optional<SysRole> optional = this.sysRoleRepository.findById(id);
        if (optional.isPresent()) {
        	SysRole role = optional.get();
			this.removeAllPermissions(role);
        	sysRoleRepository.delete(role);
        }
    }

	@Override
	public Iterable<SysRolePermission> getPermissions(Long id) {
		SysRole role = this.get(id);
		Iterable<SysRolePermission> permissions = sysRolePermissionRepository.findAllByRole(role);
		return permissions;
	}
	
	private void addPermissions(SysRole role, List<Long> permissionIds) {
		if (CollectionUtils.isEmpty(permissionIds)) {
    		return;
    	}
		Iterable<SysPermission> permissions = sysPermissionRepository.findAllById(permissionIds);
		List<SysRolePermission> rolePermissions = new ArrayList<>();
		permissions.forEach(p -> {
			SysRolePermission rp = new SysRolePermission();
			rp.setRole(role);
			rp.setPermission(p);
			rolePermissions.add(rp);
		});
		this.sysRolePermissionRepository.saveAll(rolePermissions);
	}
	

	private void removeAllPermissions(SysRole role) {
		// 删除旧的
    	Iterable<SysRolePermission> oldPermissions = sysRolePermissionRepository.findAllByRole(role);
    	sysRolePermissionRepository.deleteAll(oldPermissions);
	}
}
