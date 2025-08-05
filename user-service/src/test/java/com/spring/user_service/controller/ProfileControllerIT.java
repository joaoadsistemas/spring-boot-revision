package com.spring.user_service.controller;

import com.spring.dto.ProfileGetResponse;
import com.spring.user_service.config.IntegrationTestsConfig;
import com.spring.user_service.config.TestRestTemplateConfig;
import com.spring.user_service.utils.FileUtils;
import com.spring.user_service.utils.profile.CleanProfileAfterTest;
import com.spring.user_service.utils.profile.SqlProfileDataSetup;
import com.spring.user_service.utils.user.CleanUserAfterTest;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import net.javacrumbs.jsonunit.core.Option;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestRestTemplateConfig.class)
@Sql(value = "/sql/user/init_one_login_regular_user.sql")
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@CleanUserAfterTest
class ProfileControllerIT extends IntegrationTestsConfig {
    private static final String URL = "/v1/profiles";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FileUtils fileUtils;

    @Test
    @SqlProfileDataSetup
    @DisplayName("GET v1/profile should return all profiles when successful")
    void findAll_shouldReturnAllProfiles_whenSuccessful() {
        System.out.println(new BCryptPasswordEncoder().matches("test", "{bcrypt}$2a$10$6lUMl3EclqLaftu.GO.vNOj6yikvCUBY/.vS0ztc4ST72vORz7nKm"));
        var typeReference = new ParameterizedTypeReference<Set<ProfileGetResponse>>() {
        };
        var responseEntity = restTemplate.exchange(URL, HttpMethod.GET, null, typeReference);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody().size()).isEqualTo(2);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("GET v1/profile should return empty when doesnt have profile")
    void findAll_shouldReturnEmpty_whenDoesntHaveProfile() {
        var typeReference = new ParameterizedTypeReference<Set<ProfileGetResponse>>() {
        };
        var responseEntity = restTemplate.exchange(URL, HttpMethod.GET, null, typeReference);
        Assertions.assertThat(responseEntity.getBody()).isNotNull().isEmpty();
    }

    @Test
    @SqlProfileDataSetup
    @DisplayName("GET v1/profile/{id} should return the profile when successful")
    void findById_shouldReturnTheProfile_whenSuccessful() {
        var typeReference = new ParameterizedTypeReference<ProfileGetResponse>() {
        };
        var responseEntity = restTemplate.exchange(URL + "/" + 1, HttpMethod.GET, null, typeReference);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("GET v1/profile/{id} should throw notFound when id is not found")
    void findById_shouldThrowNotFound_whenIdIsNotFound() {
        var typeReference = new ParameterizedTypeReference<ProfileGetResponse>() {
        };
        var responseEntity = restTemplate.exchange(URL + "/" + 1, HttpMethod.GET, null, typeReference);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @SqlProfileDataSetup
    @DisplayName("GET v1/profile/by-name?name= should return the profile when successful")
    void findByName_shouldReturnTheProfile_whenSuccessful() {
        var typeReference = new ParameterizedTypeReference<ProfileGetResponse>() {
        };
        var responseEntity = restTemplate.exchange(URL + "/by-name?name=normal", HttpMethod.GET, null, typeReference);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("GET v1/profile/by-name?name= should throw notFound when name is not found")
    void findByName_shouldThrowNotFound_whenNameIsNotFound() {
        var typeReference = new ParameterizedTypeReference<ProfileGetResponse>() {
        };
        var responseEntity = restTemplate.exchange(URL + "/by-name?name=normal", HttpMethod.GET, null, typeReference);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @CleanProfileAfterTest
    @DisplayName("POST v1/profile should save when successful")
    void save_shouldSave_whenSuccessful() throws IOException {
        var request = fileUtils.readResourceFile("json/profile/save-profile-request-200.json");
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var requestEntity = new HttpEntity<>(request, headers);
        var responseEntity = restTemplate.exchange(URL, HttpMethod.POST, requestEntity, Void.class);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @ParameterizedTest
    @MethodSource("postProfileBadRequestSource")
    @CleanProfileAfterTest
    @DisplayName("POST v1/profile should validate the fields")
    void save_shouldSave_whenSuccessful(String resourceFile, String responseFile) throws Exception {

        var request = fileUtils.readResourceFile(resourceFile);
        var expectedResponse = fileUtils.readResourceFile(responseFile);
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var requestEntity = new HttpEntity<>(request, headers);

        var responseEntity = restTemplate.exchange(URL, HttpMethod.POST, requestEntity, String.class);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();

        JsonAssertions.assertThatJson(responseEntity.getBody())
                .when(Option.IGNORING_ARRAY_ORDER)
                .whenIgnoringPaths("timestamp")
                .isEqualTo(expectedResponse);
    }

    private static Stream<Arguments> postProfileBadRequestSource() {
        return Stream.of(
                Arguments.of("/json/profile/save-profile-blank-fields-request-400.json", "/json/profile/save-profile-blank-response-400.json"),
                Arguments.of("/json/profile/save-profile-empty-fields-request-400.json", "/json/profile/save-profile-empty-fields-response-400.json"));
    }

}
