package com.spring.repository;

import com.spring.commons.AnimeUtils;
import com.spring.data.AnimeData;
import com.spring.model.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AnimeRepositoryTest {

    @InjectMocks
    private AnimeRepository animeRepository;

    @InjectMocks
    private AnimeUtils animeUtils;

    @Mock
    private AnimeData animeData;

    private List<Anime> animeList = new ArrayList<>();


    @BeforeEach
    void setUp() {
        animeList = animeUtils.newAnimeList();
    }

    @Test
    @DisplayName("findAll returns a list with all animes")
    void findAll_ReturnsAllAnimes_WhenSuccessful() {
        BDDMockito.when(this.animeData.getAnimeList()).thenReturn(this.animeList);
        List<Anime> findAllList = this.animeRepository.findAll();
        Assertions.assertThat(this.animeList).isEqualTo(findAllList);
    }

    @Test
    @DisplayName("findByTitle returns empty list when name is null")
    void findByTitle_ReturnsEmptyList_WhenNameIsNull() {
        BDDMockito.when(this.animeData.getAnimeList()).thenReturn(this.animeList);
        Optional<Anime> anime = this.animeRepository.findByTitle(null);
        Assertions.assertThat(anime).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByTitle returns a anime when name is valid")
    void findByTitle_ReturnsAnime_WhenNameIsValid() {
        BDDMockito.when(this.animeData.getAnimeList()).thenReturn(this.animeList);
        Optional<Anime> anime = this.animeRepository.findByTitle(this.animeList.get(0).getTitle());
        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.get().getTitle()).isEqualTo(this.animeList.get(0).getTitle());
    }

    @Test
    @DisplayName("findById returns a anime when id is valid")
    void findById_ReturnsAnime_WhenSuccessful() {
        BDDMockito.when(this.animeData.getAnimeList()).thenReturn(this.animeList);
        Optional<Anime> anime = this.animeRepository.findById(this.animeList.get(0).getId());
        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.get().getId()).isEqualTo(this.animeList.get(0).getId());
    }

    @Test
    @DisplayName("findById returns a empty list when id is invalid")
    void findById_ReturnsEmptyList_WhenIdIsInvalid() {
        BDDMockito.when(this.animeData.getAnimeList()).thenReturn(this.animeList);
        Optional<Anime> anime = this.animeRepository.findById(99);
        Assertions.assertThat(anime).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("update updates an anime")
    void update_UpdatesAnime_WhenSuccessful() {
        BDDMockito.when(this.animeData.getAnimeList()).thenReturn(this.animeList);
        var animeToUpdate = this.animeList.get(0);
        animeToUpdate.setTitle("Hellsing");

        animeRepository.update(animeToUpdate);
        Assertions.assertThat(this.animeList).contains(animeToUpdate);

        var animeUpdatedOptional = this.animeRepository.findById(animeToUpdate.getId());

        Assertions.assertThat(animeUpdatedOptional).isPresent();
        Assertions.assertThat(animeUpdatedOptional.get().getTitle()).isEqualTo(animeToUpdate.getTitle());
    }


    @Test
    @DisplayName("save should save a new anime")
    void save_SaveAnime_WhenSuccessful() {
        BDDMockito.when(this.animeData.getAnimeList()).thenReturn(this.animeList);

        var animeToSave = Anime.builder().id(99).title("Pokemon").build();
        this.animeRepository.save(animeToSave);

        var animeSavedOptional = this.animeRepository.findById(animeToSave.getId());
        Assertions.assertThat(animeSavedOptional).isPresent().contains(animeToSave);
    }

    @Test
    @DisplayName("delete removes an anime")
    void delete_RemoveAnime_WhenSuccessful() {
        BDDMockito.when(this.animeData.getAnimeList()).thenReturn(this.animeList);

        var animeToDelete = this.animeList.get(0);
        this.animeRepository.delete(animeToDelete);

        var animes = this.animeRepository.findAll();

        Assertions.assertThat(animes).isNotEmpty().doesNotContain(animeToDelete);
    }
}