package com.daliu.sample.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.light.jpa.domain.BasicEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 学生
 * 
 * @author daliu
 */
@Entity
@Getter@Setter
public class Student extends BasicEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 姓名
	 */
	@Size(min = 1, max = 50, message = "valid.name_length_1to50")
	@Column(length = 50)
	private String name;
	
	/**
	 * 年级
	 */
	@Min(value = 1, message = "valid.grade_between_1to4")
	@Max(value = 4, message = "valid.grade_between_1to4")
	private int grade;
	
	/**
	 * 附加信息
	 */
	@NotNull
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private StudentProfile profile = new StudentProfile();
	
	/**
	 * 所选课程
	 */
	@ManyToMany
	private List<Course> courses;
}
