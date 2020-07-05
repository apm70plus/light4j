package com.light.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 授权异常，一般无效的授权或无权访问资源时，抛出该异常
 * @author liuyg
 *
 */
public class AuthorizationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

    /**
     * 业务异常编码（可结合i18n实现异常消息国际化）
     */
    private String code;
    /**
     * 可选的参数（i18n中需要动态替换的参数）
     */
    private Object[] params;

    public AuthorizationException() {}
    
    public AuthorizationException(String message) {
        super(message);
    }
    
    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static AuthorizationException of() {
    	return new AuthorizationException();
    }
    
    public static AuthorizationException of(String message) {
    	return new AuthorizationException(message);
    }
    
    public static AuthorizationException of(Throwable cause) {
    	return new AuthorizationException(null, cause);
    }
    
    public static AuthorizationException of(String message, Throwable cause) {
    	return new AuthorizationException(message, cause);
    }

    @JsonIgnore
    public AuthorizationException code(String code) {
        this.code = code;
        return this;
    }

    @JsonIgnore
    public AuthorizationException params(Object... params) {
        this.params = params;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public Object[] getParams() {
        return this.params;
    }
}
