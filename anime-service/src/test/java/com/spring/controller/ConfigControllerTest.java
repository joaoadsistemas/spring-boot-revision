package com.spring.controller;

import com.spring.config.ConfigurationBean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


@WebMvcTest(ConfigController.class)
@Import({ConfigurationBean.class})
class ConfigControllerTest {

    private static final String BASE_URL = "/v1/config";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET v1/config should return the ClassConfig with the activeProfile test")
    void get_ShouldReturn_TheClassConfig_WithTestProfile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)).andDo(MockMvcResultHandlers.print());
    }
}