package com.spring.service;

import com.spring.model.Anime;
import com.spring.repository.AnimeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepository;

    private List<Anime> animeList = new ArrayList<>();


    @BeforeEach
    void setUp() {
        var anime1 = Anime.builder().id(1).title("Jujutsu Kaisen").releaseDate(LocalDateTime.now()).build();
        var anime2 = Anime.builder().id(2).title("Dororo").releaseDate(LocalDateTime.now()).build();
        var anime3 = Anime.builder().id(3).title("Atack on Titan").releaseDate(LocalDateTime.now()).build();
        var anime4 = Anime.builder().id(4).title("Fullmetal").releaseDate(LocalDateTime.now()).build();
        animeList.addAll(List.of(anime1, anime2, anime3, anime4));
    }

    @Test
    @DisplayName("findAll returns a list with all animes")
    void findAll() {
        BDDMockito.when(this.animeRepository.findAll()).thenReturn(this.animeList);

        var result = animeService.findAll();
        Assertions.assertThat(result).isNotNull().isEqualTo(animeList);
    }

    @Test
    @DisplayName("findByTitle returns empty list when name is null")
    void findByTitle_ReturnsEmptyList_WhenNameIsNull() {
        BDDMockito.when(this.animeRepository.findByTitle(ArgumentMatchers.any())).thenReturn(Optional.empty());
        Optional<Anime> anime = this.animeRepository.findByTitle(null);
        Assertions.assertThat(anime).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByTitle returns a anime when name is valid")
    void findByTitle_ReturnsAnime_WhenNameIsValid() {
        var title = this.animeList.getFirst().getTitle();
        BDDMockito.when(this.animeRepository.findByTitle(title)).thenReturn(Optional.ofNullable(this.animeList.getFirst()));
        Optional<Anime> anime = this.animeRepository.findByTitle(title);
        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.get().getTitle()).isEqualTo(this.animeList.getFirst().getTitle());
    }

    @Test
    @DisplayName("findById returns a anime when id is valid")
    void findById_ReturnsAnime_WhenSuccessful() {
        var id = this.animeList.getFirst().getId();
        BDDMockito.when(this.animeRepository.findById(id)).thenReturn(Optional.ofNullable(this.animeList.getFirst()));
        Optional<Anime> anime = this.animeRepository.findById(id);
        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.get().getId()).isEqualTo(this.animeList.getFirst().getId());
    }

    @Test
    @DisplayName("findById returns a empty list when id is invalid")
    void findById_ReturnsEmptyList_WhenIdIsInvalid() {
        var id = 999;
        BDDMockito.when(this.animeRepository.findById(999)).thenReturn(Optional.empty());
        Optional<Anime> anime = this.animeRepository.findById(id);
        Assertions.assertThat(anime).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("update updates an anime")
    void update_UpdatesAnime_WhenSuccessful() {
        var animeToUpdate = this.animeList.getFirst();
        animeToUpdate.setTitle("Grand Blue");

        BDDMockito.when(this.animeRepository.findById(animeToUpdate.getId())).thenReturn(Optional.of(animeToUpdate));
        BDDMockito.when(this.animeRepository.update(animeToUpdate)).thenReturn(animeToUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> this.animeService.update(animeToUpdate));
    }

    @Test
    @DisplayName("update throws ResponseStatusException when anime is not found")
    void update_ThrowsResponseStatusException_WhenProducerIsNotFound() {
        var animeToUpdate = this.animeList.getFirst();

        BDDMockito.when(this.animeRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> this.animeService.update(animeToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }


    @Test
    @DisplayName("save creates an anime")
    void save_CreatesAnime_WhenSuccessful() {
        var animeToSave = Anime.builder().id(99).title("Hellsing").releaseDate(LocalDateTime.now()).build();

        BDDMockito.when(this.animeRepository.save(animeToSave)).thenReturn(animeToSave);

        var savedAnime = this.animeService.save(animeToSave);

        Assertions.assertThat(savedAnime).isEqualTo(animeToSave).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("delete removes an anime")
    void delete_RemoveAnime_WhenSuccessful() {
        var animeToDelete = this.animeList.getFirst();
        BDDMockito.when(this.animeRepository.findById(animeToDelete.getId())).thenReturn(Optional.of(animeToDelete));
        BDDMockito.doNothing().when(this.animeRepository).delete(animeToDelete);

        Assertions.assertThatNoException().isThrownBy(() -> this.animeService.delete(animeToDelete.getId()));
    }

    @Test
    @DisplayName("delete throws ResponseStatusException when anime is not found")
    void delete_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
        var animeToDelete = this.animeList.getFirst();
        BDDMockito.when(this.animeRepository.findById(animeToDelete.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> this.animeService.delete(animeToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }
}