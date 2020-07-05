package com.daliu.sample.convertor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.light.web.dto.AbstractConvertor;
import com.daliu.sample.dto.TeacherDTO;
import com.daliu.sample.model.Teacher;
import com.daliu.sample.service.TeacherService;
import lombok.NonNull;

/**
 * TeacherConvertor
 */
@Component
public class TeacherConvertor extends AbstractConvertor<Teacher, TeacherDTO> {

    @Autowired
    private TeacherService teacherService;
    
    @Override
    public Teacher toModel(@NonNull final TeacherDTO dto) {
        if (dto.isNew()) {//新增
            return constructModel(dto);
        } else {//更新
            return updateModel(dto);
        }
    }

    @Override
    public TeacherDTO toDTO(@NonNull final Teacher model, final boolean forListView) {
        final TeacherDTO dto = new TeacherDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setResume(model.getResume());

        return dto;
    }

    // 构建新Model
    private Teacher constructModel(final TeacherDTO dto) {
        Teacher model = new Teacher();
        model.setName(dto.getName());
        model.setResume(dto.getResume());

        return model;
    }

    // 更新Model
    private Teacher updateModel(final TeacherDTO dto) {
        Teacher model = teacherService.get(dto.getId());
        model.setName(dto.getName());
        model.setResume(dto.getResume());

        return model;
    }
}
