package com.spring.user_service.dto.userProfile.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class UserProfileGetResponseDTO {

    public record UserDTO(Long id, String firstName) {

    }

    public record ProfileDTO(Long id, String name) {

    }

    private Long id;
    private UserDTO user;
    private ProfileDTO profile;


}
