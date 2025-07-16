package com.spring.controller;

import com.spring.commons.AnimeUtils;
import com.spring.commons.FileUtils;
import com.spring.config.ConfigurationBean;
import com.spring.data.AnimeData;
import com.spring.mapper.AnimeMapperImpl;
import com.spring.model.Anime;
import com.spring.repository.AnimeRepository;
import com.spring.service.AnimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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