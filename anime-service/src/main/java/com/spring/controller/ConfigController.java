package com.spring.controller;

import com.spring.config.ClassConfig;
import com.spring.config.ConfigurationBean;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/config")
public class ConfigController {

    private final ConfigurationBean configurationBean;

    @GetMapping
    public ResponseEntity<ClassConfig> getConfig() {
        return ResponseEntity.ok(configurationBean.configuration());
    }

}
