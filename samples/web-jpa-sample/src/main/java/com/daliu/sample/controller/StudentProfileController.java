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

import com.daliu.sample.convertor.StudentProfileConvertor;
import com.daliu.sample.dto.StudentProfileDTO;
import com.daliu.sample.model.StudentProfile;
import com.daliu.sample.service.StudentProfileService;
import com.daliu.sample.repository.StudentProfileRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * StudentProfile的管理接口
 *
 * @author auto
 */
@Slf4j
@RestController
@RequestMapping("/w/studentProfiles")
@Api(tags = {"StudentProfile管理API" })
public class StudentProfileController {
    @Autowired
    private StudentProfileService studentProfileService;
    @Autowired
    private StudentProfileRepository studentProfileRepository;
    @Autowired
    private StudentProfileConvertor studentProfileConvertor;

    /**
     * 获取分页数据
     *
     * @param pageable 分页+排序参数
     * @return 分页数据
     */
    @ApiPageRSQL
    @ApiOperation(value = "获取分页数据", notes = "")
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponse<StudentProfileDTO> search(final Pageable pageable, @RsqlQuery(root=StudentProfile.class) Optional<Predicate> predicate) {
        final Page<StudentProfile> models = this.studentProfileRepository.findAll(predicate.orElseGet(() -> null), pageable);
        return this.studentProfileConvertor.toResponse(models);
    }

    /**
     * 取得详细数据
     *
     * @param id 资源ID
     * @return 资源详细
     */
    @ApiOperation(value = "获取详细数据", notes = "")
    @GetMapping(value = "/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<StudentProfileDTO> get(@PathVariable final Long id) {
        final StudentProfile model = this.studentProfileService.get(id);
        return this.studentProfileConvertor.toResponse(model);
    }

    /**
     * 新建操作
     *
     * @param studentProfileDTO 新建资源的DTO
     * @return 新建资源
     */
    @ApiOperation(value = "新建操作", notes = "")
    @PostMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<StudentProfileDTO> create(@RequestBody @Valid final StudentProfileDTO studentProfileDTO) {
        final StudentProfile model = this.studentProfileConvertor.toModel(studentProfileDTO);
        this.studentProfileService.create(model);
        if (log.isInfoEnabled()) {
            log.info("{} instance {} was created.", StudentProfile.class.getSimpleName(), model.getId());
        }
        return this.studentProfileConvertor.toResponse(model);
    }
    
    /**
     * 更新操作
     *
     * @param id 更新资源的ID
     * @param studentProfileDTO 更新资源的DTO
     * @return 更新后资源
     */
    @ApiOperation(value = "更新操作", notes = "")
    @PutMapping(value = "/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<StudentProfileDTO> update(@PathVariable final Long id, @RequestBody @Valid final StudentProfileDTO studentProfileDTO) {
        studentProfileDTO.setId(id);
        final StudentProfile model = this.studentProfileConvertor.toModel(studentProfileDTO);
        this.studentProfileService.update(model);
        if (log.isInfoEnabled()) {
            log.info("{} instance {} was updated.", StudentProfile.class.getSimpleName(), model.getId());
        }
        return this.studentProfileConvertor.toResponse(model);
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
        this.studentProfileService.delete(id);
        if (log.isInfoEnabled()) {
            log.info("{} instance {} was deleted.", StudentProfile.class.getSimpleName(), id);
        }
        return RestResponse.success();
    }
}
