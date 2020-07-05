package com.light.auth.bean;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginDTO {

	@NotBlank
	@ApiModelProperty(value = "用户名", position = 1)
	private String username;
	@NotBlank
	@ApiModelProperty(value = "密码", position = 2)
	private String password;
}
