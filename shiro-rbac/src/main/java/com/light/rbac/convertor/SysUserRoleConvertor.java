package com.light.rbac.convertor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.light.rbac.dto.SysRoleDTO;
import com.light.rbac.dto.SysUserRolesDTO;
import com.light.rbac.model.SysUserRole;
import com.light.web.response.RestResponse;

/**
 * SysUserRoleConvertor
 */
@Component
public class SysUserRoleConvertor {

	public SysUserRolesDTO toUserRolesDTO(Long userId, Iterable<SysUserRole> userRols) {
		List<SysRoleDTO> dtos = new ArrayList<>();
		userRols.forEach(role -> {
        	SysRoleDTO dto = new SysRoleDTO();
        	dto.setId(role.getRole().getId());
        	dto.setName(role.getRole().getName());
        	dtos.add(dto);
        });
        SysUserRolesDTO dto = new SysUserRolesDTO();
        dto.setRoles(dtos);
        dto.setId(userId);
        return dto;
	}
	
	public RestResponse<SysUserRolesDTO> toResponse(Long userId, Iterable<SysUserRole> userRoles) {
		return RestResponse.success(toUserRolesDTO(userId, userRoles));
	}
}
