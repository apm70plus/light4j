package com.light.jpa.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.light.jpa.repository.query.FreemarkerSqlTemplates;

@Configuration
@EnableConfigurationProperties(JpaExtraProperties.class)
public class JpaExtraConfiguration {

	@Bean
	public FreemarkerSqlTemplates freemarkerSqlTemplates(JpaExtraProperties properties) {
		FreemarkerSqlTemplates templates = new FreemarkerSqlTemplates();
		templates.setSuffix(properties.getTemplatesSuffix());
		templates.setTemplateLocation(properties.getTemplatesPath());
		return templates;
	}
}
