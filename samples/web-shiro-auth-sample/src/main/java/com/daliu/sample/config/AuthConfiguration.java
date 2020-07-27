package com.daliu.sample.config;

import com.daliu.sample.service.DefaultLoginRetryCounter;
import com.daliu.sample.service.LoginRetryCounter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfiguration {

    @Value("${light.auth.login.max_retry_times}")
    private int maxRetryTimes;
    @Value("${light.auth.login.lock_expired_minutes}")
    private int lockExpiredMinutes;

    @Bean
    public LoginRetryCounter loginRetryCounter() {
        DefaultLoginRetryCounter counter = new DefaultLoginRetryCounter(
                maxRetryTimes, lockExpiredMinutes);
        return counter;
    }
}
