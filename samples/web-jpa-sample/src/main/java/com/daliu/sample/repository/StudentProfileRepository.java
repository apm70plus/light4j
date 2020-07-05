package com.daliu.sample.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.daliu.sample.model.StudentProfile;

/**
 * StudentProfileRepository
 */
public interface StudentProfileRepository extends CrudRepository<StudentProfile, Long>, QuerydslPredicateExecutor<StudentProfile> {

}
