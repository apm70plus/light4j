package com.daliu.sample.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.daliu.sample.model.Student;

/**
 * StudentRepository
 */
public interface StudentRepository extends CrudRepository<Student, Long>, QuerydslPredicateExecutor<Student> {

}
