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

import com.daliu.sample.convertor.StudentConvertor;
import com.daliu.sample.dto.StudentDTO;
import com.daliu.sample.model.Student;
import com.daliu.sample.service.StudentService;
import com.daliu.sample.repository.StudentRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * Student的管理接口
 *
 * @author auto
 */
@Slf4j
@RestController
@RequestMapping("/w/students")
@Api(tags = {"Student管理API" })
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentConvertor studentConvertor;

    /**
     * 获取分页数据
     *
     * @param pageable 分页+排序参数
     * @return 分页数据
     */
    @ApiPageRSQL
    @ApiOperation(value = "获取分页数据", notes = "")
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponse<StudentDTO> search(final Pageable pageable, @RsqlQuery(root=Student.class) Optional<Predicate> predicate) {
        final Page<Student> models = this.studentRepository.findAll(predicate.orElseGet(() -> null), pageable);
        return this.studentConvertor.toResponse(models);
    }

    /**
     * 取得详细数据
     *
     * @param id 资源ID
     * @return 资源详细
     */
    @ApiOperation(value = "获取详细数据", notes = "")
    @GetMapping(value = "/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<StudentDTO> get(@PathVariable final Long id) {
        final Student model = this.studentService.get(id);
        return this.studentConvertor.toResponse(model);
    }

    /**
     * 新建操作
     *
     * @param studentDTO 新建资源的DTO
     * @return 新建资源
     */
    @ApiOperation(value = "新建操作", notes = "")
    @PostMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<StudentDTO> create(@RequestBody @Valid final StudentDTO studentDTO) {
        final Student model = this.studentConvertor.toModel(studentDTO);
        this.studentService.create(model);
        if (log.isInfoEnabled()) {
            log.info("{} instance {} was created.", Student.class.getSimpleName(), model.getId());
        }
        return this.studentConvertor.toResponse(model);
    }
    
    /**
     * 更新操作
     *
     * @param id 更新资源的ID
     * @param studentDTO 更新资源的DTO
     * @return 更新后资源
     */
    @ApiOperation(value = "更新操作", notes = "")
    @PutMapping(value = "/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<StudentDTO> update(@PathVariable final Long id, @RequestBody @Valid final StudentDTO studentDTO) {
        studentDTO.setId(id);
        final Student model = this.studentConvertor.toModel(studentDTO);
        this.studentService.update(model);
        if (log.isInfoEnabled()) {
            log.info("{} instance {} was updated.", Student.class.getSimpleName(), model.getId());
        }
        return this.studentConvertor.toResponse(model);
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
        this.studentService.delete(id);
        if (log.isInfoEnabled()) {
            log.info("{} instance {} was deleted.", Student.class.getSimpleName(), id);
        }
        return RestResponse.success();
    }
}
