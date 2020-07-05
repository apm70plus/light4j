package com.daliu.sample.controller;

import java.util.Optional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.types.Predicate;
import com.light.web.response.RestResponse;
import com.light.web.response.PageResponse;
import com.light.web.annotation.ApiPageRSQL;
import com.light.web.annotation.RsqlQuery;

import com.daliu.sample.convertor.TeacherConvertor;
import com.daliu.sample.dto.TeacherDTO;
import com.daliu.sample.model.Teacher;
import com.daliu.sample.service.TeacherService;
import com.daliu.sample.repository.TeacherRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * Teacher的管理接口
 *
 * @author auto
 */
@Slf4j
@RestController
@RequestMapping("/w/teachers")
@Api(tags = {"Teacher管理API" })
public class TeacherController {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private TeacherConvertor teacherConvertor;

    /**
     * 获取分页数据
     *
     * @param pageable 分页+排序参数
     * @return 分页数据
     */
    @ApiPageRSQL
    @ApiOperation(value = "获取分页数据", notes = "")
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponse<TeacherDTO> search(final Pageable pageable, @RsqlQuery(root=Teacher.class) Optional<Predicate> predicate) {
        final Page<Teacher> models = this.teacherRepository.findAll(predicate.orElseGet(() -> null), pageable);
        return this.teacherConvertor.toResponse(models);
    }

    /**
     * 取得详细数据
     *
     * @param id 资源ID
     * @return 资源详细
     */
    @ApiOperation(value = "获取详细数据", notes = "")
    @GetMapping(value = "/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<TeacherDTO> get(@PathVariable final Long id) {
        final Teacher model = this.teacherService.get(id);
        return this.teacherConvertor.toResponse(model);
    }

    /**
     * 新建操作
     *
     * @param teacherDTO 新建资源的DTO
     * @return 新建资源
     */
    @ApiOperation(value = "新建操作", notes = "")
    @PostMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<TeacherDTO> create(@RequestBody @Valid final TeacherDTO teacherDTO) {
        final Teacher model = this.teacherConvertor.toModel(teacherDTO);
        this.teacherService.create(model);
        if (log.isInfoEnabled()) {
            log.info("{} instance {} was created.", Teacher.class.getSimpleName(), model.getId());
        }
        return this.teacherConvertor.toResponse(model);
    }
    
    /**
     * 更新操作
     *
     * @param id 更新资源的ID
     * @param teacherDTO 更新资源的DTO
     * @return 更新后资源
     */
    @ApiOperation(value = "更新操作", notes = "")
    @PutMapping(value = "/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<TeacherDTO> update(@PathVariable final Long id, @RequestBody @Valid final TeacherDTO teacherDTO) {
        teacherDTO.setId(id);
        final Teacher model = this.teacherConvertor.toModel(teacherDTO);
        this.teacherService.update(model);
        if (log.isInfoEnabled()) {
            log.info("{} instance {} was updated.", Teacher.class.getSimpleName(), model.getId());
        }
        return this.teacherConvertor.toResponse(model);
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
        this.teacherService.delete(id);
        if (log.isInfoEnabled()) {
            log.info("{} instance {} was deleted.", Teacher.class.getSimpleName(), id);
        }
        return RestResponse.success();
    }
}
