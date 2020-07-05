package com.daliu.sample.dto;
import com.light.web.dto.AbstractDTO;

import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * 学生附加信息
 * 
 * @author daliu
 */
@ApiModel
@Getter@Setter
public class StudentProfileDTO extends AbstractDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 身份证号
	 */
    @ApiModelProperty("身份证号")
	private String cardNo;
	
	/**
	 * 籍贯
	 */
    @ApiModelProperty("籍贯")
	private String nativePlace;
	
	/**
	 * 家庭住址
	 */
    @ApiModelProperty("家庭住址")
	private String homeAddress;
	
	/**
	 * 出生年月日
	 */
    @ApiModelProperty("出生年月日")
	private LocalDate birthDate;
}
