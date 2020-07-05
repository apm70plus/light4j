package com.light.fileserver.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.light.fileserver.convertor.StorageRecordConvertor;
import com.light.fileserver.dto.Base64ImageDTO;
import com.light.fileserver.dto.RichTextDTO;
import com.light.fileserver.dto.StorageRecordDTO;
import com.light.fileserver.enums.NormalizationType;
import com.light.fileserver.model.StorageRecord;
import com.light.fileserver.service.FileService;
import com.light.web.response.ListResponse;
import com.light.web.response.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 本地存储的上传、下载接口
 *
 * @author liuyg
 */
@Api(tags = {"文件上传、下载API" })
@RestController
public class LocalStoreController {

    @Autowired
    private FileService fileService;
    @Autowired
    private StorageRecordConvertor storageRecordConvertor;

    /**
     * 文件上传
     * @param request
     * @param normalize
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "文件上传", notes = "基于multipart/form-data的文件上传")
    @RequestMapping(value = "/api/files", method = RequestMethod.POST)
    public ListResponse<StorageRecordDTO> uploadFiles(final HttpServletRequest request, @RequestParam(defaultValue="false") Boolean normalize)
            throws IOException {
        final List<StorageRecord> records = this.fileService.uploadMultipartFiles(request, normalize);
        return this.storageRecordConvertor.toResponse(records);
    }
    
    /**
     * Base64图片上传
     * @param dto
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "Base64图片上传", notes = "基于Base64文本格式的图片文件上传")
    @RequestMapping(value = "/api/base64Images", method = RequestMethod.POST)
    public RestResponse<StorageRecordDTO> uploadBase64Image(@RequestBody @Valid Base64ImageDTO dto)
            throws IOException {
        final StorageRecord record = this.fileService.uploadBase64Image(dto);
        return this.storageRecordConvertor.toResponse(record);
    }
    
    /**
     * 富文本上传
     * @param dto
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "富文本上传", notes = "富文本上传")
    @RequestMapping(value = "/api/richTexts", method = RequestMethod.POST)
    public RestResponse<StorageRecordDTO> uploadRichText(@RequestBody @Valid RichTextDTO dto)
            throws IOException {
        final StorageRecord record = this.fileService.uploadRichText(dto);
        return this.storageRecordConvertor.toResponse(record);
    }

    /**
     * 文件下载
     * 
     * @param id 文件ID
     * @param type 
     * @param filename 文件名（非必须，若非空则替换原文件名）
     * @param request
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "文件下载", notes = "文件名若非空，则替换原文件名下载")
    @RequestMapping(value = "/api/files/{id}", method = RequestMethod.GET)
    public void downloadFile(
            @PathVariable final Long id,
            @RequestParam(required = false) final NormalizationType type,
            @RequestParam(required = false) final String filename,
            final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        this.fileService.downloadFile(id, type, filename, request, response);
    }
    
    /**
     * 图片以Base64格式下载
     * 
     * @param id 文件ID
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "图片以Base64格式下载", notes = "")
    @RequestMapping(value = "/api/base64Image/{id}", method = RequestMethod.GET)
    public void downloadBase64Image(
            @PathVariable final Long id,
            final HttpServletResponse response) throws IOException {
        this.fileService.downloadBase64Image(id, response);
    }
    
    /**
     * 富文本下载
     * 
     * @param id 文件ID
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "富文本下载", notes = "")
    @RequestMapping(value = "/api/richText/{id}", method = RequestMethod.GET)
    public void downloadRichText(
            @PathVariable final Long id,
            final HttpServletResponse response) throws IOException {
        this.fileService.downloadRichText(id, response);
    }

    /**
     * 批量压缩下载
     *
     * @param ids
     * @param type
     * @param fileName
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "文件批量下载", notes = "批量文件，压缩到7Z包后下载")
    @RequestMapping(value = "/api/files/tozip", method = RequestMethod.GET)
    public void downloads(
            @RequestParam final List<Long> ids,
            @RequestParam(required = false) final String fileName,
            final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        this.fileService.downloadToZip(ids, fileName, request, response);
    }
}
