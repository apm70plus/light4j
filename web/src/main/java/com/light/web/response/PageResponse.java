package com.light.web.response;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.light.exception.BusinessException;

import io.swagger.annotations.ApiModelProperty;

public class PageResponse<T> extends ListResponse<T> {

    private static final long serialVersionUID = -1575401489882908012L;
    /**
     * 分页信息
     */
    @ApiModelProperty(value = "分页信息", position = 2)
    private PageData page;

    ///////////////////////////////////////
    // Getter
    ///////////////////////////////////////
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "data", index = 2)
    @Override
    public List<T> getData() {
        return super.getData();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "page", index = 3)
    public PageData getPage() {
        return this.page;
    }

    ///////////////////////////////////////
    // Setter
    ///////////////////////////////////////
    public void setPage(final PageData page) {
        this.page = page;
    }

    ///////////////////////////////////////
    // Constructor
    ///////////////////////////////////////
    public PageResponse() {
    }

    ///////////////////////////////////////
    // Builder
    ///////////////////////////////////////
    public static <T> PageResponse<T> success(final Page<T> pageData) {
        if (pageData == null) {
            throw BusinessException.of("The formal parameter 'pageData' cannot be null");
        }

        final PageResponse<T> result = new PageResponse<>();
        result.setData(pageData.getContent());
        result.setPage(PageData.convert(pageData));
        return result;
    }

    public static <T> PageResponse<T> success(final List<T> listData, final Page<?> pageData) {
        if (pageData == null) {
            throw BusinessException.of("The formal parameter 'pageData' cannot be null");
        }

        final PageResponse<T> result = new PageResponse<>();
        result.setData(listData);
        result.setPage(PageData.convert(pageData));
        return result;
    }
    
    public static <T> PageResponse<T> failure(final Page<T> pageData, final ResponseError error) {
        final PageResponse<T> result = new PageResponse<>();
        if (pageData != null) {
            result.setData(pageData.getContent());
            result.setPage(PageData.convert(pageData));
        }
        result.setError(error);
        return result;
    }
    
    public static <T> PageResponse<T> failure(final Page<T> pageData, final ResponseError error, HttpStatus status) {
        final PageResponse<T> result = new PageResponse<>();
        if (pageData != null) {
            result.setData(pageData.getContent());
            result.setPage(PageData.convert(pageData));
        }
        result.setStatus(status);
        result.setError(error);
        return result;
    }
}
