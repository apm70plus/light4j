package com.daliu.sample.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.daliu.sample.model.Course;

/**
 * CourseRepository
 */
public interface CourseRepository extends CrudRepository<Course, Long>, QuerydslPredicateExecutor<Course> {

}
