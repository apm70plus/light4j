package com.light.web.response;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class ListResponse<T> extends AbstractResponse {

    private static final long serialVersionUID = 8756487352760469154L;
    /**
     * 列表数据
     */
    @ApiModelProperty(value = "业务数据（List）", position = 1)
    protected List<T> data;

    ///////////////////////////////////////
    // Getter
    ///////////////////////////////////////
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "data", index = 3)
    public List<T> getData() {
        return this.data;
    }

    ///////////////////////////////////////
    // Setter
    ///////////////////////////////////////
    public void setData(final List<T> data) {
        this.data = data;
    }

    ///////////////////////////////////////
    // Constructor
    ///////////////////////////////////////
    public ListResponse() {
    }

    ///////////////////////////////////////
    // Builder
    ///////////////////////////////////////
    public static <T> ListResponse<T> success(final List<T> listData) {
        final ListResponse<T> result = new ListResponse<>();
        if (listData == null) {
            result.setData(Collections.emptyList());
        } else {
        	result.setData(listData);
        }
        return result;
    }

    public static <T> ListResponse<T> failure(final List<T> listData, final ResponseError error) {
        final ListResponse<T> result = new ListResponse<>();
        result.setData(listData);
        result.setError(error);
        return result;
    }
    
    public static <T> ListResponse<T> failure(final List<T> listData, final ResponseError error, HttpStatus status) {
        final ListResponse<T> result = new ListResponse<>();
        result.setData(listData);
        result.setError(error);
        result.setStatus(status);
        return result;
    }
}
