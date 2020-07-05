package com.light.fileserver.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.light.fileserver.dto.Base64ImageDTO;
import com.light.fileserver.dto.RichTextDTO;
import com.light.fileserver.enums.NormalizationType;
import com.light.fileserver.model.StorageRecord;

/**
 * 文件服务，处理文件的上传、下载、标准化处理等
 * @author liuyg
 *
 */
public interface FileService {
    
    /**
     * Multipart表单文件上传
     * @param request
     * @param normalize 是否执行标准化处理（对图片制作标准图、缩略图）
     * @return
     * @throws IOException
     */
    List<StorageRecord> uploadMultipartFiles(final HttpServletRequest request, boolean normalize)
    		throws IOException;
    
    /**
     * Base64格式的图片上传
     * @param dto
     * @return
     */
    StorageRecord uploadBase64Image(Base64ImageDTO dto);
    
    /**
     * 富文本上传
     * @param dto
     * @return
     * @throws IOException
     */
    StorageRecord uploadRichText(RichTextDTO dto) throws IOException;

    /**
     * 文件批量打包下载
     *
     * @param ids
     * @param type
     * @param fileName
     * @param request
     * @param response
     * @throws IOException
     */
    public void downloadToZip(
            final List<Long> ids,
            final String fileName,
            final HttpServletRequest request,
            final HttpServletResponse response) throws IOException;

    /**
     * 文件下载
     *
     * @param id
     * @param type
     * @param fileName
     * @param request
     * @param response
     * @throws IOException
     */
    void downloadFile(
            final Long id,
            final NormalizationType type,
            final String fileName,
            final HttpServletRequest request,
            final HttpServletResponse response) throws IOException;

    /**
     * Base64格式下载图片
     * @param id
     * @param response
     * @throws IOException
     */
    void downloadBase64Image(final Long id, final HttpServletResponse response)
            throws IOException;

    /**
     * 富文本下载
     * @param id
     * @param response
     * @throws IOException
     */
    void downloadRichText(final Long id, final HttpServletResponse response)
    		throws IOException;
}
