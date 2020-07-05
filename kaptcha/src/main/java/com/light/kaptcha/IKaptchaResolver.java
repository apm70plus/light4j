package com.light.kaptcha;

public interface IKaptchaResolver {

	boolean validCaptcha(String indentityKey, String capText, String scope);

	void setCaptcha(String indentityKey, String scope, String capText);
	
	void setCaptcha(String indentityKey, String scope, String capText, long captchaTimeout);
}
