package com.spring.user_service.service;

import com.spring.exception.EmailAlreadyExistException;
import com.spring.exception.NotFoundException;
import com.spring.user_service.model.User;
import com.spring.user_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

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
        BDDMockito.when(userRepository.findAll()).thenReturn(userSet.stream().toList());

        var result = userService.findAll();
        assertThat(result).containsAll(userSet);
    }

    @Test
    @DisplayName("findAllPaginated should return all paginated elements when successfully")
    void findAllPaginated_shouldReturnAllPaginatedElements_whenSuccessfully() {

        PageRequest pageRequest = PageRequest.of(0, userSet.size());
        PageImpl<User> pageUser = new PageImpl<>(new ArrayList<>(userSet), pageRequest, 1);

        BDDMockito.when(userRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(pageUser);

        var result = userService.findAllPaginated(pageRequest);
        assertThat(result).isNotNull().containsAll(userSet);
    }

    @Test
    @DisplayName("findById should return user when successfully")
    void findById_OrThrowNotFound_shouldReturnUser_whenSuccessfully() {
        var expectedResult = userSet.stream().filter(u -> u.getId().equals(1L)).findFirst().get();
        BDDMockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(expectedResult));
        var result = userService.findByIdOrThrowNotFound(expectedResult.getId());
        assertThat(result).isNotNull().isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("findById should throw an exception when id does not exists")
    void findById_shouldThrowException_whenIdOrThrowNotFoundDoesNotExists() {
        BDDMockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenThrow(ResponseStatusException.class);
        var id = 99L;
        assertThatThrownBy(() -> userService.findByIdOrThrowNotFound(id)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("findByEmail should return user when successfully")
    void findByEmail_OrThrowNotFound_shouldReturnUser_whenSuccessfully() {
        var expectedResult = userSet.stream().filter(u -> u.getId().equals(1L)).findFirst().get();
        BDDMockito.when(userRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(expectedResult));
        var result = userService.findByEmailOrThrowNotFound(expectedResult.getEmail());
        assertThat(result).isNotNull().isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("findByEmail should throw an exception when email does not exists")
    void findByEmail_shouldThrowException_whenEmailOrThrowNotFoundDoesNotExists() {
        BDDMockito.when(userRepository.findByEmail(ArgumentMatchers.anyString())).thenThrow(ResponseStatusException.class);
        var email = "xaxa@gmail.com";
        assertThatThrownBy(() -> userService.findByEmailOrThrowNotFound(email)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save should save a user when successfully")
    void save_shouldSaveUser_whenSuccessfully() {
        var expectedResult = User.builder().firstName("Joao").lastName("Silva").email("joao.silva@example.com").build();
        BDDMockito.when(userRepository.findByEmail(expectedResult.getEmail())).thenReturn(Optional.empty());
        BDDMockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(expectedResult);
        assertThatNoException().isThrownBy(() -> userService.save(expectedResult));
    }

    @Test
    @DisplayName("save should throw an exception when the email already exists")
    void save_shouldThrowAnException_whenEmailAlreadyExists() {
        var user = userSet.stream().findFirst().get();
        user.setFirstName("Nicolas");
        user.setLastName("Machado");

        BDDMockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.save(user)).isInstanceOf(EmailAlreadyExistException.class);
    }

    @Test
    @DisplayName("delete should delete user when successfully")
    void delete_shouldDeleteUser_whenSuccessfully() {
        var removedUser = userSet.stream().findFirst().get();

        BDDMockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(removedUser));
        BDDMockito.doNothing().when(userRepository).delete(ArgumentMatchers.any(User.class));

        assertThatNoException().isThrownBy(() -> userService.delete(removedUser.getId()));
    }

    @Test
    @DisplayName("delete should throw an exception when id does not exists")
    void delete_shouldThrowAnException_whenIdDoesNotExists() {
        BDDMockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
        var id = 99L;
        assertThatThrownBy(() -> userService.delete(id)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("put should return the object when succesffully")
    void put_shouldReturnObject_whenSuccessfully() {

        var user = userSet.stream().findFirst().get();

        user.setFirstName("Kaio");
        user.setLastName("Ribeiro");
        user.setEmail("kaioribeiro@gmail.com");

        BDDMockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        BDDMockito.when(userRepository.findByEmailAndIdNot(user.getEmail(), user.getId())).thenReturn(Optional.empty());
        BDDMockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

        var userResultAfterModification = userService.update(user);

        assertThat(userResultAfterModification).isNotNull().isEqualTo(user);
    }

    @Test
    @DisplayName("put should throw an exception when id does not exists")
    void put_shouldThrowAnException_whenIdDoesNotExists() {
        var user = User.builder().id(99L).email("xaxa").firstName("xaxa").lastName("xaxa").build();
        BDDMockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.update(user)).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("put should throw an exception when email belongs to another person")
    void put_shouldThrowAnException_whenEmailBelongsToAnotherPerson() {
        var userFirst = userSet.stream().findFirst().get();
        var user = User.builder().id(2L).email(userFirst.getEmail()).firstName("xaxa").lastName("xaxa").build();
        BDDMockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(user));
        BDDMockito.when(userRepository.findByEmailAndIdNot(user.getEmail(), user.getId())).thenReturn(Optional.of(userFirst));

        assertThatThrownBy(() -> userService.update(user)).isInstanceOf(EmailAlreadyExistException.class);
    }
}