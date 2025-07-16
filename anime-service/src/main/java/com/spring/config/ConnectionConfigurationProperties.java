package com.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "database")
public record ConnectionConfigurationProperties(String username, String url, String port) {
}
