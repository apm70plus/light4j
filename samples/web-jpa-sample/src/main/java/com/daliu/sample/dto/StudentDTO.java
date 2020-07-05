package com.daliu.sample.dto;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.light.web.dto.AbstractDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 学生
 * 
 * @author daliu
 */
@ApiModel
@Getter@Setter
public class StudentDTO extends AbstractDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 姓名
	 */
    @ApiModelProperty("姓名")
	@Size(min = 1, max = 50, message = "valid.name_length_1to50")
	private String name;
	
	/**
	 * 年级
	 */
    @ApiModelProperty("年级")
	@Min(value = 1, message = "valid.grade_between_1to4")
	@Max(value = 4, message = "valid.grade_between_1to4")
	private int grade;
	
	/**
	 * 附加信息
	 */
    @ApiModelProperty("附加信息")
	@NotNull
	private StudentProfileDTO profile;
}
