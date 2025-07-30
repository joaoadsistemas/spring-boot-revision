package com.spring.user_service.controller;

import com.spring.user_service.dto.profile.request.ProfilePostRequest;
import com.spring.user_service.dto.profile.response.ProfileGetResponse;
import com.spring.user_service.mapper.ProfileMapper;
import com.spring.user_service.service.ProfileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/profiles")
@AllArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class ProfileController {

    private final ProfileService profileService;
    private static final ProfileMapper mapper = ProfileMapper.INSTANCE;

    @GetMapping
    public ResponseEntity<Set<ProfileGetResponse>> findAll() {
        return ResponseEntity.ok(profileService.findAll().stream()
                .map(mapper::toProfileGetResponse).collect(Collectors.toSet()));
    }

    @GetMapping("/pageable")
    public ResponseEntity<Page<ProfileGetResponse>> findAllByPageable(Pageable pageable) {
        return ResponseEntity.ok(profileService.findAllPageable(pageable).map(mapper::toProfileGetResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileGetResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toProfileGetResponse(profileService.findById(id)));
    }

    @GetMapping("/by-name")
    public ResponseEntity<ProfileGetResponse> findByName(@RequestParam String name) {
        return ResponseEntity.ok(mapper.toProfileGetResponse(profileService.findByName(name)));
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody ProfilePostRequest profilePostRequest) {
        var profile = mapper.toProfile(profilePostRequest);
        profileService.save(profile);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
