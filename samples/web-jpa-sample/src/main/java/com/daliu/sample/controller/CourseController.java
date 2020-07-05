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

import com.daliu.sample.convertor.CourseConvertor;
import com.daliu.sample.dto.CourseDTO;
import com.daliu.sample.model.Course;
import com.daliu.sample.service.CourseService;
import com.daliu.sample.repository.CourseRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * Course的管理接口
 *
 * @author auto
 */
@Slf4j
@RestController
@RequestMapping("/w/courses")
@Api(tags = {"Course管理API" })
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseConvertor courseConvertor;

    /**
     * 获取分页数据
     *
     * @param pageable 分页+排序参数
     * @return 分页数据
     */
    @ApiPageRSQL
    @ApiOperation(value = "获取分页数据", notes = "")
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponse<CourseDTO> search(final Pageable pageable, @RsqlQuery(root=Course.class) Optional<Predicate> predicate) {
        final Page<Course> models = this.courseRepository.findAll(predicate.orElseGet(() -> null), pageable);
        return this.courseConvertor.toResponse(models);
    }

    /**
     * 取得详细数据
     *
     * @param id 资源ID
     * @return 资源详细
     */
    @ApiOperation(value = "获取详细数据", notes = "")
    @GetMapping(value = "/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<CourseDTO> get(@PathVariable final Long id) {
        final Course model = this.courseService.get(id);
        return this.courseConvertor.toResponse(model);
    }

    /**
     * 新建操作
     *
     * @param courseDTO 新建资源的DTO
     * @return 新建资源
     */
    @ApiOperation(value = "新建操作", notes = "")
    @PostMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<CourseDTO> create(@RequestBody @Valid final CourseDTO courseDTO) {
        final Course model = this.courseConvertor.toModel(courseDTO);
        this.courseService.create(model);
        if (log.isInfoEnabled()) {
            log.info("{} instance {} was created.", Course.class.getSimpleName(), model.getId());
        }
        return this.courseConvertor.toResponse(model);
    }
    
    /**
     * 更新操作
     *
     * @param id 更新资源的ID
     * @param courseDTO 更新资源的DTO
     * @return 更新后资源
     */
    @ApiOperation(value = "更新操作", notes = "")
    @PutMapping(value = "/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<CourseDTO> update(@PathVariable final Long id, @RequestBody @Valid final CourseDTO courseDTO) {
        courseDTO.setId(id);
        final Course model = this.courseConvertor.toModel(courseDTO);
        this.courseService.update(model);
        if (log.isInfoEnabled()) {
            log.info("{} instance {} was updated.", Course.class.getSimpleName(), model.getId());
        }
        return this.courseConvertor.toResponse(model);
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
        this.courseService.delete(id);
        if (log.isInfoEnabled()) {
            log.info("{} instance {} was deleted.", Course.class.getSimpleName(), id);
        }
        return RestResponse.success();
    }
}
