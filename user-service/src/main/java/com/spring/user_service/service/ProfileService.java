package com.spring.user_service.service;

import com.spring.exception.BadRequestException;
import com.spring.exception.NotFoundException;
import com.spring.user_service.model.Profile;
import com.spring.user_service.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;


    public Set<Profile> findAll() {
        return new HashSet<>(profileRepository.findAll());
    }

    public Page<Profile> findAllPageable(Pageable pageable) {
        return profileRepository.findAll(pageable);
    }

    public Profile findById(Long id) {
        return profileRepository.findById(id).orElseThrow(() -> new NotFoundException("There's no profile with id: " + id));
    }

    public Profile findByName(String name) {
        return profileRepository.findByName(name).orElseThrow(() -> new NotFoundException("There's no profile with name: " + name));
    }

    public Profile save(Profile profile) {
        assertThatTheNameDoesNotExist(profile.getName());
        return profileRepository.save(profile);
    }

    private void assertThatTheNameDoesNotExist(String name) {
        profileRepository.findByName(name).ifPresent(profile -> {throw new BadRequestException("There's already a profile with this name: " + name);});
    }
}
