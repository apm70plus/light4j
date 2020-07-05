package com.daliu.sample.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.light.exception.NotFoundException;
import com.daliu.sample.service.StudentProfileService;
import com.daliu.sample.model.StudentProfile;
import com.daliu.sample.repository.StudentProfileRepository;
import lombok.NonNull;

/**
 * StudentProfileService 实现类
 */
@Service
@Transactional
public class StudentProfileServiceImpl implements StudentProfileService {

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Override
    @Transactional(readOnly = true)
    public StudentProfile get(@NonNull Long id) {
        final  Optional<StudentProfile> model = studentProfileRepository.findById(id);
        return model.orElseThrow(() -> new NotFoundException(String.format("查找的资源[%s]不存在.", id)));
    }

    @Override
    public StudentProfile create(StudentProfile model) {
        // TODO: 业务逻辑
        return studentProfileRepository.save(model);
    }

    @Override
    public StudentProfile update(StudentProfile model) {
        // TODO: 业务逻辑
        return studentProfileRepository.save(model);
    }

    @Override
    public void delete(@NonNull Long id) {
        // TODO: 业务逻辑
        studentProfileRepository.deleteById(id);
    }
}
