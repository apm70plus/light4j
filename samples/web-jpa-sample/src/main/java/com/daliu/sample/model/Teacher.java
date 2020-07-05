package com.daliu.sample.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Size;

import com.light.jpa.domain.BasicEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 老师
 * 
 * @author daliu
 */
@Entity
@Getter@Setter
public class Teacher extends BasicEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 姓名
	 */
	@Size(min = 1, max = 50, message = "valid.name_length_1to50")
	@Column(length = 50)
	private String name;
	
	/**
	 * 所授课程
	 */
	@ManyToMany(mappedBy = "teachers", targetEntity = Course.class)
	private List<Course> courses;
	
	/**
	 * 简历
	 */
	@Size(max = 512, message = "valid.resume_length_greaterThan_512")
	@Column(length = 50)
	private String resume;
}
