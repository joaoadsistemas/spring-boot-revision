package com.spring.user_service.mapper;

import com.spring.user_service.annotation.EncodedMapping;
import com.spring.user_service.dto.user.request.UserPostRequestDTO;
import com.spring.user_service.dto.user.request.UserPutRequestDTO;
import com.spring.user_service.dto.user.response.UserGetResponseDTO;
import com.spring.user_service.dto.user.response.UserPostResponseDTO;
import com.spring.user_service.dto.user.response.UserPutResponseDTO;
import com.spring.user_service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {PasswordEncoderMapper.class})
public interface UserMapper {

    @Mapping(target = "roles", constant = "USER")
    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    User toUser(UserPostRequestDTO userPostRequestDTO);

    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    User toUser(UserPutRequestDTO userPutRequestDTO);

    UserGetResponseDTO toUserGetResponse(User user);

    UserPostResponseDTO toUserPostResponse(User user);

    UserPutResponseDTO toUserPutResponse(User user);

}
