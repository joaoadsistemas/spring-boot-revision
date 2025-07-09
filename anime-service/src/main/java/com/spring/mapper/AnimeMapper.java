package com.spring.mapper;

import com.spring.dto.request.AnimePostRequest;
import com.spring.dto.request.AnimePutRequest;
import com.spring.dto.response.AnimeGetResponse;
import com.spring.dto.response.AnimePostResponse;
import com.spring.dto.response.AnimePutResponse;
import com.spring.model.Anime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AnimeMapper {

    AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);

    @Mapping(target = "releaseDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextInt(100_000))")
    Anime toAnime(AnimePostRequest postRequest);

    Anime toAnime(AnimePutRequest putRequest);

    AnimePutResponse toAnimePutResponse(Anime anime);

    AnimeGetResponse toAnimeGetResponse(Anime anime);
}
