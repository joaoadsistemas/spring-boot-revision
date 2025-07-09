package com.spring.controller;

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
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@WebMvcTest(AnimeController.class)
@Import({AnimeService.class, AnimeRepository.class, AnimeMapperImpl.class, AnimeData.class})
class AnimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResourceLoader resourceLoader;

    @MockBean
    private AnimeData animeData;

    @SpyBean
    private AnimeService animeService;

    private List<Anime> animeList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        // i need to use this to set a default data here and in the file that i put the request and response
        var local = LocalDateTime.of(2025, 07, 05, 00, 00, 00);

        var anime1 = Anime.builder().id(1).title("Jujutsu Kaisen").releaseDate(local).build();
        var anime2 = Anime.builder().id(2).title("Dororo").releaseDate(local).build();
        var anime3 = Anime.builder().id(3).title("Atack on Titan").releaseDate(local).build();
        var anime4 = Anime.builder().id(4).title("Fullmetal").releaseDate(local).build();
        animeList.addAll(List.of(anime1, anime2, anime3, anime4));
    }

    @Test
    @DisplayName("GET v1/animes should return all animes")
    void getAll_ShouldReturnAListOfAnimes() throws Exception {

        BDDMockito.when(this.animeData.getAnimeList()).thenReturn(this.animeList);

        var response = readResourceFile("anime/get-animes-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET v1/animes/1 should return a anime")
    void findById_ShouldReturnAnime_WhenSuccessful() throws Exception {

        var anime = animeList.get(0);
        var response = readResourceFile("anime/get-anime-by-id-200.json");

        BDDMockito.when(this.animeData.getAnimeList()).thenReturn(this.animeList);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}", anime.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET v1/animes/99 should throw an error")
    void findById_ShouldThrowAnError_WhenIdDoesNotExists() throws Exception {
        var animeId = 99;

        BDDMockito.when(this.animeData.getAnimeList()).thenReturn(this.animeList);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}", animeId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    @DisplayName("GET v1/animes/by-title?title=jujutsu should return an anime")
    void findByTitle_ShouldReturnAnime_WhenSuccessful() throws Exception {

        var anime = this.animeList.get(0);
        var response = readResourceFile("anime/get-anime-by-title-200.json");

        BDDMockito.when(this.animeData.getAnimeList()).thenReturn(animeList);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/by-title").param("title", anime.getTitle()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET v1/animes/by-title?title=nothing should return an empty object")
    void findByTitle_ShouldReturnAnEmptyObject() throws Exception {

        BDDMockito.when(this.animeData.getAnimeList()).thenReturn(this.animeList);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/by-title").param("title", "unknow"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }


    @Test
    @DisplayName("POST v1/animes with a valid entry should return noContent")
    void post_ShouldSave_WhenSuccessful() throws Exception {
        BDDMockito.when(this.animeData.getAnimeList()).thenReturn(this.animeList);

        var request = readResourceFile("anime/post-request-anime-200.json");

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/v1/animes")
                                .contentType("application/json")
                                .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/animes with a valid entry should return an object")
    void update_ShouldUpdate_WhenSuccessful() throws Exception {
        var request = readResourceFile("anime/put-request-anime-200.json");
        var response = readResourceFile("anime/put-response-anime-200.json");

        var anime = Anime.builder()
                .id(1)
                .title("Yuyu Hakusho")
                .releaseDate(LocalDateTime.of(2025, 7, 5, 0, 0))
                .build();

        animeList.clear();
        animeList.add(anime);

        BDDMockito.when(animeData.getAnimeList()).thenReturn(animeList);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/v1/animes")
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
                        MockMvcRequestBuilders.delete("/v1/animes/{id}", anime.getId())
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
                        MockMvcRequestBuilders.delete("/v1/animes/{id}", animeId)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    private String readResourceFile(String fileName) throws IOException {
        var file = resourceLoader.getResource("classpath:%s".formatted(fileName)).getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }
}