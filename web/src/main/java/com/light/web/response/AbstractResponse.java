package com.light.web.response;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
public abstract class AbstractResponse implements Serializable {

    private static final long serialVersionUID = 3874081007408979058L;
    /**
     * HttpStatus
     */
    @JsonIgnore
    @Getter@Setter
    private HttpStatus status;
    
    /**
     * 错误消息
     */
    @ApiModelProperty(value = "异常信息", position = 10)
    @JsonInclude(Include.NON_NULL)
    @Getter@Setter
    protected ResponseError error;
    
    @JsonIgnore
    public boolean isFailure() {
        return this.error != null;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return !isFailure();
    }
}
