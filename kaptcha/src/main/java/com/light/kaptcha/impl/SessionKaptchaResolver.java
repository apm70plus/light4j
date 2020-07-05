package com.light.kaptcha.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;

import com.light.kaptcha.IKaptchaResolver;
import com.light.kaptcha.KaptchaProperties;

public class SessionKaptchaResolver implements IKaptchaResolver {
	
	/**
     * 验证码有效期；单位（毫秒），默认 60000
     */
	private long captchaTimeout = KaptchaProperties.DEFAULT_CAPTCHA_TIMEOUT;
	
	public void init(long captchaTimeout) {
		if(captchaTimeout > 0) {
			this.captchaTimeout = captchaTimeout;
		}
	}
	@Override
	public boolean validCaptcha(String indentityKey, String scope, String capText) {
		// 验证码无效
		if (StringUtils.isEmpty(capText)) {
			return false;
		}
		// 历史验证码无效
		String sessionCapText = (String) getSessionAttribute(indentityKey, scope, "captcha");
		if (StringUtils.isEmpty(sessionCapText)) {
			return false;
		}
		// 检查验证码是否过期
		Date sessionCapDate = (Date) getSessionAttribute(indentityKey, scope, "captcha");
		if (new Date().getTime() - sessionCapDate.getTime() > captchaTimeout) {
			return false;
		}
		return StringUtils.equalsIgnoreCase(sessionCapText, capText);
	}

	@Override
	public void setCaptcha(String indentityKey, String scope, String capText) {
		setCaptcha(indentityKey, scope, capText, captchaTimeout);
	}

	@Override
	public void setCaptcha(String indentityKey, String scope, String capText, long captchaTimeout) {
		setSessionAttribute(indentityKey, scope, "captcha", capText);
		setSessionAttribute(indentityKey, scope, "expire", new Date(System.currentTimeMillis() + captchaTimeout));
	}
	
	private Object getSessionAttribute(String indentityKey, String scope, String attribute) {
		String sessionKey = indentityKey + scope + attribute;
		return RequestContextHolder.getRequestAttributes().getAttribute(sessionKey, 1);
	}
	
	private void setSessionAttribute(String indentityKey, String scope, String attribute, Object value) {
		String sessionKey = indentityKey + scope + attribute;
		RequestContextHolder.getRequestAttributes().setAttribute(sessionKey, value, 1);
	}
}
