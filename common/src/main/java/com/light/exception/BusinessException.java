package com.light.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 运行时业务异常类<br>
 * 使用场景：<br>
 * 根据业务逻辑判定，要立刻终止当前的处理，并回滚当前数据库事务，此时可以抛出该异常
 *
 * @author liuyg
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = -6882178806561789418L;

    /**
     * 业务异常编码（可结合i18n实现异常消息国际化）
     */
    private String code;
    /**
     * 可选的参数（i18n中需要动态替换的参数）
     */
    private Object[] params;

    public BusinessException() {}
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static BusinessException of() {
    	return new BusinessException();
    }
    
    public static BusinessException of(String message) {
    	return new BusinessException(message);
    }
    
    public static BusinessException of(Throwable cause) {
    	return new BusinessException(null, cause);
    }
    
    public static BusinessException of(String message, Throwable cause) {
    	return new BusinessException(message, cause);
    }

    @JsonIgnore
    public BusinessException code(String code) {
        this.code = code;
        return this;
    }

    @JsonIgnore
    public BusinessException params(Object... params) {
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
