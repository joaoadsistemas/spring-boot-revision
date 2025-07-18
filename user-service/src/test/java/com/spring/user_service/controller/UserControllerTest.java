package com.spring.user_service.controller;

import com.spring.user_service.data.UserData;
import com.spring.user_service.mapper.UserMapperImpl;
import com.spring.user_service.model.User;
import com.spring.user_service.repository.UserRepository;
import com.spring.user_service.service.UserService;
import com.spring.user_service.utils.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebMvcTest(UserController.class)
@Import({UserService.class, UserRepository.class, UserMapperImpl.class, UserData.class, FileUtils.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileUtils fileUtils;

    @MockBean
    private UserData userData;

    private static final String URL = "/v1/users";

    private Set<User> userSet = new HashSet<>();

    @BeforeEach
    void setUp() {
        var u1 = User.builder().id(1L).firstName("Lucas").lastName("Silva").email("lucas.silva@example.com").build();
        var u2 = User.builder().id(2L).firstName("Camila").lastName("Rocha").email("camila.rocha@example.com").build();
        var u3 = User.builder().id(3L).firstName("Daniel").lastName("Souza").email("daniel.souza@example.com").build();
        var u4 = User.builder().id(4L).firstName("Fernanda").lastName("Mendes").email("fernanda.mendes@example.com").build();
        var u5 = User.builder().id(5L).firstName("Rafael").lastName("Almeida").email("rafael.almeida@example.com").build();

        userSet = new HashSet<>(List.of(u1, u2, u3, u4, u5));
    }

    @Test
    @DisplayName("GET v1/users should return all elements when successfully")
    void findAll_shouldReturnAllElements_whenSuccessfully() throws Exception {

        BDDMockito.when(userData.getUsers()).thenReturn(userSet);

        var result = fileUtils.readResourceFile("user/find-all-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(result));
    }

    @Test
    @DisplayName("GET v1/users/{id} should return user when successfully")
    void findById_shouldReturnUser_whenSuccessfully() throws Exception {

        BDDMockito.when(userData.getUsers()).thenReturn(userSet);

        var result = fileUtils.readResourceFile("user/find-by-id-200.json");
        var id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(result));
    }

    @Test
    @DisplayName("GET v1/users/{id} should throw an exception when id does not exists")
    void findById_shouldThrowException_whenIdDoesNotExists() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userSet);
        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("GET v1/users/email?email={} should return user when successfully")
    void findByEmail_shouldReturnUser_whenSuccessfully() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userSet);

        var result = fileUtils.readResourceFile("user/find-by-email-200.json");
        var email = "lucas.silva@example.com";

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/email").param("email", email))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(result));
    }

    @Test
    @DisplayName("GET v1/users/email?email={} should throw an exception when email does not exists")
    void findByEmail_shouldThrowException_whenEmailDoesNotExists() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userSet);
        var email = "xaxa@gmail.com";

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/email").param("email", email))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("POST v1/users should save a user when successfully")
    void save_shouldSaveUser_whenSuccessfully() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userSet);
        var request = fileUtils.readResourceFile("user/post-200.json");
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .contentType("application/json")
                        .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("POST v1/users should throw an exception when the email already exists")
    void save_shouldThrowAnException_whenEmailAlreadyExists() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userSet);
        var request = fileUtils.readResourceFile("user/post-exists-email-400.json");
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .contentType("application/json")
                        .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("POST v1/users should throw an exception when the id already exists")
    void save_shouldThrowException_whenIdAlreadyExists() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userSet);
        var request = fileUtils.readResourceFile("user/post-exists-id-400.json");

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .contentType("application/json")
                        .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE v1/users should delete user when successfully")
    void delete_shouldDeleteUser_whenSuccessfully() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userSet);
        var id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/users should throw an exception when id does not exists")
    void delete_shouldThrowAnException_whenIdDoesNotExists() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userSet);
        var id = 99L;
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("PUT v1/users/{id} should return the object when succesffully")
    void put_shouldReturnObject_whenSuccessfully() throws Exception {
        BDDMockito.when(userData.getUsers()).thenReturn(userSet);

        var request = fileUtils.readResourceFile("user/put-request-200.json");
        var response = fileUtils.readResourceFile("user/put-response-200.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .contentType("application/json")
                        .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("PUT v1/users/{id} should throw an exception when id does not exists")
    void put_shouldThrowAnException_whenIdDoesNotExists() throws Exception {

        BDDMockito.when(userData.getUsers()).thenReturn(userSet);

        var request = fileUtils.readResourceFile("user/put-exists-id-request-400.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .contentType("application/json")
                        .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @DisplayName("POST v1/users should throw an exception when fields is not valid")
    void save_shouldThrowException_whenFieldsIsNotValid() throws Exception {
        var request = fileUtils.readResourceFile("user/post-empty-fields-400.json");

        var idMessage = "id is required";
        var firstNameMessage = "firstName is required";
        var lastNameMessage = "lastName is required";
        var emailMessage = "email is required";

        var mock = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .contentType("application/json")
                        .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mock.getResolvedException();

        Assertions.assertThat(resolvedException.getMessage())
                .contains(idMessage, firstNameMessage, lastNameMessage, emailMessage);
    }

    @Test
    @DisplayName("PUT v1/users should throw an exception when fields is not valid")
    void put_shouldThrowException_whenFieldsIsNotValid() throws Exception {
        var request = fileUtils.readResourceFile("user/put-empty-fields-400.json");

        var idMessage = "id is required";
        var firstNameMessage = "firstName is required";
        var lastNameMessage = "lastName is required";
        var emailMessage = "email is required";

        var mock = mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .contentType("application/json")
                        .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mock.getResolvedException();

        Assertions.assertThat(resolvedException.getMessage())
                .contains(idMessage, firstNameMessage, lastNameMessage, emailMessage);
    }

}