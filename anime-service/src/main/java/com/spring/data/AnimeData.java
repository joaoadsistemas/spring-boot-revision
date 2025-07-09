package com.spring.data;

import com.spring.model.Anime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeData {

    private List<Anime> animeList = new ArrayList<>();

    {
        var anime1 = Anime.builder().id(1).title("Hunter X Hunter").releaseDate(LocalDateTime.now()).build();
        var anime2 = Anime.builder().id(2).title("Jojo").releaseDate(LocalDateTime.now()).build();
        var anime3 = Anime.builder().id(3).title("Boku No Hero").releaseDate(LocalDateTime.now()).build();
        var anime4 = Anime.builder().id(4).title("Daemon Slayer").releaseDate(LocalDateTime.now()).build();
        animeList.addAll(List.of(anime1, anime2, anime3, anime4));
    }

    public List<Anime> getAnimeList() {
        return animeList;
    }

}
