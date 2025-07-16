package com.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationBean {

    @Value("${database.username}")
    private String username;

    @Value("${database.url}")
    private String url;

    @Value("${database.port}")
    private String port;

    @Bean
    public ClassConfig configuration() {
        return new ClassConfig(username, url, port);
    }

}
