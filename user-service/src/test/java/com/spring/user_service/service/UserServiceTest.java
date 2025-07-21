package com.spring.user_service.service;

import com.spring.user_service.model.User;
import com.spring.user_service.repository.UserHardCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserHardCodeRepository userHardCodeRepository;

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
    @DisplayName("findAll should return all elements when successfully")
    void findAll_shouldReturnAllElements_whenSuccessfully() {
        BDDMockito.when(userHardCodeRepository.findAll()).thenReturn(userSet);

        var result = userService.findAll();
        assertThat(result).containsAll(userSet);
    }

    @Test
    @DisplayName("findById should return user when successfully")
    void findById_shouldReturnUser_whenSuccessfully() {
        var expectedResult = userSet.stream().filter(u -> u.getId().equals(1L)).findFirst().get();
        BDDMockito.when(userHardCodeRepository.findById(ArgumentMatchers.anyLong())).thenReturn(expectedResult);
        var result = userService.findById(expectedResult.getId());
        assertThat(result).isNotNull().isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("findById should throw an exception when id does not exists")
    void findById_shouldThrowException_whenIdDoesNotExists() {
        BDDMockito.when(userHardCodeRepository.findById(ArgumentMatchers.anyLong())).thenThrow(ResponseStatusException.class);
        var id = 99L;
        assertThatThrownBy(() -> userService.findById(id)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("findByEmail should return user when successfully")
    void findByEmail_shouldReturnUser_whenSuccessfully() {
        var expectedResult = userSet.stream().filter(u -> u.getId().equals(1L)).findFirst().get();
        BDDMockito.when(userHardCodeRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(expectedResult);
        var result = userService.findByEmail(expectedResult.getEmail());
        assertThat(result).isNotNull().isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("findByEmail should throw an exception when email does not exists")
    void findByEmail_shouldThrowException_whenEmailDoesNotExists() {
        BDDMockito.when(userHardCodeRepository.findByEmail(ArgumentMatchers.anyString())).thenThrow(ResponseStatusException.class);
        var email = "xaxa@gmail.com";
        assertThatThrownBy(() -> userService.findByEmail(email)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save should save a user when successfully")
    void save_shouldSaveUser_whenSuccessfully() {
        var expectedResult = User.builder().id(6L).firstName("Joao").lastName("Silva").email("joao.silva@example.com").build();
        BDDMockito.doNothing().when(userHardCodeRepository).save(ArgumentMatchers.any(User.class));
        assertThatNoException().isThrownBy(() -> userService.save(expectedResult));
    }

    @Test
    @DisplayName("save should throw an exception when the email already exists")
    void save_shouldThrowAnException_whenEmailAlreadyExists() {
        BDDMockito.doThrow(ResponseStatusException.class).when(userHardCodeRepository).save(ArgumentMatchers.any(User.class));
        var user = userSet.stream().findFirst().get();
        user.setId(99L);
        user.setFirstName("Nicolas");
        user.setLastName("Machado");

        assertThatThrownBy(() -> userService.save(user)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save should throw an exception when the id already exists")
    void save_shouldThrowException_whenIdAlreadyExists() {
        BDDMockito.doThrow(ResponseStatusException.class).when(userHardCodeRepository).save(ArgumentMatchers.any(User.class));

        var user = userSet.stream().findFirst().get();
        user.setEmail("nicolasmachado@gmail.com");
        user.setFirstName("Nicolas");
        user.setLastName("Machado");

        assertThatThrownBy(() -> userService.save(user)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("delete should delete user when successfully")
    void delete_shouldDeleteUser_whenSuccessfully() {
        var removedUser = userSet.stream().findFirst().get();

        BDDMockito.doNothing().when(userHardCodeRepository).delete(ArgumentMatchers.anyLong());

        assertThatNoException().isThrownBy(() -> userService.delete(removedUser.getId()));
    }

    @Test
    @DisplayName("delete should throw an exception when id does not exists")
    void delete_shouldThrowAnException_whenIdDoesNotExists() {
        BDDMockito.doThrow(ResponseStatusException.class).when(userHardCodeRepository).delete(ArgumentMatchers.anyLong());
        var id = 99L;
        assertThatThrownBy(() -> userService.delete(id)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("put should return the object when succesffully")
    void put_shouldReturnObject_whenSuccessfully() {

        var userBeforeModification = userSet.stream().findFirst().get();

        BDDMockito.when(userHardCodeRepository.findById(userBeforeModification.getId())).thenReturn(userBeforeModification);

        var userResultBeforeModification = userService.findById(userBeforeModification.getId());
        assertThat(userResultBeforeModification).isNotNull().isEqualTo(userBeforeModification);

        var userAfterModification = userBeforeModification;

        userAfterModification.setFirstName("Kaio");
        userAfterModification.setLastName("Ribeiro");
        userAfterModification.setEmail("kaioribeiro@gmail.com");

        userSet.add(userAfterModification);

        BDDMockito.when(userHardCodeRepository.update(ArgumentMatchers.any(User.class))).thenReturn(userAfterModification);

        var userResultAfterModification = userService.update(userAfterModification);

        assertThat(userResultAfterModification).isNotNull().isEqualTo(userAfterModification);
        assertThat(userAfterModification.getId()).isEqualTo(userBeforeModification.getId());
    }

    @Test
    @DisplayName("put should throw an exception when id does not exists")
    void put_shouldThrowAnException_whenIdDoesNotExists() {

        var user = User.builder().id(99L).email("xaxa").firstName("xaxa").lastName("xaxa").build();
        BDDMockito.when(userHardCodeRepository.update(ArgumentMatchers.any(User.class))).thenThrow(ResponseStatusException.class);

        assertThatThrownBy(() -> userService.update(user)).isInstanceOf(ResponseStatusException.class);
    }
}