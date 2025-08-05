package com.spring.user_service.mapper;


import com.spring.dto.PageProfileGetResponse;
import com.spring.dto.ProfileGetResponse;
import com.spring.dto.ProfilePostRequest;
import com.spring.user_service.model.Profile;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    Profile toProfile(ProfileGetResponse profileGetResponse);

    Profile toProfile(ProfilePostRequest profilePostRequest);

    ProfileGetResponse toProfileGetResponse(Profile profile);

    PageProfileGetResponse toPageProfileGetResponse(Page<ProfileGetResponse> page);

}
