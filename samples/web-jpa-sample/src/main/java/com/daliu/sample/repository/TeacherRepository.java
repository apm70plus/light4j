package com.daliu.sample.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.daliu.sample.model.Teacher;

/**
 * TeacherRepository
 */
public interface TeacherRepository extends CrudRepository<Teacher, Long>, QuerydslPredicateExecutor<Teacher> {

}
