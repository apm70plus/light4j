package com.daliu.sample.convertor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.light.web.dto.AbstractConvertor;
import com.daliu.sample.dto.StudentProfileDTO;
import com.daliu.sample.model.StudentProfile;
import com.daliu.sample.service.StudentProfileService;
import lombok.NonNull;

/**
 * StudentProfileConvertor
 */
@Component
public class StudentProfileConvertor extends AbstractConvertor<StudentProfile, StudentProfileDTO> {

    @Autowired
    private StudentProfileService studentProfileService;
    
    @Override
    public StudentProfile toModel(@NonNull final StudentProfileDTO dto) {
        if (dto.isNew()) {//新增
            return constructModel(dto);
        } else {//更新
            return updateModel(dto);
        }
    }

    @Override
    public StudentProfileDTO toDTO(@NonNull final StudentProfile model, final boolean forListView) {
        final StudentProfileDTO dto = new StudentProfileDTO();
        dto.setId(model.getId());
        dto.setCardNo(model.getCardNo());
        dto.setNativePlace(model.getNativePlace());
        dto.setHomeAddress(model.getHomeAddress());
        dto.setBirthDate(model.getBirthDate());

        return dto;
    }

    // 构建新Model
    private StudentProfile constructModel(final StudentProfileDTO dto) {
        StudentProfile model = new StudentProfile();
        model.setCardNo(dto.getCardNo());
        model.setNativePlace(dto.getNativePlace());
        model.setHomeAddress(dto.getHomeAddress());
        model.setBirthDate(dto.getBirthDate());

        return model;
    }

    // 更新Model
    private StudentProfile updateModel(final StudentProfileDTO dto) {
        StudentProfile model = studentProfileService.get(dto.getId());
        model.setCardNo(dto.getCardNo());
        model.setNativePlace(dto.getNativePlace());
        model.setHomeAddress(dto.getHomeAddress());
        model.setBirthDate(dto.getBirthDate());

        return model;
    }
}
