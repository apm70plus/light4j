package com.daliu.sample.model;

import java.time.LocalDate;

import javax.persistence.Entity;

import com.light.jpa.domain.BasicEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 学生附加信息
 * 
 * @author daliu
 */
@Entity
@Getter@Setter
public class StudentProfile extends BasicEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 身份证号
	 */
	private String cardNo;
	
	/**
	 * 籍贯
	 */
	private String nativePlace;
	
	/**
	 * 家庭住址
	 */
	private String homeAddress;
	
	/**
	 * 出生年月日
	 */
	private LocalDate birthDate;
}
