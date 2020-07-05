package com.light.oauth2;

import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.light.config.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class OAuth2AuthzServerAutoConfiguration {

	@PostConstruct
	public void init() {
		log.info(Constants.CONFIG_LOG_MARK, "OAuth2 AuthzServer enabled");
	}
}
