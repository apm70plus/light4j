package com.light.oauth2.config;

import javax.servlet.ServletException;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ShiroExceptionHandler {

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<String> handleServletException(ServletException e) {
    	if (e.getRootCause() instanceof ShiroException) {
    		handle401((ShiroException)e.getRootCause());
    	} else {
    		ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    	}
    	return ResponseEntity.status(401).body(e.getMessage());
        //return RestResponse.failure(new ResultError("401", e.getMessage(), null));
    }
	
    // 捕捉shiro的异常
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public ResponseEntity<String> handle401(ShiroException e) {
    	return ResponseEntity.status(401).body(e.getMessage());
        //return RestResponse.failure(new ResultError("401", e.getMessage(), null));
    }

    // 捕捉UnauthorizedException
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handle401(UnauthorizedException e) {
    	return ResponseEntity.status(401).body(e.getMessage());
        //return RestResponse.failure(new ResultError("401", "Unauthorized", null));
    }
    
    // 捕捉UnauthorizedException
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handle401(AuthenticationException e) {
    	return ResponseEntity.status(401).body(e.getMessage());
        //return RestResponse.failure(new ResultError("401", "Unauthorized", null));
    }
}