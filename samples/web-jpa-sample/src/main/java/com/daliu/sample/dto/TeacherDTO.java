package com.daliu.sample.dto;

import javax.validation.constraints.Size;

import com.light.web.dto.AbstractDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 老师
 * 
 * @author daliu
 */
@ApiModel
@Getter
@Setter
public class TeacherDTO extends AbstractDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 姓名
	 */
	@ApiModelProperty("姓名")
	@Size(min = 1, max = 50, message = "valid.name_length_1to50")
	private String name;

	/**
	 * 简历
	 */
	@ApiModelProperty("简历")
	@Size(max = 512, message = "valid.resume_length_greaterThan_512")
	private String resume;
}
