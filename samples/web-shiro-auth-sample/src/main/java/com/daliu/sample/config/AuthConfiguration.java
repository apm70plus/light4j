package com.daliu.sample.config;

import com.daliu.sample.service.DefaultLoginRetryCounter;
import com.daliu.sample.service.LoginRetryCounter;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AuthConfiguration {

    @Value("${light.auth.login.max_retry_times}")
    private int maxRetryTimes;
    @Value("${light.auth.login.lock_expired_minutes}")
    private int lockExpiredMinutes;
    @Autowired(required = false)
    private List<MapConfig> mapConfigList;

    @Bean
    public LoginRetryCounter loginRetryCounter() {
        DefaultLoginRetryCounter counter = new DefaultLoginRetryCounter(
                maxRetryTimes, lockExpiredMinutes);
        return counter;
    }

    @Bean(destroyMethod = "shutdown")
    public HazelcastInstance hazelcastInstance() {
        Config config = new Config();

        loadMapConfigs(config);

        return Hazelcast.newHazelcastInstance(config);
    }

    private void loadMapConfigs(Config config) {
        if (mapConfigList == null) {
            return;
        }
        mapConfigList.forEach(mapConfig -> {
            config.getMapConfigs().put(mapConfig.getName(), mapConfig);
        });
    }
}
