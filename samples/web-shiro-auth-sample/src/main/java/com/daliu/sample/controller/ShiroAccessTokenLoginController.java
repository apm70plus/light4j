package com.daliu.sample.controller;

import com.light.auth.bean.LoginDTO;
import com.light.auth.bean.LoginSuccessDTO;
import com.light.auth.bean.LoginUser;
import com.light.auth.bean.UsernamePasswordToken;
import com.light.auth.token.AccessTokenService;
import com.light.auth.util.LoginUserUtils;
import com.light.web.response.ResponseError;
import com.light.web.response.RestResponse;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基于AccessToken的认证方式
 */
@RestController
@ConditionalOnProperty(name = "light.auth.type",  havingValue = "accessToken", matchIfMissing = false)
public class ShiroAccessTokenLoginController {
	
	@Autowired
	private AccessTokenService accessTokenService;
	
	@PostMapping(value="/login", produces = "application/json")
	@ApiOperation(value = "登录接口", notes = "用户名、密码登录")
	public RestResponse<LoginSuccessDTO> login(@RequestBody LoginDTO login) {
		UsernamePasswordToken token = new UsernamePasswordToken(login.getUsername(), login.getPassword());
		Subject subject = SecurityUtils.getSubject();
		subject.login(token);
		LoginUser user = LoginUserUtils.getLoginUser();
	    AccessTokenService.AccessToken accessToken = accessTokenService.allocateToken(user);
		LoginSuccessDTO dto = new LoginSuccessDTO();
		dto.setUserId(user.getId());
		dto.setUsername(user.getName());
		dto.setToken(accessToken);
		
		return RestResponse.success(dto);
	}
	
	@GetMapping(value="/unauthenticated", produces = "application/json")
	@ApiOperation(value = "未认证的回调接口", notes = "")
	public ResponseEntity<RestResponse<Void>> unauthenticated() {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RestResponse.failure(ResponseError.of("请先登录再访问服务.")));
	}
	
	@GetMapping(value="/logout", produces = "application/json")
	@ApiOperation(value = "退出登录接口", notes = "")
	public ResponseEntity<RestResponse<String>> logout() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return ResponseEntity.ok(RestResponse.success("logout."));
	}
}
