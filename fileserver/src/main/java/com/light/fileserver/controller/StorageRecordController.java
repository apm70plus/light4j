package com.light.fileserver.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.light.fileserver.convertor.StorageRecordConvertor;
import com.light.fileserver.dto.StorageRecordDTO;
import com.light.fileserver.model.StorageRecord;
import com.light.fileserver.repository.StorageRecordRepository;
import com.light.fileserver.service.StorageRecordService;
import com.light.web.response.PageResponse;
import com.light.web.response.RestResponse;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * StorageRecord的管理接口
 *
 * @author auto
 */
@Slf4j
@Api(tags = {"文件存储记录的管理API" })
@RestController
@RequestMapping("/api/storageRecords")
public class StorageRecordController {
    @Autowired
    private StorageRecordService storageRecordService;
    @Autowired
    private StorageRecordRepository storageRecordRepository;
    @Autowired
    private StorageRecordConvertor storageRecordConvertor;

    /**
     * 取得分页数据
     *
     * @param pageable 分页+排序参数
     * @return 分页数据
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponse<StorageRecordDTO> page(final Pageable pageable) {
        final Page<StorageRecord> models = this.storageRecordRepository.findAll(pageable);
        return this.storageRecordConvertor.toResponse(models);
    }

    /**
     * 取得详细数据
     *
     * @param id 资源ID
     * @return 资源详细
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<StorageRecordDTO> get(@PathVariable final Long id) {
        final StorageRecord model = this.storageRecordService.getStorageRecord(id);
        return this.storageRecordConvertor.toResponse(model);
    }

    /**
     * 新建操作
     *
     * @param storageRecordDTO 新建资源的DTO
     * @return 新建资源
     */
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<StorageRecordDTO> create(@RequestBody @Valid final StorageRecordDTO storageRecordDTO) {
        final StorageRecord model = this.storageRecordConvertor.toModel(storageRecordDTO);
        this.storageRecordService.createStorageRecord(model);
        if (StorageRecordController.log.isInfoEnabled()) {
            StorageRecordController.log.info("{} instance {} was created.", StorageRecord.class.getSimpleName(),
                    model.getId());
        }
        return this.storageRecordConvertor.toResponse(model);
    }

    /**
     * 更新操作
     *
     * @param storageRecordDTO 更新资源的DTO
     * @return 更新后资源
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<StorageRecordDTO> update(@PathVariable final Long id,
            @RequestBody @Valid final StorageRecordDTO storageRecordDTO) {
        storageRecordDTO.setId(id);
        final StorageRecord model = this.storageRecordConvertor.toModel(storageRecordDTO);
        this.storageRecordService.updateStorageRecord(model);
        if (StorageRecordController.log.isInfoEnabled()) {
            StorageRecordController.log.info("{} instance {} was updated.", StorageRecord.class.getSimpleName(),
                    model.getId());
        }
        return this.storageRecordConvertor.toResponse(model);
    }

    /**
     * 删除操作
     *
     * @param Id 资源ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<Void> delete(@PathVariable final Long id) {
        this.storageRecordService.deleteStorageRecord(id);
        if (StorageRecordController.log.isInfoEnabled()) {
            StorageRecordController.log.info("{} instance {} was deleted.", StorageRecord.class.getSimpleName(), id);
        }
        return RestResponse.success();
    }
}
