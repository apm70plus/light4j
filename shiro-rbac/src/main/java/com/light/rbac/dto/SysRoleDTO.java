package com.light.rbac.dto;
import javax.validation.constraints.NotBlank;

import com.light.web.dto.AbstractDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter@Setter
public class SysRoleDTO extends AbstractDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 角色名
	 */
    @ApiModelProperty("角色名")
	@NotBlank
	private String name;
    
	/**
	 * 角色编码
	 */
    @ApiModelProperty("角色编码")
	@NotBlank
	private String code;
}
