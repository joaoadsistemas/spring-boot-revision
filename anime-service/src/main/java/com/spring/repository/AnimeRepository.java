package com.spring.repository;

import com.spring.data.AnimeData;
import com.spring.model.Anime;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AnimeRepository {

    private final AnimeData animeData;

    public AnimeRepository(AnimeData animeData) {
        this.animeData = animeData;
    }

    public List<Anime> findAll() {
        return animeData.getAnimeList();
    }

    public Optional<Anime> findByTitle(String title) {
        return animeData.getAnimeList().stream().filter(anime -> anime.getTitle().equalsIgnoreCase(title)).findFirst();
    }

    public Optional<Anime> findById(int id) {
        return animeData.getAnimeList().stream().filter(anime -> anime.getId() == id).findFirst();
    }

    public Anime update(Anime anime) {
        delete(anime);
        return save(anime);
    }

    public Anime save(Anime anime) {
        animeData.getAnimeList().add(anime);
        return anime;
    }

    public void delete(Anime anime) {
        animeData.getAnimeList().remove(anime);
    }

}
