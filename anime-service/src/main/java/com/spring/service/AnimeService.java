package com.spring.service;

import com.spring.model.Anime;
import com.spring.repository.AnimeRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    public List<Anime> findAll() {
        return animeRepository.findAll();
    }

    public Anime findByTitleOrThrowNotFound(String title) {
        return animeRepository.findByTitle(title).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found"));
    }

    public Anime findByIdOrThrowNotFound(int id) {
        return animeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found"));
    }

    public Anime update(Anime anime) {
        assertAnimeExists(anime.getId());
        return animeRepository.update(anime);
    }

    public Anime save(Anime anime) {
        return animeRepository.save(anime);
    }

    public void delete(int animeId) {
        Anime byIdOrThrowNotFound = findByIdOrThrowNotFound(animeId);
        animeRepository.delete(byIdOrThrowNotFound);
    }

    public void assertAnimeExists(int id) {
        findByIdOrThrowNotFound(id);
    }

}
