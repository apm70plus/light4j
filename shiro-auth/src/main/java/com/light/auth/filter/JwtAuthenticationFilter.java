package com.light.auth.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import com.light.auth.authc.JwtToken;
import com.light.auth.authc.Oauth2Constants;
import com.light.util.JsonUtils;
import com.light.web.response.ResponseError;
import com.light.web.response.RestResponse;

public class JwtAuthenticationFilter extends AccessControlFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		Subject subject = getSubject(request, response);
		if (null != subject && subject.isAuthenticated()) {
            return true;//已经认证过直接放行
        }
        return false;//转到拒绝访问处理逻辑
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		if(!isJwtRequest(request)){//如果不是OAuth2鉴权的请求
			//WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
			String json = JsonUtils.pojoToJson(RestResponse.failure(ResponseError.of("未登录").code("unauthorized")));
	        WebUtils.toHttp(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        WebUtils.toHttp(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
	        WebUtils.toHttp(response).getWriter().write(json);
			return false;
		}
        //创建令牌
        AuthenticationToken token = createToken(request, response);
        try {
            getSubject(request, response).login(token);//认证
            return true;//认证成功，过滤器链继续
        } catch (AuthenticationException e) {//认证失败，发送401状态并附带异常信息
            String json = JsonUtils.pojoToJson(RestResponse.failure(ResponseError.of(e.getMessage()).code("unauthorized")));
            WebUtils.toHttp(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            WebUtils.toHttp(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            WebUtils.toHttp(response).getWriter().write(json);
            return false;
        }
	}

	private AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		return new JwtToken(getAuthzToken(request));
	}

	private boolean isJwtRequest(ServletRequest request) {
		return !StringUtils.isEmpty(getAuthzToken(request));
	}

	private String getAuthzToken(ServletRequest request) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        httpRequest.getHeaderNames();
        String token = httpRequest.getHeader(Oauth2Constants.AUTHORIZATION_HEADER);
        if (token != null && token.length() > 7) { 
        	return token.substring("bearer ".length());
        } else {
        	Cookie[] cookies = httpRequest.getCookies();
        	if (cookies == null) {
        		return null;
        	}
        	for (Cookie cookie : cookies) {
        		if (Oauth2Constants.AUTHORIZATION_HEADER.equals(cookie.getName())) {
        			return cookie.getValue();
        		}
        	}
        	return null;
        }
    }
}
