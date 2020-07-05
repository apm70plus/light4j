package com.light.web.response;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * Rest返回结果
 */
public class RestResponse<T> extends AbstractResponse {

    private static final long serialVersionUID = 1668914867578552488L;
    
    /**
     * 对象数据
     */
    @ApiModelProperty(value = "业务数据", position = 1)
    private T data;

    ///////////////////////////////////////
    // Getter
    ///////////////////////////////////////
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "data", index = 2)
    public T getData() {
        return this.data;
    }

    ///////////////////////////////////////
    // Setter
    ///////////////////////////////////////
    private void setData(final T data) {
        this.data = data;
    }

    ///////////////////////////////////////
    // Constructor
    ///////////////////////////////////////
    public RestResponse() {
    }

    ///////////////////////////////////////
    // Builder
    ///////////////////////////////////////
    public static RestResponse<Void> success() {
        final RestResponse<Void> result = new RestResponse<>();
        return result;
    }

    public static RestResponse<Void> failure(final ResponseError error) {
        final RestResponse<Void> result = new RestResponse<>();
        result.setError(error);
        return result;
    }

    public static <T> RestResponse<T> success(final T data) {
        final RestResponse<T> result = new RestResponse<>();
        result.setData(data);
        return result;
    }

    public static <T> RestResponse<T> failure(final T data, final ResponseError error) {
        final RestResponse<T> result = new RestResponse<>();
        result.setData(data);
        result.setError(error);
        return result;
    }
    
    public static <T> RestResponse<T> failure(final T data, final ResponseError error, HttpStatus status) {
        final RestResponse<T> result = new RestResponse<>();
        result.setData(data);
        result.setError(error);
        result.setStatus(status);
        return result;
    }
}
