package com.spring.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class AnimePostResponse {

    private int id;
    private String title;
    private LocalDate releaseDate;

}
