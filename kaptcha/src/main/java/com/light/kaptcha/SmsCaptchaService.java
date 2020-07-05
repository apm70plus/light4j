package com.light.kaptcha;

import com.light.kaptcha.impl.Response;

/**
 * 短信验证码服务
 * 
 * @author liuyg
 *
 */
public interface SmsCaptchaService {
	/**
	 * 发送短信验证给指定的手机号码
	 *
	 * @param mobile 接收验证码的手机号码
	 * @param scope  范围
	 * @param template 短信模板
	 */
	Response sendSmsCaptcha(String mobile, String scope, String template);

	/**
	 * 校验验证码
	 *
	 * @param mobile
	 * @param scope
	 * @param captcha
	 */
	boolean validSmsCaptcha(String mobile, String scope, String smsCaptcha);
}
