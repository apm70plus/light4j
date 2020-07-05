package com.light.kaptcha;

import java.util.Properties;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.servlet.KaptchaServlet;
import com.google.code.kaptcha.util.Config;
import com.light.kaptcha.impl.AliSmsCaptchaServiceImpl;
import com.light.kaptcha.impl.HazelcastKaptchaResolver;
import com.light.kaptcha.impl.SessionKaptchaResolver;
import com.light.kaptcha.servlet.ExtendKaptchaServlet;

@Configuration
@ConditionalOnClass({ KaptchaServlet.class })
@EnableConfigurationProperties(KaptchaProperties.class)
public class KaptchaAutoConfiguration {

	@Value("${framework.kaptcha.resolver:session}")
	private String resolver;
	
	@Bean
	@ConditionalOnMissingBean(IKaptchaResolver.class)
	public IKaptchaResolver kaptchaResolver(KaptchaProperties properties) {
		if ("session".equals(resolver)) {
			SessionKaptchaResolver kaptchaResolver = new SessionKaptchaResolver(); 
			// 初始化参数
			kaptchaResolver.init(properties.getCaptchaTimeout());
			return kaptchaResolver;
		} else {
			return new HazelcastKaptchaResolver();
		}
	}
	
	// 验证码
	@Bean
	@ConditionalOnMissingBean(name = "kaptchaServlet")
	public ServletRegistrationBean<ExtendKaptchaServlet> servletRegistrationBean(KaptchaProperties properties,IKaptchaResolver kaptchaResolver, Producer kaptchaProducer) throws ServletException {
		ServletRegistrationBean<ExtendKaptchaServlet> registrationBean = new ServletRegistrationBean<ExtendKaptchaServlet>();
		ExtendKaptchaServlet kaptchaServlet = new ExtendKaptchaServlet(kaptchaResolver, kaptchaProducer, KaptchaProperties.DEFAULT_SCOPE);
		registrationBean.setServlet(kaptchaServlet);
		registrationBean.addUrlMappings(properties.getPattern());
		return registrationBean;
	}
	
	@Bean
	public Producer kaptchaProducer(KaptchaProperties properties) {
		Properties props = new Properties();
		props.put(Constants.KAPTCHA_BORDER, "no");
		props.put(Constants.KAPTCHA_BORDER_COLOR, "black");
		props.put(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, "black");
		props.put(Constants.KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "5");
		props.put(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
		if (!CollectionUtils.isEmpty(properties.getParameters())) {
			props.putAll(properties.getParameters());
		}
		Config config = new Config(props);
		Producer kaptchaProducer = config.getProducerImpl();
		return kaptchaProducer;
	}
	
	@Bean
	public KaptchaService kaptchaService(Producer kaptchaProducer, IKaptchaResolver kaptchaResolver) {
		KaptchaService.init(kaptchaProducer, kaptchaResolver);
		return KaptchaService.instance();
	}
	
	@Bean
	@ConditionalOnProperty(name="framework.kaptcha.sms.aliyun.enabled", havingValue="true")
	public SmsCaptchaService smsCaptchaService() {
		return new AliSmsCaptchaServiceImpl();
	}
}