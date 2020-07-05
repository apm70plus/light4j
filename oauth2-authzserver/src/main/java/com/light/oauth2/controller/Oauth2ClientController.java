package com.light.oauth2.controller;

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

import com.light.oauth2.convertor.Oauth2ClientConvertor;
import com.light.oauth2.dto.Oauth2ClientDTO;
import com.light.oauth2.model.Oauth2Client;
import com.light.oauth2.repository.Oauth2ClientRepository;
import com.light.oauth2.service.Oauth2ClientService;
import com.light.web.annotation.ApiPageRSQL;
import com.light.web.annotation.RsqlQuery;
import com.light.web.response.PageResponse;
import com.light.web.response.RestResponse;
import com.querydsl.core.types.Predicate;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * Oauth2Client的管理接口
 *
 * @author auto
 */
@Slf4j
@RestController
@RequestMapping("/api/oauth2clients")
@Api(tags = {"Oauth2Client管理API" })
public class Oauth2ClientController {
    @Autowired
    private Oauth2ClientService oauth2ClientService;
    @Autowired
    private Oauth2ClientRepository oauth2ClientRepository;
    @Autowired
    private Oauth2ClientConvertor oauth2ClientConvertor;

    /**
     * 获取分页数据
     *
     * @param pageable 分页+排序参数
     * @return 分页数据
     */
    @ApiPageRSQL
    @ApiOperation(value = "获取分页数据", notes = "")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermissions(value="oauth2:client:list")
    public PageResponse<Oauth2ClientDTO> search(final Pageable pageable, @RsqlQuery(root=Oauth2Client.class) Optional<Predicate> predicate) {
        final Page<Oauth2Client> models = this.oauth2ClientRepository.findAll(predicate.orElseGet(() -> null), pageable);
        return this.oauth2ClientConvertor.toResponse(models);
    }

    /**
     * 取得详细数据
     *
     * @param id 资源ID
     * @return 资源详细
     */
    @ApiOperation(value = "获取详细数据", notes = "")
    @GetMapping(value = "/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermissions(value="oauth2:client:read")
    public RestResponse<Oauth2ClientDTO> get(@PathVariable final Long id) {
        final Oauth2Client model = this.oauth2ClientService.get(id);
        return this.oauth2ClientConvertor.toResponse(model);
    }

    /**
     * 新建操作
     *
     * @param oauth2ClientDTO 新建资源的DTO
     * @return 新建资源
     */
    @ApiOperation(value = "新建操作", notes = "")
    @PostMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermissions(value="oauth2:client:create")
    public RestResponse<Oauth2ClientDTO> create(@RequestBody @Valid final Oauth2ClientDTO oauth2ClientDTO) {
        final Oauth2Client model = this.oauth2ClientConvertor.toModel(oauth2ClientDTO);
        this.oauth2ClientService.create(model);
        if (log.isInfoEnabled()) {
            log.info("{} instance {} was created.", Oauth2Client.class.getSimpleName(), model.getId());
        }
        return this.oauth2ClientConvertor.toResponse(model);
    }
    
    /**
     * 更新操作
     *
     * @param id 更新资源的ID
     * @param oauth2ClientDTO 更新资源的DTO
     * @return 更新后资源
     */
    @ApiOperation(value = "更新操作", notes = "")
    @PutMapping(value = "/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermissions(value="oauth2:client:update")
    public RestResponse<Oauth2ClientDTO> update(@PathVariable final Long id, @RequestBody @Valid final Oauth2ClientDTO oauth2ClientDTO) {
        oauth2ClientDTO.setId(id);
        final Oauth2Client model = this.oauth2ClientConvertor.toModel(oauth2ClientDTO);
        this.oauth2ClientService.update(model);
        if (log.isInfoEnabled()) {
            log.info("{} instance {} was updated.", Oauth2Client.class.getSimpleName(), model.getId());
        }
        return this.oauth2ClientConvertor.toResponse(model);
    }

    /**
     * 删除操作
     *
     * @param Id 资源ID
     * @return 操作结果
     */
    @ApiOperation(value = "删除操作", notes = "")
    @DeleteMapping(value = "/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermissions(value="oauth2:client:delete")
    public RestResponse<Void> delete(@PathVariable final Long id) {
        this.oauth2ClientService.delete(id);
        if (log.isInfoEnabled()) {
            log.info("{} instance {} was deleted.", Oauth2Client.class.getSimpleName(), id);
        }
        return RestResponse.success();
    }
}
