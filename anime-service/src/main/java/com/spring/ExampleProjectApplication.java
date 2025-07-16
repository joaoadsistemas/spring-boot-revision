package com.spring;

import com.spring.config.ConnectionConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ConnectionConfigurationProperties.class)
public class ExampleProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExampleProjectApplication.class, args);
    }
}
