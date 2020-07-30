package com.daliu.sample.controller;

import com.daliu.sample.convertor.CourseConvertor;
import com.daliu.sample.model.Course;
import com.daliu.sample.repository.CourseRepository;
import com.daliu.sample.service.CourseService;
import com.light.exception.BusinessException;
import com.light.web.response.PageResponse;
import com.light.web.response.RestResponse;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private CourseConvertor courseConvertor;
	@Autowired
	private CourseService courseService;

	@PostMapping("/test")
	public PageResponse<Course> search(final Pageable pageable, @QuerydslPredicate(root= Course.class) Predicate predicate) {
		final Page<Course> models = this.courseRepository.findAll(predicate, pageable);
		return PageResponse.success(models);
	}

	@GetMapping("/hello")
	public RestResponse<String> hello() {
		return RestResponse.success("world!");
	}
	
	@GetMapping("/exception")
	public RestResponse<String> exception() {
		throw new BusinessException("业务异常，数据不合法！");
	}
}
