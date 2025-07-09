package com.spring.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class AnimePutRequest {

    private int id;
    private String title;
    private LocalDateTime releaseDate;

}
