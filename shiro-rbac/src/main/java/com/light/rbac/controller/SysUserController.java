package com.light.rbac.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
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

import com.light.rbac.convertor.SysUserConvertor;
import com.light.rbac.dto.SysUserDTO;
import com.light.rbac.model.SysUser;
import com.light.rbac.repository.SysUserRepository;
import com.light.rbac.service.SysUserService;
import com.light.web.annotation.ApiPageRSQL;
import com.light.web.annotation.RsqlQuery;
import com.light.web.response.PageResponse;
import com.light.web.response.RestResponse;
import com.querydsl.core.types.Predicate;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * User的管理接口
 *
 * @author auto
 */
@Slf4j
@RestController
@RequestMapping("/api/sysUsers")
@Api(tags = {"SysUser管理API" })
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRepository sysUserRepository;
    @Autowired
    private SysUserConvertor sysUserConvertor;

    /**
     * 获取分页数据
     *
     * @param pageable 分页+排序参数
     * @return 分页数据
     */
    @ApiPageRSQL
    @ApiOperation(value = "获取分页数据", notes = "")
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponse<SysUserDTO> search(final Pageable pageable, @RsqlQuery(root=SysUser.class) Optional<Predicate> predicate) {
        final Page<SysUser> models = this.sysUserRepository.findAll(predicate.orElseGet(() -> null), pageable);
        return this.sysUserConvertor.toResponse(models);
    }

    /**
     * 取得详细数据
     *
     * @param id 资源ID
     * @return 资源详细
     */
    @ApiOperation(value = "获取详细数据", notes = "")
    @GetMapping(value = "/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<SysUserDTO> get(@PathVariable final Long id) {
        final SysUser model = this.sysUserService.get(id);
        return this.sysUserConvertor.toResponse(model);
    }

    /**
     * 新建操作
     *
     * @param userDTO 新建资源的DTO
     * @return 新建资源
     */
    @ApiOperation(value = "新建操作", notes = "")
    @PostMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<SysUserDTO> create(@RequestBody @Valid final SysUserDTO userDTO) {
        final SysUser model = this.sysUserConvertor.toModel(userDTO);
        this.sysUserService.create(model);
        if (log.isInfoEnabled()) {
            log.info("{} instance {} was created.", User.class.getSimpleName(), model.getId());
        }
        return this.sysUserConvertor.toResponse(model);
    }
    
    /**
     * 更新操作
     *
     * @param id 更新资源的ID
     * @param userDTO 更新资源的DTO
     * @return 更新后资源
     */
    @ApiOperation(value = "更新操作", notes = "")
    @PutMapping(value = "/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<SysUserDTO> update(@PathVariable final Long id, @RequestBody @Valid final SysUserDTO userDTO) {
        userDTO.setId(id);
        final SysUser model = this.sysUserConvertor.toModel(userDTO);
        this.sysUserService.update(model);
        if (log.isInfoEnabled()) {
            log.info("{} instance {} was updated.", User.class.getSimpleName(), model.getId());
        }
        return this.sysUserConvertor.toResponse(model);
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
        this.sysUserService.delete(id);
        if (log.isInfoEnabled()) {
            log.info("{} instance {} was deleted.", User.class.getSimpleName(), id);
        }
        return RestResponse.success();
    }
}