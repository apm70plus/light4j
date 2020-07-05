package com.light.rbac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.light.rbac.convertor.SysUserRoleConvertor;
import com.light.rbac.dto.IdListDTO;
import com.light.rbac.dto.SysUserRolesDTO;
import com.light.rbac.model.SysUserRole;
import com.light.rbac.repository.SysUserRoleRepository;
import com.light.rbac.service.SysUserRoleService;
import com.light.web.response.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * SysUserRole的管理接口
 *
 * @author auto
 */
@Slf4j
@RestController
@RequestMapping("/api/sysUser")
@Api(tags = {"SysUser角色管理API" })
public class SysUserRoleController {

	@Autowired
	private SysUserRoleRepository sysUserRoleRepository;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysUserRoleConvertor sysUserRoleConvertor;
	
    /**
     * 取得用户角色列表
     *
     * @param userId 资源ID
     * @return 资源详细
     */
    @ApiOperation(value = "获取用户角色列表", notes = "")
    @GetMapping(value = "/{userId}/roles", produces=MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<SysUserRolesDTO> getUserRoles(@PathVariable final Long userId) {
        final Iterable<SysUserRole> roles = sysUserRoleRepository.findAllByUserId(userId);
        return sysUserRoleConvertor.toResponse(userId, roles);
    }
    
    /**
     * 取得用户角色列表
     *
     * @param userId 资源ID
     * @return 资源详细
     */
    @ApiOperation(value = "更新用户角色列表", notes = "")
    @PostMapping(value = "/{userId}/roles", produces=MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<SysUserRolesDTO> updateUserRoles(@PathVariable final Long userId, @RequestBody IdListDTO roleIds) {
    	Iterable<SysUserRole> roles = sysUserRoleService.updateUserRoles(userId, roleIds.getIds());
        if (log.isInfoEnabled()) {
            log.info("SysUser {}'s roles was updated.", userId);
        }
        return sysUserRoleConvertor.toResponse(userId, roles);
    }
}
