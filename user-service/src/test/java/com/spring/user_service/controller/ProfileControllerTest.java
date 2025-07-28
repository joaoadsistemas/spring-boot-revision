package com.spring.user_service.controller;

import com.spring.user_service.mapper.ProfileMapperImpl;
import com.spring.user_service.model.Profile;
import com.spring.user_service.repository.ProfileRepository;
import com.spring.user_service.repository.UserProfileRepository;
import com.spring.user_service.repository.UserRepository;
import com.spring.user_service.service.ProfileService;
import com.spring.user_service.utils.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@WebMvcTest(ProfileController.class)
@Import({ProfileService.class, ProfileMapperImpl.class, FileUtils.class})
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileUtils fileUtils;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ProfileRepository profileRepository;

    @MockBean
    private UserProfileRepository userProfileRepository;

    private static final String URL = "/v1/profiles";

    private List<Profile> profileList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        var profile1 = Profile.builder().id(1L).name("Administrator").description("Administrator").build();
        var profile2 = Profile.builder().id(2L).name("Normal").description("Normal User").build();

        profileList.addAll(List.of(profile1, profile2));
    }

    @Test
    @DisplayName("GET v1/profile should return all profiles when successful")
    void findAll_shouldReturnAllProfiles_whenSuccessful() throws Exception {

        var result = fileUtils.readResourceFile("/json/profile/find-all-profiles-200.json");

        BDDMockito.when(profileRepository.findAll()).thenReturn(profileList);

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(result))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("GET v1/profile/pageable should return page with profiles when successful")
    void findAllPageable_shouldReturnPageWithProfiles_whenSuccessful() throws Exception {

        var result = fileUtils.readResourceFile("/json/profile/find-all-pageable-200.json");

        var page = PageRequest.of(0, profileList.size());
        var pageProfile = new PageImpl<>(profileList, page, 1);

        BDDMockito.when(profileRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(pageProfile);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/pageable"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(result))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("GET v1/profile/{id} should return the profile when successful")
    void findById_shouldReturnTheProfile_whenSuccessful() throws Exception {

        var profile = profileList.get(0);
        var result = fileUtils.readResourceFile("/json/profile/find-by-id-200.json");

        BDDMockito.when(profileRepository.findById(profile.getId())).thenReturn(Optional.of(profile));

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", profile.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(result))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("GET v1/profile/{id} should throw notFound when id is not found")
    void findById_shouldThrowNotFound_whenIdIsNotFound() throws Exception {

        var profileId = 99L;
        var result = fileUtils.readResourceFile("/json/profile/find-by-id-404.json");

        BDDMockito.when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", profileId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(result))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("GET v1/profile/by-name?name= should return the profile when successful")
    void findByName_shouldReturnTheProfile_whenSuccessful() throws Exception {

        var profile = profileList.get(0);
        var result = fileUtils.readResourceFile("/json/profile/find-by-name-200.json");

        BDDMockito.when(profileRepository.findByNameIgnoreCase(profile.getName())).thenReturn(Optional.of(profile));

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/by-name").param("name", profile.getName()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(result))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("GET v1/profile/by-name?name= should throw notFound when name is not found")
    void findByName_shouldThrowNotFound_whenNameIsNotFound() throws Exception {

        var profileName = "xaxa";
        var result = fileUtils.readResourceFile("/json/profile/find-by-name-404.json");

        BDDMockito.when(profileRepository.findByNameIgnoreCase(profileName)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/by-name").param("name", profileName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(result))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("POST v1/profile should save when successful")
    void save_shouldSave_whenSuccessful() throws Exception {

        var profile = Profile.builder().name("Another Profile").description("Another Description").build();
        var request = fileUtils.readResourceFile("/json/profile/save-profile-request-200.json");

        BDDMockito.when(profileRepository.save(ArgumentMatchers.any(Profile.class))).thenReturn(profile);

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .contentType("application/json")
                        .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @ParameterizedTest
    @MethodSource("postProfileBadRequestSource")
    @DisplayName("POST v1/profile should validate the fields")
    void save_shouldValidateTheFields(String resourceFile, List<String> validations) throws Exception {

        var request = fileUtils.readResourceFile(resourceFile);

        var mock = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .contentType("application/json")
                        .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mock.getResolvedException();

        Assertions.assertThat(resolvedException.getMessage())
                .contains(validations);
    }

    private static Stream<Arguments> postProfileBadRequestSource() {
        return Stream.of(
                Arguments.of("/json/profile/save-profile-blank-400.json", allPostNotBlankMessages()),
                Arguments.of("/json/profile/save-profile-null-400.json", allPostNotNullMessages())
        );
    }

    private static List<String> allPostNotBlankMessages() {
        return List.of(
                "name can not be blank",
                "description can not be blank");
    }

    private static List<String> allPostNotNullMessages() {
        return List.of(
                "name can not be null",
                "description can not be null");
    }

}