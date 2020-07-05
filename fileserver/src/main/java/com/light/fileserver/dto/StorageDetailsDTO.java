package com.light.fileserver.dto;

import com.light.fileserver.model.StorageRecord.ObjectType;
import com.light.web.dto.AbstractDTO;

import lombok.Getter;
import lombok.Setter;

/**
 * 存储的详细信息
 *
 * @author liuyg
 */
@Getter@Setter
public class StorageDetailsDTO extends AbstractDTO {

    private static final long serialVersionUID = 5625417965803110556L;

    /**
     * 存储类型
     */
    private ObjectType type;

    /** 文件名称 */
    private String fileName;

    /** 文件大小 */
    private Long fileSize;

    /** 时长 */
    private Integer duration;
    /**
     * 缩略图Base64编码值
     */
    private String base64Thumbnail;

    /**
     * 图像宽度（像素）
     */
    private Integer thumbnailWidth;

    /**
     * 图像高度（像素）
     */
    private Integer thumbnailHeight;
}
