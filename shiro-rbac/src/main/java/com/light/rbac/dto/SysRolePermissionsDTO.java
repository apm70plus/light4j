package com.light.rbac.dto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter@Setter
public class SysRolePermissionsDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty("角色ID")
	private Long roleId;
	@ApiModelProperty("角色名")
	private String roleName;
	@ApiModelProperty("角色编码")
	private String roleCode;

	@ApiModelProperty("菜单权限列表")
	private List<MenuDTO> menus = new ArrayList<>();
	
	@Getter@Setter
	public class MenuDTO {
		private String menuCode;
		private String menuName;
		private List<PermissionDTO> permissions = new ArrayList<>();
	}
	@Getter@Setter
	public class PermissionDTO {
		private Long permissionId;
		private String permissionName;
	}
	
	public PermissionDTO newPermission(Long id, String name) {
		PermissionDTO p = new PermissionDTO();
		p.setPermissionId(id);
		p.setPermissionName(name);
		return p;
	}
	
	public MenuDTO newMenuDTO(String code, String name) {
		MenuDTO m = new MenuDTO();
		m.setMenuCode(code);
		m.setMenuName(name);
		return m;
	}
}
