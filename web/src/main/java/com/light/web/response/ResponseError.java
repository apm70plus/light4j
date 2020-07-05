package com.light.web.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

/**
 * 业务类异常<br>
 * <p>
 * 考虑到业务类异常，HttpResponse的标准State往往不能完全表述业务意图，所以引入该异常类进行扩展和封装，<br>
 * 同时也为了方便Spring的ResponseEntity对异常信息的提取，异常信息会与正常数据一起封装在RestResponse里。
 *
 * @author liuyg
 */
public class ResponseError implements Serializable {

    private static final long serialVersionUID = 8042733799291115991L;

    /**
     * 异常消息详细描述
     */
    @ApiModelProperty(value = "异常消息", position = 1)
    private String message;
    /**
     * 异常编码
     */
    @ApiModelProperty(value = "异常编码", position = 0)
    private String code;

    public ResponseError() {
    }
    
    public static ResponseError of(String message) {
    	ResponseError error = new ResponseError();
    	error.message = message;
    	return error;
    }
    
    @JsonIgnore
    public ResponseError code(String code) {
    	this.code = code;
    	return this;
    }
    
    public ResponseError(final String message) {
        this.message = message;
    }

    public ResponseError(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(final String errcode) {
        this.code = errcode;
    }

    public void setMessage(final String errmsg) {
        this.message = errmsg;
    }

    @Override
    public String toString() {
        return String.format("{code:%s, message:%s}", this.code, this.message);
    }
}
