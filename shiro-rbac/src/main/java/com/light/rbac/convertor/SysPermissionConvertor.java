package com.light.rbac.convertor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.light.rbac.dto.SysPermissionDTO;
import com.light.rbac.model.SysPermission;
import com.light.rbac.service.SysPermissionService;
import com.light.web.dto.AbstractConvertor;

import lombok.NonNull;

/**
 * SysPermissionConvertor
 */
@Component
public class SysPermissionConvertor extends AbstractConvertor<SysPermission, SysPermissionDTO> {

    @Autowired
    private SysPermissionService sysPermissionService;
    
    @Override
    public SysPermission toModel(@NonNull final SysPermissionDTO dto) {
        if (dto.isNew()) {//新增
            return constructModel(dto);
        } else {//更新
            return updateModel(dto);
        }
    }

    @Override
    public SysPermissionDTO toDTO(@NonNull final SysPermission model, final boolean forListView) {
        final SysPermissionDTO dto = new SysPermissionDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setCode(model.getCode());
        dto.setMenuCode(model.getMenuCode());
        dto.setMenuName(model.getMenuName());
        dto.setRequired(model.isRequired());

        return dto;
    }

    // 构建新Model
    private SysPermission constructModel(final SysPermissionDTO dto) {
        SysPermission model = new SysPermission();
        model.setName(dto.getName());
        model.setCode(dto.getCode());
        model.setMenuCode(dto.getMenuCode());
        model.setMenuName(dto.getMenuName());
        model.setRequired(dto.isRequired());

        return model;
    }

    // 更新Model
    private SysPermission updateModel(final SysPermissionDTO dto) {
        SysPermission model = sysPermissionService.get(dto.getId());
        model.setName(dto.getName());
        model.setCode(dto.getCode());
        model.setMenuCode(dto.getMenuCode());
        model.setMenuName(dto.getMenuName());
        model.setRequired(dto.isRequired());

        return model;
    }
}
