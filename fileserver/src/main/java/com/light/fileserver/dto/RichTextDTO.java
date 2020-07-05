package com.light.fileserver.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter@Setter
public class RichTextDTO {
    @NotNull
    @ApiModelProperty(value="文本内容")
    private String text;
}
