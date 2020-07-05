package com.light.fileserver.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.light.jpa.domain.AssignIdEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 对象数据存储记录类
 */
@Entity
@Getter@Setter
public class StorageRecord extends AssignIdEntity<Long> {

    public enum ObjectType {
        file, audio, picture, video
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    protected ObjectType objectType;

    /**
     * 文件路径，包含访问文件的全部信息
     */
    @NotBlank
    @Length(max = 1024)
    @Column(length = 1024, nullable = false)
    private String filePath;

    /**
     * 文件原始名称
     */
    @NotBlank
    @Length(max = 255)
    @Column(length = 255, nullable = false)
    private String filename;

    /**
     * 文件大小
     */
    private Long fileSize = 0l;

    /**
     * 缩略图文件路径
     */
    @Length(max = 1024)
    @Column(length = 1024)
    private String thumbnailFilePath;

    /**
     * 原始文件路径
     */
    @NotBlank
    @Length(max = 1024)
    @Column(length = 1024, nullable = false)
    private String originalFilePath;

    /**
     * 播放时长, 单位秒
     */
    private Integer duration = 0;

    /**
     * 存储的服务器
     */
    private String storageServer;
}
