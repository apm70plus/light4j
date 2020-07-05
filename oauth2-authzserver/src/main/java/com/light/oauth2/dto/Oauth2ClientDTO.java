package com.light.oauth2.dto;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.light.web.dto.AbstractDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter@Setter
public class Oauth2ClientDTO extends AbstractDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 客户端名称
	 */
    @ApiModelProperty("客户端名称")
	@NotBlank
	@Size(max = 100)
	private String clientName;
	
	/**
	 * 客户端ID
	 */
    @ApiModelProperty("客户端ID")
	@Size(max = 100)
	private String clientId;
	
	/**
	 * 客户端安全key
	 */
    @ApiModelProperty("客户端安全key")
	@Size(max = 100)
	private String clientSecret;
    
	/**
	 * 客户端权限范围（逗号分割）
	 */
    @ApiModelProperty("客户端权限范围（逗号分割）")
	@Size(max = 1024)
	private String scope;
    
	/**
	 * 资源服务（内部服务，允许执行密码授权）
	 */
    @ApiModelProperty("资源服务（内部服务，允许执行密码授权）")
	@Size(max = 100)
	private String resourceServer;
}
