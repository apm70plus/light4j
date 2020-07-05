package com.light.rbac.dto;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.light.web.dto.AbstractDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter@Setter
public class SysUserRolesDTO extends AbstractDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 角色列表
	 */
    @ApiModelProperty("角色列表")
	@NotBlank
	private List<SysRoleDTO> roles;
}
