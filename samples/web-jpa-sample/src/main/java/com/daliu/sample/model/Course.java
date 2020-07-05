package com.daliu.sample.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.light.jpa.domain.BasicEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 课程
 * 
 * @author daliu
 */
@Entity
@Getter@Setter
public class Course extends BasicEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 课程名
	 */
	@Size(min = 1, max = 50, message = "valid.name.length_1to50")
	@Column(length = 50)
	private String name;
	
	/**
	 * 年级
	 */
	@Min(value = 1, message = "valid.grade.between_1to4")
	@Max(value = 4, message = "valid.grade.between_1to4")
	private int grade;
	
	/**
	 * 授课老师
	 */
	@ManyToMany
	private List<Teacher> teachers;
}
