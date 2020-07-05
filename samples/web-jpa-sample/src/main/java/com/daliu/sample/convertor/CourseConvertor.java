package com.daliu.sample.convertor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.light.web.dto.AbstractConvertor;
import com.daliu.sample.dto.CourseDTO;
import com.daliu.sample.model.Course;
import com.daliu.sample.service.CourseService;
import lombok.NonNull;

/**
 * CourseConvertor
 */
@Component
public class CourseConvertor extends AbstractConvertor<Course, CourseDTO> {

    @Autowired
    private CourseService courseService;
    
    @Override
    public Course toModel(@NonNull final CourseDTO dto) {
        if (dto.isNew()) {//新增
            return constructModel(dto);
        } else {//更新
            return updateModel(dto);
        }
    }

    @Override
    public CourseDTO toDTO(@NonNull final Course model, final boolean forListView) {
        final CourseDTO dto = new CourseDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setGrade(model.getGrade());

        return dto;
    }

    // 构建新Model
    private Course constructModel(final CourseDTO dto) {
        Course model = new Course();
        model.setName(dto.getName());
        model.setGrade(dto.getGrade());

        return model;
    }

    // 更新Model
    private Course updateModel(final CourseDTO dto) {
        Course model = courseService.get(dto.getId());
        model.setName(dto.getName());
        model.setGrade(dto.getGrade());

        return model;
    }
}
