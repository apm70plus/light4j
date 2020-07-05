package com.light.rbac.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.light.rbac.convertor.SysRoleConvertor;
import com.light.rbac.dto.SysRoleDTO;
import com.light.rbac.dto.SysRolePermissionsDTO;
import com.light.rbac.dto.SysRoleUpdateDTO;
import com.light.rbac.model.SysRole;
import com.light.rbac.model.SysRolePermission;
import com.light.rbac.repository.SysRoleRepository;
import com.light.rbac.service.SysRoleService;
import com.light.web.annotation.ApiPageRSQL;
import com.light.web.annotation.RsqlQuery;
import com.light.web.response.PageResponse;
import com.light.web.response.RestResponse;
import com.querydsl.core.types.Predicate;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * SysRole的管理接口
 *
 * @author auto
 */
@Slf4j
@RestController
@RequestMapping("/api/sysRoles")
@Api(tags = {"SysRole管理API" })
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRoleRepository sysRoleRepository;
    @Autowired
    private SysRoleConvertor sysRoleConvertor;

    /**
     * 获取分页数据
     *
     * @param pageable 分页+排序参数
     * @return 分页数据
     */
    @ApiPageRSQL
    @ApiOperation(value = "获取分页数据", notes = "")
    @RequiresPermissions("role:list")
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponse<SysRoleDTO> search(final Pageable pageable, @RsqlQuery(root=SysRole.class) Optional<Predicate> predicate) {
        final Page<SysRole> models = this.sysRoleRepository.findAll(predicate.orElseGet(() -> null), pageable);
        return this.sysRoleConvertor.toResponse(models);
    }
    
    /**
     * 取得角色详细数据
     *
     * @param id 资源ID
     * @return 资源详细
     */
    @ApiOperation(value = "获取角色的菜单&权限详细数据", notes = "")
    @RequiresPermissions("role:get")
    @GetMapping(value = "/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<SysRolePermissionsDTO> getPermissions(@PathVariable final Long id) {
        final Iterable<SysRolePermission> permissions = this.sysRoleService.getPermissions(id);
        return RestResponse.success(this.sysRoleConvertor.toPermissionsDTO(permissions));
    }

    /**
     * 新建操作
     *
     * @param sysRoleDTO 新建资源的DTO
     * @return 新建资源
     */
    @ApiOperation(value = "新建操作", notes = "")
    @RequiresPermissions("role:create")
    @PostMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<SysRoleDTO> create(@RequestBody @Valid final SysRoleUpdateDTO sysRoleDTO) {
    	SysRole model = this.sysRoleService.create(sysRoleDTO);
        if (log.isInfoEnabled()) {
            log.info("{} instance {} was created.", SysRole.class.getSimpleName(), model.getId());
        }
        return this.sysRoleConvertor.toResponse(model);
    }
    
    /**
     * 更新操作
     *
     * @param id 更新资源的ID
     * @param sysRoleDTO 更新资源的DTO
     * @return 更新后资源
     */
    @ApiOperation(value = "更新操作", notes = "")
    @RequiresPermissions("role:update")
    @PutMapping(value = "/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<SysRoleDTO> update(@PathVariable final Long id, @RequestBody @Valid final SysRoleUpdateDTO sysRoleDTO) {
        sysRoleDTO.setId(id);
        final SysRole model = this.sysRoleService.update(sysRoleDTO);
        if (log.isInfoEnabled()) {
            log.info("{} instance {} was updated.", SysRole.class.getSimpleName(), model.getId());
        }
        return this.sysRoleConvertor.toResponse(model);
    }

    /**
     * 删除操作
     *
     * @param Id 资源ID
     * @return 操作结果
     */
    @ApiOperation(value = "删除操作", notes = "")
    @RequiresPermissions("role:delete")
    @DeleteMapping(value = "/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<Void> delete(@PathVariable final Long id) {
        this.sysRoleService.delete(id);
        if (log.isInfoEnabled()) {
            log.info("{} instance {} was deleted.", SysRole.class.getSimpleName(), id);
        }
        return RestResponse.success();
    }
}
