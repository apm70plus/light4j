package com.light.fileserver.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.light.fileserver.enums.NormalizationType;
import com.light.fileserver.model.StorageRecord;

/**
 * 存储服务
 *
 * @author liuyg
 */
public interface StorageService {

    /**
     * 获取本地文件，支持数据文件标准化操作。
     *
     * @param filePath 文件路径
     * @return 文件
     */
    File getFile(String filePath);

    /**
     * 获取文件存储记录
     *
     * @param no
     * @return
     */
    StorageRecord getFileStorageRecord(Long id);

    /**
     * 批量获取文件存储记录，返回压缩文件
     *
     * @param no
     * @return
     */
    File getFileStorageRecordsTo7Zip(List<Long> ids, String filePath);

    /**
     * 在本地创建临时文件
     *
     * @param extension 文件扩展名
     * @return 临时文件
     */
    File createLocalTempFile(final String extension);

    /**
     * 保存文件
     *
     * @param file 文件
     * @return 文件存储记录
     */
    StorageRecord save(File file);

    /**
     * 保存HTTP上传的文件内容到本地存储<br/>
     *
     * @param file HTTP请求的文件内容
     * @return 文件相对路径
     */
    StorageRecord save(MultipartFile file);

    /**
     * 分片文件保存
     *
     * @param files
     * @param originalFilename
     * @return
     */
    StorageRecord save(File[] files, String originalFilename, long fileTotalSize);

    /**
     * 删除后端存储的文件
     *
     * @param fileId 文件ID
     */
    void delete(Long fileId);

    /**
     * 对存储的文件执行标准化处理
     *
     * @param fileId 存储ID
     * @param sync 是否同步处理
     * @throws IOException
     */
    StorageRecord normalize(Long fileId, boolean sync);

    /**
     * 合成多张图片为一张图片，最多支持九宫格样式（九张图合成）
     *
     * @param imagePath
     * @return
     */
    StorageRecord createCombinationImage(List<Long> imageIds, NormalizationType type);

}
