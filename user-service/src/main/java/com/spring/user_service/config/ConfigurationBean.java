package com.spring.user_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ConfigurationBean {

    private final ConnectionConfigurationProperties configurationProperties;

    @Bean
    public ClassConfig getConfig() {
        return new ClassConfig(
                configurationProperties.url(),
                configurationProperties.port(),
                configurationProperties.username());
    }

}
