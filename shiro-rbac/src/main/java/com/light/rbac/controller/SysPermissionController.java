package com.light.rbac.controller;

import java.util.Optional;

import javax.validation.Valid;

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

import com.light.rbac.convertor.SysPermissionConvertor;
import com.light.rbac.dto.SysPermissionDTO;
import com.light.rbac.model.SysPermission;
import com.light.rbac.repository.SysPermissionRepository;
import com.light.rbac.service.SysPermissionService;
import com.light.web.annotation.ApiPageRSQL;
import com.light.web.annotation.RsqlQuery;
import com.light.web.response.PageResponse;
import com.light.web.response.RestResponse;
import com.querydsl.core.types.Predicate;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * SysPermission的管理接口
 *
 * @author auto
 */
@Slf4j
@RestController
@RequestMapping("/w/sysPermissions")
@Api(tags = {"SysPermission管理API" })
public class SysPermissionController {
    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private SysPermissionRepository sysPermissionRepository;
    @Autowired
    private SysPermissionConvertor sysPermissionConvertor;

    /**
     * 获取分页数据
     *
     * @param pageable 分页+排序参数
     * @return 分页数据
     */
    @ApiPageRSQL
    @ApiOperation(value = "获取分页数据", notes = "")
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponse<SysPermissionDTO> search(final Pageable pageable, @RsqlQuery(root=SysPermission.class) Optional<Predicate> predicate) {
        final Page<SysPermission> models = this.sysPermissionRepository.findAll(predicate.orElseGet(() -> null), pageable);
        return this.sysPermissionConvertor.toResponse(models);
    }

    /**
     * 取得详细数据
     *
     * @param id 资源ID
     * @return 资源详细
     */
    @ApiOperation(value = "获取详细数据", notes = "")
    @GetMapping(value = "/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<SysPermissionDTO> get(@PathVariable final Long id) {
        final SysPermission model = this.sysPermissionService.get(id);
        return this.sysPermissionConvertor.toResponse(model);
    }

    /**
     * 新建操作
     *
     * @param sysPermissionDTO 新建资源的DTO
     * @return 新建资源
     */
    @ApiOperation(value = "新建操作", notes = "")
    @PostMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<SysPermissionDTO> create(@RequestBody @Valid final SysPermissionDTO sysPermissionDTO) {
        final SysPermission model = this.sysPermissionConvertor.toModel(sysPermissionDTO);
        this.sysPermissionService.create(model);
        if (log.isInfoEnabled()) {
            log.info("{} instance {} was created.", SysPermission.class.getSimpleName(), model.getId());
        }
        return this.sysPermissionConvertor.toResponse(model);
    }
    
    /**
     * 更新操作
     *
     * @param id 更新资源的ID
     * @param sysPermissionDTO 更新资源的DTO
     * @return 更新后资源
     */
    @ApiOperation(value = "更新操作", notes = "")
    @PutMapping(value = "/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<SysPermissionDTO> update(@PathVariable final Long id, @RequestBody @Valid final SysPermissionDTO sysPermissionDTO) {
        sysPermissionDTO.setId(id);
        final SysPermission model = this.sysPermissionConvertor.toModel(sysPermissionDTO);
        this.sysPermissionService.update(model);
        if (log.isInfoEnabled()) {
            log.info("{} instance {} was updated.", SysPermission.class.getSimpleName(), model.getId());
        }
        return this.sysPermissionConvertor.toResponse(model);
    }

    /**
     * 删除操作
     *
     * @param Id 资源ID
     * @return 操作结果
     */
    @ApiOperation(value = "删除操作", notes = "")
    @DeleteMapping(value = "/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<Void> delete(@PathVariable final Long id) {
        this.sysPermissionService.delete(id);
        if (log.isInfoEnabled()) {
            log.info("{} instance {} was deleted.", SysPermission.class.getSimpleName(), id);
        }
        return RestResponse.success();
    }
}
