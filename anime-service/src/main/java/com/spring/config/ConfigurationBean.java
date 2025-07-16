package com.spring.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ConfigurationBean {

    private final ConnectionConfigurationProperties configurationProperties;

    @Bean
    public ClassConfig configuration() {
        return new ClassConfig(
                configurationProperties.username(),
                configurationProperties.url(),
                configurationProperties.port());
    }

}
