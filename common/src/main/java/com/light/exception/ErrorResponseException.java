package com.light.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ErrorResponseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	@Getter
	private final int status;
	@Getter
	private final String error;
	
	
}
