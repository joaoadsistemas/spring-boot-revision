package com.spring.controller;

import com.spring.commons.AnimeUtils;
import com.spring.commons.FileUtils;
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


@WebMvcTest(AnimeController.class)
@Import({AnimeService.class, AnimeRepository.class, AnimeMapperImpl.class, AnimeData.class, FileUtils.class, AnimeUtils.class})
class AnimeControllerTest {

    private static final String BASE_URL = "/v1/animes";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimeData animeData;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private AnimeUtils animeUtils;

    @SpyBean
    private AnimeService animeService;

    private List<Anime> animeList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        animeList = animeUtils.newAnimeList();
    }

    @Test
    @DisplayName("GET v1/animes should return all animes")
    void getAll_ShouldReturnAListOfAnimes() throws Exception {

        BDDMockito.when(this.animeData.getAnimeList()).thenReturn(this.animeList);

        var response = fileUtils.readResourceFile("anime/get-animes-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET v1/animes/1 should return a anime")
    void findById_ShouldReturnAnime_WhenSuccessful() throws Exception {

        var anime = animeList.get(0);
        var response = fileUtils.readResourceFile("anime/get-anime-by-id-200.json");

        BDDMockito.when(this.animeData.getAnimeList()).thenReturn(this.animeList);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", anime.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET v1/animes/99 should throw an error")
    void findById_ShouldThrowAnError_WhenIdDoesNotExists() throws Exception {
        var animeId = 99;

        BDDMockito.when(this.animeData.getAnimeList()).thenReturn(this.animeList);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", animeId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    @DisplayName("GET v1/animes/by-title?title=jujutsu should return an anime")
    void findByTitle_ShouldReturnAnime_WhenSuccessful() throws Exception {

        var anime = this.animeList.get(0);
        var response = fileUtils.readResourceFile("anime/get-anime-by-title-200.json");

        BDDMockito.when(this.animeData.getAnimeList()).thenReturn(animeList);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/by-title").param("title", anime.getTitle()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET v1/animes/by-title?title=nothing should return an empty object")
    void findByTitle_ShouldReturnAnEmptyObject() throws Exception {

        BDDMockito.when(this.animeData.getAnimeList()).thenReturn(this.animeList);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/by-title").param("title", "unknow"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }


    @Test
    @DisplayName("POST v1/animes with a valid entry should return noContent")
    void post_ShouldSave_WhenSuccessful() throws Exception {
        BDDMockito.when(this.animeData.getAnimeList()).thenReturn(this.animeList);

        var request = fileUtils.readResourceFile("anime/post-request-anime-200.json");

        mockMvc.perform(
                        MockMvcRequestBuilders.post(BASE_URL)
                                .contentType("application/json")
                                .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/animes with a valid entry should return an object")
    void update_ShouldUpdate_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("anime/put-request-anime-200.json");
        var response = fileUtils.readResourceFile("anime/put-response-anime-200.json");

        var anime = Anime.builder()
                .id(1)
                .title("Yuyu Hakusho")
                .releaseDate(LocalDateTime.of(2025, 7, 5, 0, 0))
                .build();

        animeList.clear();
        animeList.add(anime);

        BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);

        mockMvc.perform(
                        MockMvcRequestBuilders.put(BASE_URL)
                                .contentType("application/json")
                                .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("DELETE v1/animes with a valid id should delete it")
    void delete_ShouldDelete_WhenSuccessful() throws Exception {
        var anime = this.animeList.get(0);
        BDDMockito.when(this.animeData.getAnimeList()).thenReturn(this.animeList);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete(BASE_URL + "/{id}", anime.getId())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/animes with a invalid id should throw a exception")
    void delete_ShouldThrowException_WhenIdIsInvalid() throws Exception {
        var animeId = 99;
        BDDMockito.when(this.animeData.getAnimeList()).thenReturn(this.animeList);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete(BASE_URL + "/{id}", animeId)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}