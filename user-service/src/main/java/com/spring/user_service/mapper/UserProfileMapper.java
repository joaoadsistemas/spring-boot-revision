package com.spring.user_service.mapper;

import com.spring.dto.UserProfileGetResponseDTO;
import com.spring.dto.UserProfileGetUserByProfileResponseDTO;
import com.spring.user_service.model.User;
import com.spring.user_service.model.UserProfile;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    Set<UserProfileGetResponseDTO> toUserProfileGetResponseDTOSet(Set<UserProfile> userProfiles);

    Set<UserProfileGetUserByProfileResponseDTO> toUserProfileGetUserByProfileResponseDTOSet(Set<User> users);

}
