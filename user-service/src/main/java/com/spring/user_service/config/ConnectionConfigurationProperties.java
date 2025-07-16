package com.spring.user_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "database")
public record ConnectionConfigurationProperties(String url, String port, String username) {
}
