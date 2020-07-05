package com.light.kaptcha;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.code.kaptcha.Producer;

public class KaptchaService {
	
	private static KaptchaService instance;
	
	private char[] numbers = new char[]{'0','1','2','3','4','5','6','7','8','9'};
	
	public static KaptchaService instance() {
		return instance;
	}
	
	static void init(Producer kaptchaProducer, IKaptchaResolver kaptchaResolver) {
		if (instance == null) {
			instance = new KaptchaService();
			instance.kaptchaProducer = kaptchaProducer;
			instance.kaptchaResolver = kaptchaResolver;
		}
	}
	
	private KaptchaService() {}

	@Autowired
	private Producer kaptchaProducer;
	@Autowired
	private IKaptchaResolver kaptchaResolver;
	
	/**
	 * 创建验证码文本（数字字母组合）
	 * @param indentityKey
	 * @param scope
	 * @return
	 */
	public String createCaptchaText(String indentityKey, String scope) {
		String capText = kaptchaProducer.createText();
		kaptchaResolver.setCaptcha(indentityKey, scope, capText);
		return capText;
	}
	
	/**
	 * 创建短信验证码（6位数字）
	 * @param indentityKey
	 * @param scope
	 * @return
	 */
	public String createSmsCaptcha(String indentityKey, String scope) {
		char[] chars = new char[6];
		for(int i = 0; i < 6; i++) {
			int random = (int)(Math.random() * 10);
			chars[i] = numbers[random];
		}
		String smsCaptcha = new String(chars);
		kaptchaResolver.setCaptcha(indentityKey, scope, smsCaptcha);
		return smsCaptcha;
	}
	
	public boolean validCaptcha(String indentityKey, String scope, String captchaText) {
		return kaptchaResolver.validCaptcha(indentityKey, scope, captchaText);
	}
}
