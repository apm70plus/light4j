package com.light.rbac.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.light.exception.NotFoundException;
import com.light.rbac.model.SysUser;
import com.light.rbac.repository.SysUserRepository;
import com.light.rbac.service.SysUserService;

import lombok.NonNull;

/**
 * SysUserService 实现类
 */
@Service
@Transactional
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Override
    @Transactional(readOnly = true)
    public SysUser get(@NonNull Long id) {
        final  Optional<SysUser> model = sysUserRepository.findById(id);
        return model.orElseThrow(() -> new NotFoundException(String.format("查找的资源[%s]不存在.", id)));
    }

    @Override
    public SysUser create(SysUser model) {
        // TODO: 业务逻辑
        return sysUserRepository.save(model);
    }

    @Override
    public SysUser update(SysUser model) {
        // TODO: 业务逻辑
        return sysUserRepository.save(model);
    }

    @Override
    public void delete(@NonNull Long id) {
        // TODO: 业务逻辑
        sysUserRepository.deleteById(id);
    }
}