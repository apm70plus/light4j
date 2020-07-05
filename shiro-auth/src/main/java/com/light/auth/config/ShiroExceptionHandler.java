package com.light.auth.config;

import javax.servlet.ServletException;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.light.web.response.ResponseError;
import com.light.web.response.RestResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ShiroExceptionHandler {

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<Object> handleServletException(ServletException e) {
    	if (e.getRootCause() instanceof ShiroException) {
    		return handle401((ShiroException)e.getRootCause());
    	} else if (e instanceof HttpRequestMethodNotSupportedException) {
    		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(RestResponse.failure(ResponseError.of("无访问权限.").code("forbidden")));
    	} else {
    		log.error(e.getMessage(), e);
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(RestResponse.failure(ResponseError.of(e.getMessage())));
    	}
    }
	
    // 捕捉AuthenticationException
    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<Object> handle401(AuthenticationException e) {
    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RestResponse.failure(ResponseError.of(e.getMessage()).code("unauthorized")));
    }
    
    @ExceptionHandler({UnauthenticatedException.class})
    public ResponseEntity<Object> handle401(UnauthenticatedException e) {
    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RestResponse.failure(ResponseError.of(e.getMessage()).code("unauthorized")));
    }
    
    // 捕捉UnauthorizedException
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Object> handle403(UnauthorizedException e) {
    	return ResponseEntity.status(HttpStatus.FORBIDDEN).body(RestResponse.failure(ResponseError.of(e.getMessage()).code("forbidden")));
    }
    
    // 捕捉shiro的异常
    @ExceptionHandler(ShiroException.class)
    public ResponseEntity<Object> handle401(ShiroException e) {
    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RestResponse.failure(ResponseError.of(e.getMessage()).code("unauthorized")));
    }
}