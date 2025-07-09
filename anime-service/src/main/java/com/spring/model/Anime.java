package com.spring.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Anime {

    @EqualsAndHashCode.Include
    private int id;
    private String title;
    private LocalDateTime releaseDate;
}
