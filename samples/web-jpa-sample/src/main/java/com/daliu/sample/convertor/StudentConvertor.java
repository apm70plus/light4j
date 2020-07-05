package com.daliu.sample.convertor;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.daliu.sample.dto.StudentDTO;
import com.daliu.sample.dto.StudentProfileDTO;
import com.daliu.sample.model.Student;
import com.daliu.sample.model.StudentProfile;
import com.daliu.sample.service.StudentService;
import com.light.web.dto.AbstractConvertor;

import lombok.NonNull;

/**
 * StudentConvertor
 */
@Component
public class StudentConvertor extends AbstractConvertor<Student, StudentDTO> {

    @Autowired
    private StudentService studentService;
    
    @Override
    public Student toModel(@NonNull final StudentDTO dto) {
        if (dto.isNew()) {//新增
            return constructModel(dto);
        } else {//更新
            return updateModel(dto);
        }
    }

    @Override
    public StudentDTO toDTO(@NonNull final Student model, final boolean forListView) {
        final StudentDTO dto = new StudentDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setGrade(model.getGrade());
        dto.setProfile(toDTO(model.getProfile()));

        return dto;
    }

    // 构建新Model
    private Student constructModel(final StudentDTO dto) {
        Student model = new Student();
        model.setName(dto.getName());
        model.setGrade(dto.getGrade());
        model.setProfile(toModel(dto.getProfile()));

        return model;
    }

    // 更新Model
    private Student updateModel(final StudentDTO dto) {
        Student model = studentService.get(dto.getId());
        model.setName(dto.getName());
        model.setGrade(dto.getGrade());
        model.setProfile(toModel(dto.getProfile()));

        return model;
    }
    
    private StudentProfile toModel(StudentProfileDTO dto) {
    	StudentProfile model = new StudentProfile();
    	BeanUtils.copyProperties(dto, model);
    	return model;
    }
    
    private StudentProfileDTO toDTO(StudentProfile model) {
    	StudentProfileDTO dto = new StudentProfileDTO();
    	BeanUtils.copyProperties(model, dto);
    	return dto;
    }
}
