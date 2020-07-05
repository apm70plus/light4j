package com.light.rbac.convertor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.light.rbac.dto.SysUserDTO;
import com.light.rbac.model.SysUser;
import com.light.rbac.service.SysUserService;
import com.light.web.dto.AbstractConvertor;

import lombok.NonNull;

/**
 * SysUserConvertor
 */
@Component
public class SysUserConvertor extends AbstractConvertor<SysUser, SysUserDTO> {

    @Autowired
    private SysUserService sysUserService;
    
    @Override
    public SysUser toModel(@NonNull final SysUserDTO dto) {
        if (dto.isNew()) {//新增
            return constructModel(dto);
        } else {//更新
            return updateModel(dto);
        }
    }

    @Override
    public SysUserDTO toDTO(@NonNull final SysUser model, final boolean forListView) {
        final SysUserDTO dto = new SysUserDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setLoginId(model.getLoginId());
        dto.setMobile(model.getMobile());
        dto.setEmail(model.getEmail());
        dto.setPassword(model.getPassword());
        dto.setEnabled(model.isEnabled());
        dto.setAccountLocked(model.isAccountLocked());
        dto.setAccountExpired(model.isAccountExpired());
        dto.setCredentialsExpired(model.isCredentialsExpired());

        return dto;
    }

    // 构建新Model
    private SysUser constructModel(final SysUserDTO dto) {
    	SysUser model = new SysUser();
        model.setName(dto.getName());
        model.setLoginId(dto.getLoginId());
        model.setMobile(dto.getMobile());
        model.setEmail(dto.getEmail());
        model.setPassword(dto.getPassword());
        model.setEnabled(dto.isEnabled());
        model.setAccountLocked(dto.isAccountLocked());
        model.setAccountExpired(dto.isAccountExpired());
        model.setCredentialsExpired(dto.isCredentialsExpired());

        return model;
    }

    // 更新Model
    private SysUser updateModel(final SysUserDTO dto) {
    	SysUser model = sysUserService.get(dto.getId());
        model.setName(dto.getName());
        model.setLoginId(dto.getLoginId());
        model.setMobile(dto.getMobile());
        model.setEmail(dto.getEmail());
        model.setPassword(dto.getPassword());
        model.setEnabled(dto.isEnabled());
        model.setAccountLocked(dto.isAccountLocked());
        model.setAccountExpired(dto.isAccountExpired());
        model.setCredentialsExpired(dto.isCredentialsExpired());

        return model;
    }
}