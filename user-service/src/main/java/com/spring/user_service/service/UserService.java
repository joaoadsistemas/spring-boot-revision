package com.spring.user_service.service;

import com.spring.exception.EmailAlreadyExistException;
import com.spring.exception.NotFoundException;
import com.spring.user_service.mapper.UserMapper;
import com.spring.user_service.model.User;
import com.spring.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private static final UserMapper MAPPER = UserMapper.INSTANCE;

    public Set<User> findAll() {
        //return new HashSet<>(userHardCodeRepository.findAll());
        return new HashSet<>(userRepository.findAll());
    }

    public Page<User> findAllPaginated(Pageable pageable) {
        //return new HashSet<>(userHardCodeRepository.findAll());
        return userRepository.findAll(pageable);
    }

    public User findByIdOrThrowNotFound(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("There is no user with id: " + id));
    }

    public User findByEmailOrThrowNotFound(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("There is no user with email: " + email));
    }

    public void save(User user) {
        assertEmailDoesNotExist(user.getEmail());
        userRepository.save(user);
    }

    public void delete(Long id) {
        User user = this.findByIdOrThrowNotFound(id);
        userRepository.delete(user);
    }

    public User update(User user) {
        var savedUser = findByIdOrThrowNotFound(user.getId());
        assertEmailDoesNotExist(user.getEmail(), user.getId());
        user.setRoles(savedUser.getRoles());
        if (user.getPassword() == null) {
            user.setPassword(savedUser.getPassword());
        }
        return userRepository.save(user);
    }

    public void assertEmailDoesNotExist(String email) {
        userRepository
                .findByEmail(email)
                .ifPresent(e -> {
                    throwEmailExistException();
                });
    }

    public void assertEmailDoesNotExist(String email, Long id) {
        userRepository.findByEmailAndIdNot(email, id)
                .ifPresent(e -> {
                    throwEmailExistException();
                });
    }

    private static void throwEmailExistException() {
        throw new EmailAlreadyExistException("Email already exists");
    }


}
