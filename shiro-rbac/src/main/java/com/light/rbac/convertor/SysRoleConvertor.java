package com.light.rbac.convertor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.light.rbac.dto.SysRoleDTO;
import com.light.rbac.dto.SysRolePermissionsDTO;
import com.light.rbac.dto.SysRolePermissionsDTO.MenuDTO;
import com.light.rbac.dto.SysRolePermissionsDTO.PermissionDTO;
import com.light.rbac.model.SysPermission;
import com.light.rbac.model.SysRole;
import com.light.rbac.model.SysRolePermission;
import com.light.web.dto.AbstractConvertor;

import lombok.NonNull;

/**
 * SysRoleConvertor
 */
@Component
public class SysRoleConvertor extends AbstractConvertor<SysRole, SysRoleDTO> {

    @Override
    public SysRole toModel(@NonNull final SysRoleDTO dto) {
    	throw new UnsupportedOperationException("unsupported method");
    }

    @Override
    public SysRoleDTO toDTO(@NonNull final SysRole model, final boolean forListView) {
        final SysRoleDTO dto = new SysRoleDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setCode(model.getCode());

        return dto;
    }
    
    public SysRolePermissionsDTO toPermissionsDTO(Iterable<SysRolePermission> permissions) {
    	SysRolePermissionsDTO dto = new SysRolePermissionsDTO();
    	Map<String, MenuDTO> menus = new HashMap<>();
    	permissions.forEach(p -> {
    		if (dto.getRoleId() == null) {
    			SysRole role = p.getRole();
    			dto.setRoleId(role.getId());
    			dto.setRoleCode(role.getCode());
    			dto.setRoleName(role.getName());
    		}
    		SysPermission permission = p.getPermission();
    		PermissionDTO permissionDTO = dto.newPermission(permission.getId(), permission.getName());
    		MenuDTO menu = menus.get(permission.getMenuCode());
    		if (menu == null) {
    			menu = dto.newMenuDTO(permission.getMenuCode(), permission.getMenuName());
    			menus.put(menu.getMenuCode(), menu);
    		}
    		menu.getPermissions().add(permissionDTO);
    	});
    	List<MenuDTO> menuList = menus.values().stream()
    	    .sorted((m1,m2) -> m1.getMenuCode().compareTo(m2.getMenuCode()))
    	    .collect(Collectors.toList());
    	dto.setMenus(menuList);
    	return dto;
    }
}
