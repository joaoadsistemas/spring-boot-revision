package com.spring.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Anime {

    private int id;
    private String title;
    private LocalDate releaseDate;

    private List<Anime> animes = new ArrayList<>();

}
