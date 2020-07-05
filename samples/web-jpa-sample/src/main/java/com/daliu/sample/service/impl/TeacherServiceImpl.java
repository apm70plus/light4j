package com.daliu.sample.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.light.exception.NotFoundException;
import com.daliu.sample.service.TeacherService;
import com.daliu.sample.model.Teacher;
import com.daliu.sample.repository.TeacherRepository;
import lombok.NonNull;

/**
 * TeacherService 实现类
 */
@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    @Transactional(readOnly = true)
    public Teacher get(@NonNull Long id) {
        final  Optional<Teacher> model = teacherRepository.findById(id);
        return model.orElseThrow(() -> new NotFoundException(String.format("查找的资源[%s]不存在.", id)));
    }

    @Override
    public Teacher create(Teacher model) {
        // TODO: 业务逻辑
        return teacherRepository.save(model);
    }

    @Override
    public Teacher update(Teacher model) {
        // TODO: 业务逻辑
        return teacherRepository.save(model);
    }

    @Override
    public void delete(@NonNull Long id) {
        // TODO: 业务逻辑
        teacherRepository.deleteById(id);
    }
}
