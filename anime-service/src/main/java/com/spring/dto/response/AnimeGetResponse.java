package com.spring.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class AnimeGetResponse {

    private int id;
    private String title;
    private LocalDateTime releaseDate;

}
