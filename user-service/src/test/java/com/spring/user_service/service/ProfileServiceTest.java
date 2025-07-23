package com.spring.user_service.service;

import com.spring.exception.BadRequestException;
import com.spring.exception.NotFoundException;
import com.spring.user_service.model.Profile;
import com.spring.user_service.repository.ProfileRepository;
import com.spring.user_service.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @InjectMocks
    private ProfileService profileService;

    @Mock
    private ProfileRepository profileRepository;

    private List<Profile> profileList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        var profile1 = Profile.builder().id(1L).name("Profile 1").description("Description 1").build();
        var profile2 = Profile.builder().id(2L).name("Profile 2").description("Description 2").build();
        var profile3 = Profile.builder().id(3L).name("Profile 3").description("Description 3").build();
        var profile4 = Profile.builder().id(4L).name("Profile 4").description("Description 4").build();

        profileList.addAll(List.of(profile1, profile2, profile3, profile4));
    }

    @Test
    @DisplayName("findAll should return all profiles when successful")
    void findAll_ShouldReturnAllProfiles_WhenSuccessful() {
        BDDMockito.when(profileRepository.findAll()).thenReturn(profileList);

        var result = profileService.findAll();

        Assertions.assertThat(result).isNotNull().containsAll(profileList);
    }

    @Test
    @DisplayName("findAllPageable should return a pageable of profiles")
    void findAllPageable_ShouldReturnPageableOfProfiles_WhenSuccessful() {

        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Profile> pageProfile = new PageImpl<>(profileList, pageable, profileList.size());
        BDDMockito.when(profileRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(pageProfile);

        var result = profileService.findAllPageable(pageable);
        Assertions.assertThat(result).isNotNull().containsAll(profileList);
    }

    @Test
    @DisplayName("findById should return a profile when id exists")
    void findById_ShouldReturnProfile_WhenIdExists() {
        var profile = profileList.stream().findFirst();

        BDDMockito.when(profileRepository.findById(ArgumentMatchers.anyLong())).thenReturn(profile);

        var result = profileService.findById(profile.get().getId());
        Assertions.assertThat(result).isNotNull().isEqualTo(profile.get());
    }

    @Test
    @DisplayName("findById should throw a notFoundException when id does not exists")
    void findById_ShouldThrowNotFoundException_WhenIdDoesNotExists() {
        var id = 99L;

        BDDMockito.when(profileRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> profileService.findById(id)).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("findByName should return a profile when name exists")
    void findByName_ShouldReturnProfile_WhenNameExists() {
        var profile = profileList.stream().findFirst();

        BDDMockito.when(profileRepository.findByName(ArgumentMatchers.anyString())).thenReturn(profile);

        var result = profileService.findByName(profile.get().getName());
        Assertions.assertThat(result).isNotNull().isEqualTo(profile.get());
    }

    @Test
    @DisplayName("findByName should throw a notFoundException when name does not exists")
    void findByName_ShouldThrowNotFoundException_WhenNameDoesNotExists() {
        var name = "xaxa";

        BDDMockito.when(profileRepository.findByName(ArgumentMatchers.anyString())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> profileService.findByName(name)).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("save should persist profile when successful")
    void save_ShouldPersistProfile_WhenSuccessful() {
        var profile = Profile.builder().name("Another profile").description("Another profile").build();
        BDDMockito.when(profileRepository.save(ArgumentMatchers.any(Profile.class))).thenReturn(profile);
        var result = profileService.save(profile);
        Assertions.assertThat(result).isNotNull().isEqualTo(profile);
    }

    @Test
    @DisplayName("save should throw an exception when name already exist")
    void save_ShouldThrowAnException_WhenNameAlreadyExists() {
        var profile = profileList.stream().findFirst();
        var newProfile = Profile.builder().name(profile.get().getName()).description("Another profile").build();

        BDDMockito.when(profileRepository.findByName(ArgumentMatchers.anyString())).thenReturn(profile);
        //BDDMockito.when(profileRepository.save(ArgumentMatchers.any(Profile.class))).thenReturn(newProfile);

        Assertions.assertThatThrownBy(() -> profileService.save(newProfile)).isInstanceOf(BadRequestException.class);
    }

}