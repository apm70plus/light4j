package com.daliu.sample.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.light.exception.NotFoundException;
import com.daliu.sample.service.StudentService;
import com.daliu.sample.model.Student;
import com.daliu.sample.repository.StudentRepository;
import lombok.NonNull;

/**
 * StudentService 实现类
 */
@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    @Transactional(readOnly = true)
    public Student get(@NonNull Long id) {
        final  Optional<Student> model = studentRepository.findById(id);
        return model.orElseThrow(() -> new NotFoundException(String.format("查找的资源[%s]不存在.", id)));
    }

    @Override
    public Student create(Student model) {
        // TODO: 业务逻辑
        return studentRepository.save(model);
    }

    @Override
    public Student update(Student model) {
        // TODO: 业务逻辑
        return studentRepository.save(model);
    }

    @Override
    public void delete(@NonNull Long id) {
        // TODO: 业务逻辑
        studentRepository.deleteById(id);
    }
}
