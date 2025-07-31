package com.spring.user_service.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.util.DefaultUriBuilderFactory;

import static com.spring.user_service.utils.Constants.*;

@TestConfiguration
@Lazy
public class TestRestTemplateConfig {

    @LocalServerPort
    private int port;

    @Bean
    public TestRestTemplate testRestTemplate() {
        var defaultUriBuilderFactory = new DefaultUriBuilderFactory(BASE_URL + port);
        var testRest = new TestRestTemplate()
                .withBasicAuth(REGULAR_USER, PASSWORD);
        testRest.setUriTemplateHandler(defaultUriBuilderFactory);
        return testRest;
    }

}
