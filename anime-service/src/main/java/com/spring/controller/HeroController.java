package com.spring.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/heroes")
public class HeroController {

    private static final List<String> HEROES = List.of(
            "Guts",
            "Zoro",
            "Kakashi",
            "Jotaro"
    );

    @GetMapping
    public List<String> listAllHeroes() {
        return HEROES;
    }

    @GetMapping("/filter")
    public List<String> listAllHeroesParam(@RequestParam(required = false, defaultValue = "") String name) {
        return HEROES.stream().filter(h -> h.contains(name)).collect(Collectors.toList());
    }

    @GetMapping("/{name}")
    public String findByName(@PathVariable String name) {
        return HEROES.stream().filter(h -> h.contains(name)).findFirst().orElse(null);
    }
}
