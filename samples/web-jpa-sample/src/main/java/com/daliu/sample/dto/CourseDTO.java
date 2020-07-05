package com.daliu.sample.dto;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.light.web.dto.AbstractDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 课程
 * 
 * @author daliu
 */
@ApiModel
@Getter@Setter
public class CourseDTO extends AbstractDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 课程名
	 */
    @ApiModelProperty("课程名")
	@Size(min = 1, max = 50, message = "valid.name.length_1to50")
	private String name;
	
	/**
	 * 年级
	 */
    @ApiModelProperty("年级")
	@Min(value = 1, message = "valid.grade.between_1to4")
	@Max(value = 4, message = "valid.grade.between_1to4")
	private int grade;
}
