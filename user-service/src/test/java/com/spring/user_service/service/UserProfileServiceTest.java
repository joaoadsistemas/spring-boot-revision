package com.spring.user_service.service;

import com.spring.user_service.model.Profile;
import com.spring.user_service.model.User;
import com.spring.user_service.model.UserProfile;
import com.spring.user_service.repository.UserProfileRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @InjectMocks
    private UserProfileService userProfileService;

    @Mock
    private UserProfileRepository userProfileRepository;

    private List<Profile> profileList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();
    private List<UserProfile> userProfileList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        var profile1 = Profile.builder().id(1L).name("Admin").description("Administrator").build();

        var user1 = User.builder().id(1L).firstName("Lucas").lastName("Silva").email("lucas.silva@example.com").build();
        var user2 = User.builder().id(2L).firstName("Camila").lastName("Rocha").email("camila.rocha@example.com").build();

        var userProfile1 = UserProfile.builder().id(1L).profile(profile1).user(user1).build();
        var userProfile2 = UserProfile.builder().id(2L).profile(profile1).user(user2).build();


        profileList.addAll(List.of(profile1));
        userList.addAll(List.of(user1, user2));
        userProfileList.addAll(List.of(userProfile1, userProfile2));
    }

    @Test
    @DisplayName("findAll should return all profiles when successful")
    void findAll_ShouldReturnAllProfiles_WhenSuccessful() {
        BDDMockito.when(userProfileRepository.findAll()).thenReturn(userProfileList);

        var result = userProfileService.findAll();

        Assertions.assertThat(result).isNotNull().containsAll(userProfileList);
    }

    @Test
    @DisplayName("findAllUsersByProfileId should return all user when successful")
    void findAllUsersByProfileId_shouldReturnAllUsers_WhenSuccessful() {
        var profileId = 1L;

        BDDMockito.when(userProfileRepository.findAllUsersByProfileId(profileId)).thenReturn(userList);

        var result = userProfileService.findAllUsersByProfileId(profileId);
        Assertions.assertThat(result).isNotNull().containsAll(userList);
    }


}