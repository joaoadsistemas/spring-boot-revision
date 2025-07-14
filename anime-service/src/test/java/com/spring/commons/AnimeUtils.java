package com.spring.commons;

import com.spring.model.Anime;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeUtils {

    public List<Anime> newAnimeList(){
        // i need to use this to set a default data here and in the file that i put the request and response
        var local = LocalDateTime.of(2025, 07, 05, 00, 00, 00);

        var anime1 = Anime.builder().id(1).title("Jujutsu Kaisen").releaseDate(local).build();
        var anime2 = Anime.builder().id(2).title("Dororo").releaseDate(local).build();
        var anime3 = Anime.builder().id(3).title("Atack on Titan").releaseDate(local).build();
        var anime4 = Anime.builder().id(4).title("Fullmetal").releaseDate(local).build();
        return new ArrayList<>(List.of(anime1, anime2, anime3, anime4));
    }

}
