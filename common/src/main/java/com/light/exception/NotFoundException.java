package com.light.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 无效的请求异常。<br>
 * 用户发送的请求有错误，服务器拒绝响应该请求
 * 
 * @author liuyg
 */
public class NotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
    /**
     * 业务异常编码（可结合i18n实现异常消息国际化）
     */
    private String code;
    /**
     * 可选的参数（i18n中需要动态替换的参数）
     */
    private Object[] params;
    
	public NotFoundException() {}
	
	public NotFoundException(final String message) {
        super(message);
    }
	
	public static NotFoundException of(String message) {
		return new NotFoundException(message);
	}
	
	public static NotFoundException of() {
		return new NotFoundException();
	}
	
	@JsonIgnore
	public NotFoundException code(String code) {
		this.code = code;
		return this;
	}
	
	@JsonIgnore
	public NotFoundException params(Object... params) {
		this.params = params;
		return this;
	}
	
	public String getCode() {
		return code;
	}
	
	public Object[] getParams() {
		return params;
	}
}
