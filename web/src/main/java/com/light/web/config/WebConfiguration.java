package com.light.web.config;

import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.light.config.Constants;
import com.light.web.local.AngularCookieLocaleResolver;
import com.light.web.rsql.RsqlQuerydslPredicateArgumentResolver;
import com.light.web.rsql.RsqlQuerydslPredicateBuilderFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties({ WebProperties.class })
@ComponentScan(basePackages = "com.light.web")
public class WebConfiguration implements WebMvcConfigurer, ServletContextInitializer {

	@Autowired
	private Environment env;
	@Autowired
	private WebProperties properties;
	@Autowired
	private RsqlQuerydslPredicateBuilderFactory rsqlQuerydslPredicateBuilderFactory;
	
//	@Bean
//	public UndertowServletWebServerFactory servletWebServerFactory() {
//      // 配置使同时支持https和http请求
//		UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
//		factory.addBuilderCustomizers(new UndertowBuilderCustomizer() {
//
//			@Override
//			public void customize(Builder builder) {
//				builder.addHttpListener(8080, "0.0.0.0");
//			}
//
//		});
//		return factory;
//	}

	@Override
	public void onStartup(final ServletContext servletContext) throws ServletException {
		if (this.env.getActiveProfiles().length != 0) {
		    log.info(Constants.CONFIG_LOG_MARK, "Web application configuration, using profiles: " +
					Arrays.toString(this.env.getActiveProfiles()));
		}
		if (this.env.acceptsProfiles(Profiles.of(Constants.SPRING_PROFILE_DEVELOPMENT))) {
			try {
				Thread.currentThread().getContextClassLoader().loadClass("org.h2.server.web.WebServlet");
				this.initH2Console(servletContext);
			} catch (ClassNotFoundException e) {
				return;
			}
		}
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		if (this.properties.getCorsPath().isEmpty()) {
			return;
		}
	    log.info(Constants.CONFIG_LOG_MARK, "CORS enabled");
		for (final String path : this.properties.getCorsPath()) {
			registry.addMapping(path);
		}
	}

	@Override
	public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(0, new RsqlQuerydslPredicateArgumentResolver(rsqlQuerydslPredicateBuilderFactory));
	}

	@Override
	public void extendMessageConverters(final List<HttpMessageConverter<?>> converters) {
		// 配置数据库Date类型转json时，格式为时间戳Long
		//final SqlDateSerializer sqlDateSerializer = new SqlDateSerializer().withFormat(true, null);
		//final SimpleModule module = new SimpleModule();
		//module.addSerializer(Date.class, sqlDateSerializer);
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		// long损失精度的问题
		SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
		for (final HttpMessageConverter<?> converter : converters) {
			if (converter instanceof MappingJackson2HttpMessageConverter) {
				final MappingJackson2HttpMessageConverter jsonMessageConverter = (MappingJackson2HttpMessageConverter) converter;
				final ObjectMapper objectMapper = jsonMessageConverter.getObjectMapper();
				//objectMapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
				objectMapper.registerModule(javaTimeModule);
	            objectMapper.registerModule(simpleModule);
			}
		}
		log.info(Constants.CONFIG_LOG_MARK, "JacksonMessageConvertor WRITE_DATES_AS_TIMESTAMPS configured");
	}

	/**
	 * Initializes H2 console.
	 * @param h2ServletClazz 
	 */
	private void initH2Console(final ServletContext servletContext) {
		final ServletRegistration.Dynamic h2ConsoleServlet = servletContext.addServlet("H2Console",
				"org.h2.server.web.WebServlet");
		h2ConsoleServlet.addMapping("/h2-console/*");
		h2ConsoleServlet.setInitParameter("-properties", "src/main/resources/");
		h2ConsoleServlet.setLoadOnStartup(1);
		log.info(Constants.CONFIG_LOG_MARK, "H2 console enabled, Access URL path: /h2-console");
	}
	
	@Bean(name = "localeResolver")
    public LocaleResolver localeResolver() {
        final AngularCookieLocaleResolver cookieLocaleResolver = new AngularCookieLocaleResolver();
        cookieLocaleResolver.setCookieName("NG_TRANSLATE_LANG_KEY");
        return cookieLocaleResolver;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        final LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        registry.addInterceptor(localeChangeInterceptor);
    }
}
