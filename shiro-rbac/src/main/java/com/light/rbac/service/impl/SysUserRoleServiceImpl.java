package com.light.rbac.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.light.rbac.model.SysRole;
import com.light.rbac.model.SysUserRole;
import com.light.rbac.repository.SysRoleRepository;
import com.light.rbac.repository.SysUserRoleRepository;
import com.light.rbac.service.SysUserRoleService;

/**
 * SysUserRoleService 实现类
 */
@Service
@Transactional
public class SysUserRoleServiceImpl implements SysUserRoleService {

	@Autowired
	private SysUserRoleRepository sysUserRoleRepository;
	@Autowired
	private SysRoleRepository sysRoleRepository;
	
	@Override
	public Iterable<SysUserRole> updateUserRoles(Long userId, List<Long> roleIds) {
		Iterable<SysUserRole> allocatedRoles = sysUserRoleRepository.findAllByUserId(userId);
		sysUserRoleRepository.deleteAll(allocatedRoles);
		if (CollectionUtils.isEmpty(roleIds)) {
			return Collections.emptyList();
		}
		Iterable<SysRole> roles = sysRoleRepository.findAllById(roleIds);
		List<SysUserRole> userRoles = new ArrayList<>();
		roles.forEach(role -> {
			SysUserRole userRole = new SysUserRole();
			userRole.setUserId(userId);
			userRole.setRole(role);
			userRoles.add(userRole);
		});
		return sysUserRoleRepository.saveAll(userRoles);
	}

	@Override
	public void revokeUsersRole(SysRole role) {
		Iterable<SysUserRole> allocatedRoles = sysUserRoleRepository.findAllByRole(role);
		sysUserRoleRepository.deleteAll(allocatedRoles);
	}
}
