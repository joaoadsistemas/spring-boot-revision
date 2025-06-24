package com.spring.controller;

import com.spring.dto.request.AnimePostRequest;
import com.spring.dto.response.AnimePostResponse;
import com.spring.mapper.AnimeMapper;
import com.spring.model.Anime;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/animes")
public class AnimeController {

    private static final AnimeMapper MAPPER = AnimeMapper.INSTANCE;

    @GetMapping
    public List<String> findAllAnimes() {
        return List.of("Naruto", "HxH", "AoT", "Jojo", "Demon Slayer");
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> addAnime(@RequestBody AnimePostRequest animePostRequest) {
        Anime anime = MAPPER.toAnime(animePostRequest);
        anime.getAnimes().add(anime);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(MAPPER.toAnimePostResponse(anime));
    }

}
