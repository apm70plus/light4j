package com.light.kaptcha.impl;

import lombok.Data;

@Data
public class Response {

	private String data;
	private int httpStatus;
	
	public boolean isSuccess() {
		return httpStatus >= 200 && httpStatus < 400;
	}
}
