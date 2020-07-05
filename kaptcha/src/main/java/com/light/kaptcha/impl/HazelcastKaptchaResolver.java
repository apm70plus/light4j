package com.light.kaptcha.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.light.kaptcha.IKaptchaResolver;
import com.light.kaptcha.KaptchaProperties;

/**
 * 分布式场景，hazelcast 实现验证码的缓存<br>
 * hazelcast需要提前调用 HazelcastKaptchaResolver.configCache 方法配置缓存
 * 
 * @author liuyg
 */
public class HazelcastKaptchaResolver implements IKaptchaResolver {

	private static final String CACHE_NAME = "HazelcastKaptchaResolver";

	@Autowired
	private HazelcastInstance hazelcastInstance;

	public static void configCache(com.hazelcast.config.Config hazelcastConfig, long captchaTimeout) {
		int timeout = captchaTimeout > 0 ? (int) captchaTimeout/1000 : (int) KaptchaProperties.DEFAULT_CAPTCHA_TIMEOUT / 1000;
		hazelcastConfig.getMapConfig(CACHE_NAME).setTimeToLiveSeconds(timeout);
	}

	@Override
	public boolean validCaptcha(String indentityKey, String scope, String capText) {
		if (StringUtils.isBlank(capText)) {
			return false;
		}
		String captchaKey = getCaptchaKey(indentityKey, scope);
		if (captchaKey == null) {
			return false;
		}
		String cachedCaptcha = captchaCache().get(captchaKey);
		if (cachedCaptcha == null) {
			return false;
		} else {
			captchaCache().remove(captchaKey);
			return capText.equalsIgnoreCase(cachedCaptcha);
		}
	}
	
	private String getCaptchaKey(String indentityKey, String scope) {
		return indentityKey + scope;
	}

	@Override
	public void setCaptcha(String indentityKey, String scope, String capText) {
		setCaptcha(indentityKey, scope, capText, 0l);
	}

	@Override
	public void setCaptcha(String indentityKey, String scope, String capText, long timeout) {
		String captchaKey = getCaptchaKey(indentityKey, scope);
		captchaCache().put(captchaKey, capText);
	}

	private IMap<String, String> captchaCache() {
		return hazelcastInstance.getMap(CACHE_NAME);
	}
}
