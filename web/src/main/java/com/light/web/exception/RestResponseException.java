package com.light.web.exception;

import org.springframework.http.HttpStatus;

import com.light.web.response.RestResponse;

/**
 * Rest请求结果运行时异常<br>
 * 主要为了包装RestResponse中的异常
 *
 * @author liuyg
 */
public class RestResponseException extends RuntimeException {

    private static final long serialVersionUID = 7913293827793560253L;

    private RestResponse<Void> response;
    
    private HttpStatus status;

    public RestResponseException(RestResponse<Void> response, HttpStatus status) {
        super();
        this.status = status;
        this.response = response;
    }

    public RestResponse<Void> getBody() {
        return this.response;
    }

    public HttpStatus getStatus() {
        return this.status;
    }
}
