package com.light.fileserver.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.light.fileserver.model.StorageRecord.ObjectType;
import com.light.web.dto.AbstractDTO;

import lombok.Getter;
import lombok.Setter;

/**
 * 对象数据存储记录类
 */
@Getter@Setter
public class StorageRecordDTO extends AbstractDTO {

    private static final long serialVersionUID = -1538901115520003737L;

    @NotNull
    protected ObjectType objectType;

    /**
     * 文件路径，包含访问文件的全部信息
     */
    @NotBlank
    @Length(max = 1024)
    private String filePath;

    /**
     * 文件原始名称
     */
    @NotBlank
    @Length(max = 255)
    private String filename;

    /**
     * 文件大小
     */
    private Long fileSize = 0l;

    /**
     * 缩略图文件路径，包含访问文件的全部相对信息
     */
    @Length(max = 1024)
    private String thumbnailFilePath;

    /**
     * 原始文件路径，包含访问文件的全部相对信息
     */
    @NotBlank
    @Length(max = 1024)
    private String originalFilePath;

    /**
     * 播放时长, 单位秒
     */
    private Integer duration = 0;
}
