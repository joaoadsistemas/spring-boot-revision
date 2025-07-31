package com.spring.user_service.mapper;

import com.spring.user_service.dto.userProfile.response.UserProfileGetResponseDTO;
import com.spring.user_service.dto.userProfile.response.UserProfileGetUserByProfileResponseDTO;
import com.spring.user_service.model.User;
import com.spring.user_service.model.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    Set<UserProfileGetResponseDTO> toUserProfileGetResponseDTOSet(Set<UserProfile> userProfiles);
    Set<UserProfileGetUserByProfileResponseDTO> toUserProfileGetUserByProfileResponseDTOSet(Set<User> users);

}
