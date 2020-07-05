package com.light.jpa.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "framework.jpa")
public class JpaExtraProperties {

	private String templatesSuffix = ".sftl";
	
	private String templatesPath = "classpath:/sqls";
}
