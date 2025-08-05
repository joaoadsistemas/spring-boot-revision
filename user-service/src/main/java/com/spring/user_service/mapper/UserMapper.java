package com.spring.user_service.mapper;

import com.spring.dto.*;
import com.spring.user_service.annotation.EncodedMapping;
import com.spring.user_service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", uses = {PasswordEncoderMapper.class})
public interface UserMapper {

    @Mapping(target = "roles", constant = "USER")
    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    User toUser(UserPostRequestDTO userPostRequestDTO);

    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    User toUser(UserPutRequestDTO userPutRequestDTO);

    UserGetResponseDTO toUserGetResponse(User user);

    UserPutResponseDTO toUserPutResponse(User user);

    PageUserGetResponseDTO toPageUserGetResponseDTO(Page<UserGetResponseDTO>users);

}
