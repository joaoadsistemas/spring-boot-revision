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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;


@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @InjectMocks
    private UserRepository userRepository;

    @Mock
    private UserData userData;

    private Set<User> userList = new HashSet<>();


    @BeforeEach
    void setUp() {
        var u1 = User.builder().id(1L).firstName("Lucas").lastName("Silva").email("lucas.silva@example.com").build();
        var u2 = User.builder().id(2L).firstName("Camila").lastName("Rocha").email("camila.rocha@example.com").build();
        var u3 = User.builder().id(3L).firstName("Daniel").lastName("Souza").email("daniel.souza@example.com").build();
        var u4 = User.builder().id(4L).firstName("Fernanda").lastName("Mendes").email("fernanda.mendes@example.com").build();
        var u5 = User.builder().id(5L).firstName("Rafael").lastName("Almeida").email("rafael.almeida@example.com").build();

        userList = new HashSet<>(List.of(u1, u2, u3, u4, u5));
    }

    @Test
    @DisplayName("findAll should return all elements when successfully")
    void findAll_shouldReturnAllElements_whenSuccessfully() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var result = userRepository.findAll();
        assertThat(result).containsAll(userList);
    }

    @Test
    @DisplayName("findById should return user when successfully")
    void findById_shouldReturnUser_whenSuccessfully() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var expectedResult = userList.stream().filter(u -> u.getId().equals(1L)).findFirst();
        var result = userRepository.findById(1L);
        assertThat(result).isNotNull().isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("findById should return an optional empty when id does not exists")
    void findById_shouldReturnEmptyOptional_whenIdDoesNotExist() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var id = 99L;
        var result = userRepository.findById(id);
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByEmail should return an optional empty when email does not exists")
    void findByEmail_shouldReturnEmptyOptional_whenEmailDoesNotExist() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var email = "xaxa@gmail.com";
        var result = userRepository.findByEmail(email);
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("save should save a user when successfully")
    void save_shouldSaveUser_whenSuccessfully() {
        var expectedResult = User.builder().id(6L).firstName("Joao").lastName("Silva").email("joao.silva@example.com").build();
        userList.add(expectedResult);
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        assertThatNoException().isThrownBy(() -> userRepository.save(expectedResult));
        assertThat(userRepository.findById(6L)).contains(expectedResult);
    }

    @Test
    @DisplayName("save should do nothing when the email already exists")
    void save_shouldDoNothing_whenEmailAlreadyExists() {
        var initialCount = userList.size();
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var user = userList.stream().findFirst().get();
        user.setId(99L);
        user.setFirstName("Nicolas");
        user.setLastName("Machado");

        assertThatNoException().isThrownBy(() -> userRepository.save(user));
        var finalCount = userList.size();
        assertThat(initialCount).isEqualTo(finalCount);
    }

    @Test
    @DisplayName("save should do nothing when the id already exists")
    void save_shouldDoNothing_whenIdAlreadyExists() {
        var initialCount = userList.size();
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var user = userList.stream().findFirst().get();
        user.setEmail("nicolasmachado@gmail.com");
        user.setFirstName("Nicolas");
        user.setLastName("Machado");

        assertThatNoException().isThrownBy(() -> userRepository.save(user));
        var finalCount = userList.size();
        assertThat(initialCount).isEqualTo(finalCount);
    }

    @Test
    @DisplayName("delete should delete user when successfully")
    void delete_shouldDeleteUser_whenSuccessfully() {

        var removedUser = userList.stream().findFirst().get();
        userList.remove(removedUser);
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        assertThatNoException().isThrownBy(() -> userRepository.delete(removedUser.getId()));
        assertThat(userList).doesNotContain(removedUser);
    }

    @Test
    @DisplayName("delete should do nothing when id does not exists")
    void delete_shouldDoNothing_whenIdDoesNotExists() {
        var initialCount = userList.size();
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var id = 99L;
        assertThatNoException().isThrownBy(() -> userRepository.delete(id));
        var finalCount = userList.size();
        assertThat(initialCount).isEqualTo(finalCount);
    }

    @Test
    @DisplayName("put should return the object when succesffully")
    void put_shouldReturnObject_whenSuccessfully() {

        var userBeforeModification = userList.stream().findFirst().get();

        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userResultBeforeModification = userRepository.findById(userBeforeModification.getId()).get();
        assertThat(userResultBeforeModification).isNotNull().isEqualTo(userBeforeModification);

        var userAfterModification = userBeforeModification;

        userAfterModification.setFirstName("Kaio");
        userAfterModification.setLastName("Ribeiro");
        userAfterModification.setEmail("kaioribeiro@gmail.com");

        userList.add(userAfterModification);

        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userResultAfterModification = userRepository.update(userAfterModification);

        assertThat(userResultAfterModification).isNotNull().isNotEmpty().isEqualTo(userAfterModification);
        assertThat(userAfterModification.getId()).isEqualTo(userBeforeModification.getId());
    }

    @Test
    @DisplayName("put should do nothing when id does not exists")
    void put_shouldDoNothing_whenIdDoesNotExists() {

        var user = User.builder().id(99L).email("xaxa").firstName("xaxa").lastName("xaxa").build();
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        assertThatNoException().isThrownBy(() -> userRepository.update(user));
    }
}