package com.light.rbac.dto;
import org.hibernate.validator.constraints.Length;

import com.light.web.dto.AbstractDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter@Setter
public class SysUserDTO extends AbstractDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 姓名
	 */
    @ApiModelProperty("姓名")
    @Length(max = 32)
	private String name;
	
    /**
     * 登录名称
     */
    @ApiModelProperty("登录名称")
    @Length(max = 32)
    private String loginId;

    /**
     * 手机号码
     */
    @ApiModelProperty("手机号码")
    @Length(max = 20)
    private String mobile;

    /**
     * 电子邮箱
     */
    @ApiModelProperty("电子邮箱")
    @Length(max = 255)
    private String email;

    /**
     * 登录密码
     */
    @ApiModelProperty("登录密码")
    @Length(max = 80)
    private String password;
    
    /**
     * 帐号是否启用
     */
    @ApiModelProperty("帐号是否启用")
    private boolean enabled = true;

    /**
     * 帐号是否锁定
     */
    @ApiModelProperty("帐号是否锁定")
    private boolean accountLocked = false;

    /**
     * 帐号是否过期
     */
    @ApiModelProperty("帐号是否过期")
    private boolean accountExpired = false;

    /**
     * 密码是否过期
     */
    @ApiModelProperty("密码是否过期")
    private boolean credentialsExpired = false;
}