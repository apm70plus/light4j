package com.light.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 身份认证异常。
 * <p>
 * 用户未登录时，返回该异常
 *
 * @author liuyg
 */
public class AuthenticationException extends RuntimeException {
    private static final long serialVersionUID = 6774083802665621769L;

    /**
     * 业务异常编码（可结合i18n实现异常消息国际化）
     */
    private String code;
    /**
     * 可选的参数（i18n中需要动态替换的参数）
     */
    private Object[] params;
    
    public AuthenticationException() {}

    public AuthenticationException(String message) {
        super(message);
    }
    
    public static AuthenticationException of() {
    	return new AuthenticationException();
    }
    
    public static AuthenticationException of(String message) {
    	return new AuthenticationException(message);
    }
    
    @JsonIgnore
    public AuthenticationException code(String code) {
        this.code = code;
        return this;
    }

    @JsonIgnore
    public AuthenticationException params(Object... params) {
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
