package com.light.oauth2.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.light.exception.AuthorizationException;
import com.light.oauth2.Oauth2Constants;
import com.light.oauth2.authc.UsernamePasswordToken;
import com.light.oauth2.bean.LoginUser;
import com.light.oauth2.bean.TokenBean;
import com.light.oauth2.dto.LoginDTO;
import com.light.oauth2.service.Oauth2Service;
import com.light.web.response.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 用户的登录、登出接口
 *
 * @author auto
 */
@RestController
@Api(tags = {"用户的登录、登出接口" })
public class LoginController {
	
	@Autowired
	private Oauth2Service oauth2Service;
	
	@PostMapping(value="/login", produces = "application/json")
	@ApiOperation(value = "用户名、密码登录", notes = "")
	public RestResponse<LoginUser> login(@Valid LoginDTO dto, HttpServletResponse response) {
		UsernamePasswordToken token = new UsernamePasswordToken(dto.getUsername(), dto.getPassword());
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
		} catch (Exception e) {
			throw AuthorizationException.of("用户名密码错误").code("invalid_password");
		}
		LoginUser user = (LoginUser)subject.getPrincipal();
		TokenBean accessToken = oauth2Service.createAccessToken(user.getId(), "WEB", Oauth2Constants.USER_SELF_SCOPE);
		Cookie cookie = new Cookie(Oauth2Constants.AUTHORIZATION_HEADER, accessToken.getToken());
		cookie.setHttpOnly(true);
		cookie.setMaxAge(accessToken.getTimeout());
		//cookie.setDomain(domain);
		response.addCookie(cookie);
		return RestResponse.success(user);
	}
	
	/**
	 * 退出登录
	 * @param accessToken
	 * @return
	 */
	@GetMapping(value="/logout", produces = "application/json")
	public RestResponse<String> logout() {
		SecurityUtils.getSubject().logout();
		return RestResponse.success("logout.");
	}
}
