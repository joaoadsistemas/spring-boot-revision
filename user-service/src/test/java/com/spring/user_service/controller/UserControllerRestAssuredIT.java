package com.spring.user_service.controller;

import com.spring.user_service.config.IntegrationTestsConfig;
import com.spring.user_service.config.RestAssuredConfig;
import com.spring.user_service.utils.FileUtils;
import com.spring.user_service.utils.user.SqlUserDataSetup;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import net.javacrumbs.jsonunit.core.Option;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestAssuredConfig.class)
@SqlUserDataSetup
class UserControllerRestAssuredIT extends IntegrationTestsConfig {
    private static final String URL = "/v1/users";

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    @Qualifier(value = "requestSpecificationAdminUser")
    private RequestSpecification requestSpecificationAdminUser;

    @Autowired
    @Qualifier(value = "requestSpecificationRegularUser")
    private RequestSpecification requestSpecificationRegularUser;

    @BeforeEach
    void setUp() {
        RestAssured.requestSpecification = requestSpecificationRegularUser;
    }

    @Test
    @SqlUserDataSetup
    @DisplayName("GET v1/users should return all elements when successfully")
    void findAll_shouldReturnAllElements_whenSuccessfully() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get(URL))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.content().json(result))
//                .andExpect(MockMvcResultMatchers.status().isOk());

        RestAssured.requestSpecification = requestSpecificationAdminUser;

        var result = fileUtils.readResourceFile("json/user/find-all-users-response-200.json");

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
    @SqlUserDataSetup
    @DisplayName("GET v1/users/paginated should return all elements paginated")
    void findAllPaginated_shouldReturnAllElements_Paginated() throws Exception {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(URL + "/paginated")
                .then()
                .statusCode(HttpStatus.OK.value());
    }


    @Test
    @SqlUserDataSetup
    @DisplayName("GET v1/users/{id} should return user when successfully")
    void findById_shouldReturnUser_whenSuccessfully() throws Exception {
        var result = fileUtils.readResourceFile("/json/user/find-by-id-response-200.json");

        RestAssured.given()
                .pathParam("userId", 1)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(URL + "/{userId}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(result));

    }

    @Test
    @DisplayName("GET v1/users/{id} should throw an exception when id does not exists")
    void findById_shouldThrowException_whenIdDoesNotExists() throws Exception {

        RestAssured.requestSpecification = requestSpecificationAdminUser;

        var result = fileUtils.readResourceFile("/json/user/find-by-id-response-404.json");

        RestAssured.given()
                .pathParam("userId", 99)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(URL + "/{userId}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(result));
    }

    @Test
    @SqlUserDataSetup
    @DisplayName("GET v1/users/email?email={} should return user when successfully")
    void findByEmail_shouldReturnUser_whenSuccessfully() throws Exception {
        var result = fileUtils.readResourceFile("/json/user/find-by-email-response-200.json");

        RestAssured.given()
                .param("email", "regularuser@gmail.com")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(URL + "/email")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(result));

    }

    @Test
    @DisplayName("GET v1/users/email?email={} should throw an exception when email does not exists")
    void findByEmail_shouldThrowException_whenEmailDoesNotExists() throws Exception {
        var result = fileUtils.readResourceFile("/json/user/find-by-email-response-404.json");

        RestAssured.given()
                .param("email", "xaxa@gmail.com")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(URL + "/email")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(result));

    }

    @Test
    @SqlUserDataSetup
    @DisplayName("POST v1/users should save a user when successfully")
    void save_shouldSaveUser_whenSuccessfully() throws Exception {
        var request = fileUtils.readResourceFile("/json/user/save-user-request-200.json");

        RestAssured.given()
                .body(request)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @SqlUserDataSetup
    @DisplayName("POST v1/users should throw an exception when the email already exists")
    void save_shouldThrowAnException_whenEmailAlreadyExists() throws Exception {
        var request = fileUtils.readResourceFile("json/user/post-exists-email-400.json");

        RestAssured.given()
                .body(request)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @SqlUserDataSetup
    @DisplayName("DELETE v1/users should delete user when successfully")
    void delete_shouldDeleteUser_whenSuccessfully() {

        RestAssured.requestSpecification = requestSpecificationAdminUser;

        RestAssured.given()
                .contentType(ContentType.JSON)
                .pathParam("userId", 1)
                .accept(ContentType.JSON)
                .when()
                .delete(URL + "/{userId}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @SqlUserDataSetup
    @DisplayName("DELETE v1/users should throw an exception when id does not exists")
    void delete_shouldThrowAnException_whenIdDoesNotExists() throws IOException {

        RestAssured.requestSpecification = requestSpecificationAdminUser;

        var result = fileUtils.readResourceFile("/json/user/find-by-id-response-404.json");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .pathParam("userId", 99)
                .accept(ContentType.JSON)
                .when()
                .delete(URL + "/{userId}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(result));
    }

    @Test
    @SqlUserDataSetup
    @DisplayName("PUT v1/users should return the object when succesffully")
    void put_shouldReturnObject_whenSuccessfully() throws Exception {

        var request = fileUtils.readResourceFile("/json/user/put-request-200.json");
        var response = fileUtils.readResourceFile("/json/user/put-response-200.json");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .accept(ContentType.JSON)
                .when()
                .put(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(response));

    }

    @Test
    @DisplayName("PUT v1/users/ should throw an exception when id does not exists")
    void put_shouldThrowAnException_whenIdDoesNotExists() throws Exception {
        var request = fileUtils.readResourceFile("json/user/put-not-exists-id-request-404.json");
        var response = fileUtils.readResourceFile("/json/user/put-not-exists-id-response-404.json");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .accept(ContentType.JSON)
                .when()
                .put(URL)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(response));
    }

    @ParameterizedTest
    @MethodSource("postUserBadRequestSource")
    @DisplayName("POST v1/users should throw an exception when fields is empty")
    void save_shouldThrowException_whenFieldsIsEmpty(String fileRequest, String fileResponse) throws Exception {
        var request = fileUtils.readResourceFile(fileRequest);
        var response = fileUtils.readResourceFile(fileResponse);

        String string = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .accept(ContentType.JSON)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().body().asString();

        JsonAssertions.assertThatJson(string)
                .whenIgnoringPaths("timestamp")
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(response);
    }

    @ParameterizedTest
    @MethodSource("putUserBadRequestSource")
    @DisplayName("PUT v1/users should throw an exception when fields is empty")
    void put_shouldThrowException_whenFieldsIsEmpty(String fileName) throws Exception {
        var request = fileUtils.readResourceFile(fileName);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .accept(ContentType.JSON)
                .when()
                .put(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }


    private static Stream<Arguments> postUserBadRequestSource() {
        return Stream.of(
                Arguments.of("json/user/post-empty-fields-request-400.json", "json/user/post-empty-fields-response-400.json"),
                Arguments.of("json/user/post-blank-fields-request-400.json", "json/user/post-blank-fields-response-400.json"),
                Arguments.of("json/user/post-invalid-email-request-400.json", "json/user/post-invalid-email-response-400.json")
        );
    }

    private static Stream<Arguments> putUserBadRequestSource() {
        return Stream.of(
                Arguments.of("json/user/put-empty-fields-request-400.json", "json/user/put-empty-fields-response-400.json"),
                Arguments.of("json/user/put-blank-fields-request-400.json", "json/user/put-blank-fields-response-400.json"),
                Arguments.of("json/user/put-invalid-email-request-400.json", "json/user/put-invalid-email-response-400.json")
        );
    }

}
