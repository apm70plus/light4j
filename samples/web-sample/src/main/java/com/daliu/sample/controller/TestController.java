package com.daliu.sample.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.light.exception.BusinessException;
import com.light.web.response.RestResponse;

@RestController
public class TestController {

	@GetMapping("/hello")
	public RestResponse<String> hello() {
		return RestResponse.success("world!");
	}
	
	@GetMapping("/exception")
	public RestResponse<String> exception() {
		throw new BusinessException("业务异常，数据不合法！");
	}
}
