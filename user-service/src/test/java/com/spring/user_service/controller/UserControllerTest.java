package com.spring.user_service.controller;

import com.spring.user_service.mapper.UserMapperImpl;
import com.spring.user_service.model.User;
import com.spring.user_service.repository.ProfileRepository;
import com.spring.user_service.repository.UserProfileRepository;
import com.spring.user_service.repository.UserRepository;
import com.spring.user_service.service.UserService;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;
import java.util.stream.Stream;

@WebMvcTest(UserController.class)
@Import({UserService.class, UserMapperImpl.class, FileUtils.class})
class UserControllerTest {

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

    private static final String URL = "/v1/users";

    private Set<User> userSet = new HashSet<>();

    @BeforeEach
    void setUp() {
        var u1 = User.builder().id(1L).firstName("Humberto").lastName("Nobrega").email("humbertonobrega@gmail.com").build();
        var u2 = User.builder().id(2L).firstName("Samuel").lastName("Vieira").email("samuelvieira@gmail.com").build();

        userSet = new HashSet<>(List.of(u1, u2));
    }

    @Test
    @DisplayName("GET v1/users should return all elements when successfully")
    void findAll_shouldReturnAllElements_whenSuccessfully() throws Exception {

        BDDMockito.when(userRepository.findAll()).thenReturn(userSet.stream().toList());

        var result = fileUtils.readResourceFile("user/find-all-users-response-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(result));
    }

    @Test
    @DisplayName("GET v1/users/paginated should return all elements paginated")
    void findAllPaginated_shouldReturnAllElements_Paginated() throws Exception {
        var result = fileUtils.readResourceFile("user/find-all-paginated-response-200.json");
        PageRequest pageRequest = PageRequest.of(0, userSet.size());
        PageImpl<User> pageUser = new PageImpl<>(new ArrayList<>(userSet), pageRequest, 1);

        BDDMockito.when(userRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(pageUser);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/paginated"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(result));
    }

    @Test
    @DisplayName("GET v1/users/{id} should return user when successfully")
    void findById_shouldReturnUser_whenSuccessfully() throws Exception {

        var result = fileUtils.readResourceFile("user/find-by-id-response-200.json");
        var id = 1L;
        var user = userSet.stream().filter(u -> u.getId().equals(id)).findFirst();

        BDDMockito.when(userRepository.findById(id)).thenReturn(user);


        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(result));
    }

    @Test
    @DisplayName("GET v1/users/{id} should throw an exception when id does not exists")
    void findById_shouldThrowException_whenIdDoesNotExists() throws Exception {
        var id = 99L;

        BDDMockito.when(userRepository.findById(id)).thenReturn(Optional.empty());


        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("GET v1/users/email?email={} should return user when successfully")
    void findByEmail_shouldReturnUser_whenSuccessfully() throws Exception {
        var email = "humbertonobrega@gmail.com";
        var expectedResult = userSet.stream().filter(u -> u.getEmail().equals(email)).findFirst();
        BDDMockito.when(userRepository.findByEmail(email)).thenReturn(expectedResult);
        var result = fileUtils.readResourceFile("user/find-by-email-response-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/email").param("email", email))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(result));
    }

    @Test
    @DisplayName("GET v1/users/email?email={} should throw an exception when email does not exists")
    void findByEmail_shouldThrowException_whenEmailDoesNotExists() throws Exception {
        var email = "xaxa@gmail.com";

        BDDMockito.when(userRepository.findByEmail(email)).thenReturn(Optional.empty());


        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/email").param("email", email))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("POST v1/users should save a user when successfully")
    void save_shouldSaveUser_whenSuccessfully() throws Exception {

        var user = User.builder()
                .firstName("Joao")
                .lastName("Abraao")
                .email("joao.abraao@example.com").build();

        BDDMockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(user);
        var request = fileUtils.readResourceFile("user/post-request-200.json");
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .contentType("application/json")
                        .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("POST v1/users should throw an exception when the email already exists")
    void save_shouldThrowAnException_whenEmailAlreadyExists() throws Exception {
        BDDMockito.when(userRepository.save(ArgumentMatchers.any())).thenThrow(DataIntegrityViolationException.class);
        var request = fileUtils.readResourceFile("user/post-exists-email-400.json");
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .contentType("application/json")
                        .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE v1/users should delete user when successfully")
    void delete_shouldDeleteUser_whenSuccessfully() throws Exception {
        var id = 1L;

        var user = userSet.stream().filter(u -> u.getId().equals(id)).findFirst();

        BDDMockito.when(userRepository.findById(id)).thenReturn(user);
        BDDMockito.doNothing().when(userRepository).deleteById(id);

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/users should throw an exception when id does not exists")
    void delete_shouldThrowAnException_whenIdDoesNotExists() throws Exception {
        BDDMockito.when(userRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
        BDDMockito.doNothing().when(userRepository).delete(ArgumentMatchers.any());
        var id = 99L;
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("PUT v1/users/{id} should return the object when succesffully")
    void put_shouldReturnObject_whenSuccessfully() throws Exception {
        var toUpdate = userSet.stream().filter(u -> u.getId() == 1L).findFirst().orElse(null);

        BDDMockito.when(userRepository.findById(toUpdate.getId())).thenReturn(Optional.of(toUpdate));

        toUpdate.setFirstName("Joao");
        toUpdate.setLastName("Abraao");
        toUpdate.setEmail("joao.abraao@example.com");
        BDDMockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(toUpdate);

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

        BDDMockito.when(userRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
        BDDMockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(null);

        var request = fileUtils.readResourceFile("user/put-not-exists-id-request-404.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .contentType("application/json")
                        .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @ParameterizedTest
    @MethodSource("postUserBadRequestSource")
    @DisplayName("POST v1/users should throw an exception when fields is empty")
    void save_shouldThrowException_whenFieldsIsEmpty(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile(fileName);

        var mock = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .contentType("application/json")
                        .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mock.getResolvedException();

        Assertions.assertThat(resolvedException.getMessage())
                .contains(errors);
    }

    @ParameterizedTest
    @MethodSource("putUserBadRequestSource")
    @DisplayName("PUT v1/users should throw an exception when fields is empty")
    void put_shouldThrowException_whenFieldsIsEmpty(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile(fileName);

        var mock = mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .contentType("application/json")
                        .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mock.getResolvedException();

        Assertions.assertThat(resolvedException.getMessage())
                .contains(errors);
    }

    private static Stream<Arguments> postUserBadRequestSource() {
        return Stream.of(
                Arguments.of("user/post-empty-fields-request-400.json", allPostRequiredMessages()),
                Arguments.of("user/post-blank-fields-request-400.json", allPostRequiredMessages()),
                Arguments.of("user/post-invalid-email-request-400.json", allInvalidMessages())
        );
    }

    private static Stream<Arguments> putUserBadRequestSource() {
        return Stream.of(
                Arguments.of("user/put-empty-fields-request-400.json", allPutRequiredMessages()),
                Arguments.of("user/put-blank-fields-request-400.json", allPutRequiredMessages()),
                Arguments.of("user/put-invalid-email-request-400.json", allInvalidMessages())
        );
    }

    private static List<String> allPostRequiredMessages() {
        return List.of(
                "firstName is required",
                "lastName is required",
                "email is required");
    }

    private static List<String> allPutRequiredMessages() {
        return List.of("id is required",
                "firstName is required",
                "lastName is required",
                "email is required");
    }

    private static List<String> allInvalidMessages() {
        return List.of("email is not valid");
    }

}