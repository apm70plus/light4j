package com.light.fileserver.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter@Setter
public class Base64ImageDTO {
    @NotNull
    @ApiModelProperty(value="Base64文本内容")
    private String base64;
    @ApiModelProperty(value="图片扩展名，默认jpg")
    private String extension = "jpg";
}
