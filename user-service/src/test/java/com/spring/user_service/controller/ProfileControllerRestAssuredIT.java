package com.spring.user_service.controller;

import com.spring.user_service.config.IntegrationTestsConfig;
import com.spring.user_service.dto.profile.response.ProfileGetResponse;
import com.spring.user_service.utils.CleanProfileAfterTest;
import com.spring.user_service.utils.FileUtils;
import com.spring.user_service.utils.SqlProfileDataSetup;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.assertj.core.api.Assertions;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProfileControllerRestAssuredIT extends IntegrationTestsConfig {
    private static final String URL = "/v1/profiles";

    @Autowired
    private FileUtils fileUtils;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        RestAssured.port = port;
    }

    @Test
    @SqlProfileDataSetup
    @DisplayName("GET v1/profile should return all profiles when successful")
    void findAll_shouldReturnAllProfiles_whenSuccessful() throws IOException {
//        mockMvc.perform(MockMvcRequestBuilders.get(URL))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.content().json(result))
//                .andExpect(MockMvcResultMatchers.status().isOk());


        var result = fileUtils.readResourceFile("/profile/find-all-profiles-200.json");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(result));

    }

    @Test
    @CleanProfileAfterTest
    @DisplayName("GET v1/profile should return empty when doesnt have profile")
    void findAll_shouldReturnEmpty_whenDoesntHaveProfile() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo("[]"));
    }


    @Test
    @SqlProfileDataSetup
    @DisplayName("GET v1/profile/{id} should return the profile when successful")
    void findById_shouldReturnTheProfile_whenSuccessful() throws IOException {
        var result = fileUtils.readResourceFile("/profile/find-by-id-200.json");

        RestAssured.given()
                .pathParam("profileId", 1)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(URL + "/{profileId}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(result));

    }

    @Test
    @CleanProfileAfterTest
    @DisplayName("GET v1/profile/{id} should throw notFound when id is not found")
    void findById_shouldThrowNotFound_whenIdIsNotFound() throws IOException {
        var result = fileUtils.readResourceFile("/profile/find-by-id-404.json");

        RestAssured.given()
                .pathParam("profileId", 99)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(URL + "/{profileId}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(result));
    }

    @Test
    @SqlProfileDataSetup
    @DisplayName("GET v1/profile/by-name?name= should return the profile when successful")
    void findByName_shouldReturnTheProfile_whenSuccessful() throws IOException {
        var result = fileUtils.readResourceFile("/profile/find-by-name-200.json");

        RestAssured.given()
                .param("name", "administrator")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(URL + "/by-name")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(result));

    }

    @Test
    @CleanProfileAfterTest
    @DisplayName("GET v1/profile/by-name?name= should throw notFound when name is not found")
    void findByName_shouldThrowNotFound_whenNameIsNotFound() throws IOException {
        var result = fileUtils.readResourceFile("/profile/find-by-name-404.json");

        RestAssured.given()
                .param("name", "xaxa")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(URL + "/by-name")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(result));
    }

    @Test
    @CleanProfileAfterTest
    @DisplayName("POST v1/profile should save when successful")
    void save_shouldSave_whenSuccessful() throws IOException {
        var request = fileUtils.readResourceFile("/profile/save-profile-request-200.json");

        RestAssured.given()
                .body(request)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @ParameterizedTest
    @MethodSource("postProfileBadRequestSource")
    @DisplayName("POST v1/profile should validate the fields")
    void save_shouldValidateTheFields(String resourceFile, String responseFile) throws Exception {

        var request = fileUtils.readResourceFile(resourceFile);
        var expectedResponse = fileUtils.readResourceFile(responseFile);

        var actualResponse = RestAssured.given()
                .body(request)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .asString();

        JsonAssertions.assertThatJson(actualResponse).whenIgnoringPaths("timestamp")
                .isEqualTo(expectedResponse);

    }

    private static Stream<Arguments> postProfileBadRequestSource() {
        return Stream.of(
                Arguments.of("/profile/save-profile-blank-400.json", "/profile/save-profile-blank-response-400.json"),
                Arguments.of("/profile/save-profile-null-400.json", "/profile/save-profile-blank-response-400.json"));
    }

}
