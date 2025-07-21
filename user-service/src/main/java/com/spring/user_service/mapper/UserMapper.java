package com.spring.user_service.mapper;

import com.spring.user_service.dto.request.UserPostRequestDTO;
import com.spring.user_service.dto.request.UserPutRequestDTO;
import com.spring.user_service.dto.response.UserGetResponseDTO;
import com.spring.user_service.dto.response.UserPostResponseDTO;
import com.spring.user_service.dto.response.UserPutResponseDTO;
import com.spring.user_service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toUser(UserPostRequestDTO userPostRequestDTO);

    User toUser(UserPutRequestDTO userPutRequestDTO);

    UserGetResponseDTO toUserGetResponse(User user);

    UserPostResponseDTO toUserPostResponse(User user);

    UserPutResponseDTO toUserPutResponse(User user);

}
