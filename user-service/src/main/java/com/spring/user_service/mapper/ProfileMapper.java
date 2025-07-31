package com.spring.user_service.mapper;

import com.spring.user_service.dto.profile.request.ProfilePostRequest;
import com.spring.user_service.dto.profile.response.ProfileGetResponse;
import com.spring.user_service.model.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    Profile toProfile(ProfileGetResponse profileGetResponse);

    Profile toProfile(ProfilePostRequest profilePostRequest);

    ProfileGetResponse toProfileGetResponse(Profile profile);

}
