package com.spring.user_service.service;

import com.spring.user_service.model.UserProfile;
import com.spring.user_service.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;


    public Set<UserProfile> findAll() {
        return new HashSet<>(userProfileRepository.retrieveAll());
    }

}
