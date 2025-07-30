package com.spring.user_service.controller;

import com.spring.user_service.dto.userProfile.response.UserProfileGetResponseDTO;
import com.spring.user_service.dto.userProfile.response.UserProfileGetUserByProfileResponseDTO;
import com.spring.user_service.mapper.UserProfileMapper;
import com.spring.user_service.model.User;
import com.spring.user_service.model.UserProfile;
import com.spring.user_service.service.UserProfileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/user-profiles")
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private static final UserProfileMapper MAPPER = UserProfileMapper.INSTANCE;

    @GetMapping
    public ResponseEntity<Set<UserProfileGetResponseDTO>> findAll() {
        return ResponseEntity.ok(MAPPER.toUserProfileGetResponseDTOSet(new HashSet<>(userProfileService.findAll())));
    }

    @GetMapping("/profile/{id}/users")
    public ResponseEntity<Set<UserProfileGetUserByProfileResponseDTO>> findAllUsersByProfileId(@PathVariable Long id) {
        return ResponseEntity.ok(new HashSet<>(MAPPER.toUserProfileGetUserByProfileResponseDTOSet(userProfileService.findAllUsersByProfileId(id))));
    }

}
