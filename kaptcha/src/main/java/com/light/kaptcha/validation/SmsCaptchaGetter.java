package com.light.kaptcha.validation;

/**
 * 手机验证码DTO
 * @author liuyg
 *
 */
public interface SmsCaptchaGetter {

	String getMobile();
	
    String getCaptcha();
}
