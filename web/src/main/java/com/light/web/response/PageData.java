package com.light.web.response;

import java.io.Serializable;

import org.springframework.data.domain.Page;

import io.swagger.annotations.ApiModelProperty;

/**
 * 分页数据
 *
 * @author liuyg
 */
class PageData implements Serializable {

    private static final long serialVersionUID = 8841982643079227096L;

    public static PageData convert(final Page<?> page) {
        final PageData pageData = new PageData();
        pageData.setNumber(page.getNumber());
        pageData.setRows(page.getNumberOfElements());
        pageData.setSize(page.getSize());
        pageData.setTotal(page.getTotalElements());
        return pageData;
    }

    /**
     * 当前页码（从0开始）
     */
    @ApiModelProperty(value = "当前页（从0开始）", position = 1)
    private int number;
    /**
     * 查询结果条数
     */
    @ApiModelProperty(value = "返回条数", position = 2)
    private int rows;
    /**
     * 总条数
     */
    @ApiModelProperty(value = "总条数", position = 3)
    private long total;
    /**
     * 页大小
     */
    @ApiModelProperty(value = "页SIZE", position = 4)
    private int size;

    public long getTotal() {
        return this.total;
    }

    public void setTotal(final long total) {
        this.total = total;
    }

    public int getRows() {
        return this.rows;
    }

    public void setRows(final int rows) {
        this.rows = rows;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(final int size) {
        this.size = size;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(final int page) {
        this.number = page;
    }
}
