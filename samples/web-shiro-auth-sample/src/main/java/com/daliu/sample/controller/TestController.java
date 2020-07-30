package com.daliu.sample.controller;

import com.light.auth.util.LoginUserUtils;
import com.light.web.response.RestResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@GetMapping(value="/test", produces = "application/json")
	public RestResponse<String> testGet() {
		return RestResponse.success("hello " + LoginUserUtils.getLoginUser().getName());
	}

	@PostMapping(value="/test", produces = "application/json")
	public RestResponse<String> testPost() {
		return RestResponse.success("hello " + LoginUserUtils.getLoginUser().getName());
	}
}
