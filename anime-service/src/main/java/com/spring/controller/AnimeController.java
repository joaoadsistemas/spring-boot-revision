package com.spring.controller;

import com.spring.dto.request.AnimePostRequest;
import com.spring.dto.request.AnimePutRequest;
import com.spring.dto.response.AnimeGetResponse;
import com.spring.dto.response.AnimePutResponse;
import com.spring.mapper.AnimeMapper;
import com.spring.service.AnimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/animes")
public class AnimeController {

    private static final AnimeMapper MAPPER = AnimeMapper.INSTANCE;
    private final AnimeService animeService;

    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> getAll() {
        return ResponseEntity.ok(animeService.findAll().stream().map(MAPPER::toAnimeGetResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable("id") Integer id) {
        try {
            return ResponseEntity.ok(MAPPER.toAnimeGetResponse(animeService.findByIdOrThrowNotFound(id)));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-title")
    public ResponseEntity<AnimeGetResponse> findByTitle(@RequestParam("title") String title) {
        return ResponseEntity.ok(MAPPER.toAnimeGetResponse(animeService.findByTitleOrThrowNotFound(title)));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody AnimePostRequest animeRequest) {
        var anime = MAPPER.toAnime(animeRequest);
        var animeSaved = animeService.save(anime);
        return ResponseEntity.noContent().build();

    }

    @PutMapping
    public ResponseEntity<AnimePutResponse> update(@RequestBody AnimePutRequest animeRequest) {
        var anime = MAPPER.toAnime(animeRequest);
        var response = animeService.update(anime);
        return ResponseEntity.ok(MAPPER.toAnimePutResponse(response));

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        animeService.delete(id);
        return ResponseEntity.noContent().build();

    }


}
