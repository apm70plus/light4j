package com.daliu.sample.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.light.exception.NotFoundException;
import com.daliu.sample.service.CourseService;
import com.daliu.sample.model.Course;
import com.daliu.sample.repository.CourseRepository;
import lombok.NonNull;

/**
 * CourseService 实现类
 */
@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Override
    @Transactional(readOnly = true)
    public Course get(@NonNull Long id) {
        final  Optional<Course> model = courseRepository.findById(id);
        return model.orElseThrow(() -> new NotFoundException(String.format("查找的资源[%s]不存在.", id)));
    }

    @Override
    public Course create(Course model) {
        // TODO: 业务逻辑
        return courseRepository.save(model);
    }

    @Override
    public Course update(Course model) {
        // TODO: 业务逻辑
        return courseRepository.save(model);
    }

    @Override
    public void delete(@NonNull Long id) {
        // TODO: 业务逻辑
        courseRepository.deleteById(id);
    }
}
