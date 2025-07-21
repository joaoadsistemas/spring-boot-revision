package com.spring.user_service.repository;

import com.spring.user_service.data.UserData;
import com.spring.user_service.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class UserHardCodeRepositoryTest {

    @InjectMocks
    private UserHardCodeRepository userHardCodeRepository;

    @Mock
    private UserData userData;

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
        BDDMockito.when(userData.getUsers()).thenReturn(userSet);

        var result = userHardCodeRepository.findAll();
        assertThat(result).containsAll(userSet);
    }

    @Test
    @DisplayName("findById should return user when successfully")
    void findById_shouldReturnUser_whenSuccessfully() {
        BDDMockito.when(userData.getUsers()).thenReturn(userSet);
        var expectedResult = userSet.stream().filter(u -> u.getId().equals(1L)).findFirst().get();
        var result = userHardCodeRepository.findById(expectedResult.getId());
        assertThat(result).isNotNull().isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("findById should throw an exception when id does not exists")
    void findById_shouldThrowException_whenIdDoesNotExists() {
        BDDMockito.when(userData.getUsers()).thenReturn(userSet);
        var id = 99L;
        assertThatThrownBy(() -> userHardCodeRepository.findById(id)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("findByEmail should return user when successfully")
    void findByEmail_shouldReturnUser_whenSuccessfully() {
        BDDMockito.when(userData.getUsers()).thenReturn(userSet);
        var expectedResult = userSet.stream().filter(u -> u.getId().equals(1L)).findFirst().get();
        var result = userHardCodeRepository.findByEmail(expectedResult.getEmail());
        assertThat(result).isNotNull().isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("findByEmail should throw an exception when email does not exists")
    void findByEmail_shouldThrowException_whenEmailDoesNotExists() {
        BDDMockito.when(userData.getUsers()).thenReturn(userSet);
        var email = "xaxa@gmail.com";
        assertThatThrownBy(() -> userHardCodeRepository.findByEmail(email)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save should save a user when successfully")
    void save_shouldSaveUser_whenSuccessfully() {
        BDDMockito.when(userData.getUsers()).thenReturn(userSet);
        var expectedResult = User.builder().id(6L).firstName("Joao").lastName("Silva").email("joao.silva@example.com").build();
        assertThatNoException().isThrownBy(() -> userHardCodeRepository.save(expectedResult));
        assertThat(userHardCodeRepository.findById(6L)).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("save should throw an exception when the email already exists")
    void save_shouldThrowAnException_whenEmailAlreadyExists() {
        BDDMockito.when(userData.getUsers()).thenReturn(userSet);

        var user = userSet.stream().findFirst().get();
        user.setId(99L);
        user.setFirstName("Nicolas");
        user.setLastName("Machado");

        assertThatThrownBy(() -> userHardCodeRepository.save(user)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save should throw an exception when the id already exists")
    void save_shouldThrowException_whenIdAlreadyExists() {
        BDDMockito.when(userData.getUsers()).thenReturn(userSet);

        var user = userSet.stream().findFirst().get();
        user.setEmail("nicolasmachado@gmail.com");
        user.setFirstName("Nicolas");
        user.setLastName("Machado");

        assertThatThrownBy(() -> userHardCodeRepository.save(user)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("delete should delete user when successfully")
    void delete_shouldDeleteUser_whenSuccessfully() {
        BDDMockito.when(userData.getUsers()).thenReturn(userSet);

        var removedUser = userSet.stream().findFirst().get();

        assertThatNoException().isThrownBy(() -> userHardCodeRepository.delete(removedUser.getId()));
        assertThat(userSet).doesNotContain(removedUser);
    }

    @Test
    @DisplayName("delete should throw an exception when id does not exists")
    void delete_shouldThrowAnException_whenIdDoesNotExists() {
        BDDMockito.when(userData.getUsers()).thenReturn(userSet);
        var id = 99L;
        assertThatThrownBy(() -> userHardCodeRepository.delete(id)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("put should return the object when succesffully")
    void put_shouldReturnObject_whenSuccessfully() {

        var userBeforeModification = userSet.stream().findFirst().get();

        BDDMockito.when(userData.getUsers()).thenReturn(userSet);

        var userResultBeforeModification = userHardCodeRepository.findById(userBeforeModification.getId());
        assertThat(userResultBeforeModification).isNotNull().isEqualTo(userBeforeModification);

        var userAfterModification = userBeforeModification;

        userAfterModification.setFirstName("Kaio");
        userAfterModification.setLastName("Ribeiro");
        userAfterModification.setEmail("kaioribeiro@gmail.com");

        userSet.add(userAfterModification);

        BDDMockito.when(userData.getUsers()).thenReturn(userSet);

        var userResultAfterModification = userHardCodeRepository.update(userAfterModification);

        assertThat(userResultAfterModification).isNotNull().isEqualTo(userAfterModification);
        assertThat(userAfterModification.getId()).isEqualTo(userBeforeModification.getId());
    }

    @Test
    @DisplayName("put should throw an exception when id does not exists")
    void put_shouldThrowAnException_whenIdDoesNotExists() {

        var user = User.builder().id(99L).email("xaxa").firstName("xaxa").lastName("xaxa").build();
        BDDMockito.when(userData.getUsers()).thenReturn(userSet);

        assertThatThrownBy(() -> userHardCodeRepository.update(user)).isInstanceOf(ResponseStatusException.class);
    }
}