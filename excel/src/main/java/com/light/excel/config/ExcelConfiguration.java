package com.light.excel.config;

import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ComponentScan(basePackages = "com.light.excel")
public class ExcelConfiguration implements WebMvcConfigurer {

	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		// 注册ExcelView解析器
		ViewResolver excelViewResolver = new ViewResolver() {
			private Map<String, AbstractXlsView> views = null;

			@Override
			public View resolveViewName(String viewName, Locale locale) throws Exception {
				AbstractXlsView xlsView = this.getXlsView(viewName);
				return xlsView;
			}

			private AbstractXlsView getXlsView(String viewName) {
				if (this.views == null) {
					this.views = ExcelConfiguration.this.applicationContext.getBeansOfType(AbstractXlsView.class);
				}
				return this.views.get(viewName);
			}
		};
		registry.viewResolver(excelViewResolver);
		log.info("::::: Framework Configuration ::::: {}", "ExcelViewResolver registerd");
	}
}
