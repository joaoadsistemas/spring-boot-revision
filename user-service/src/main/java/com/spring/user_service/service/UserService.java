package com.spring.user_service.service;

import com.spring.exception.BadRequestException;
import com.spring.exception.NotFoundException;
import com.spring.user_service.mapper.UserMapper;
import com.spring.user_service.model.User;
import com.spring.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("There is no user with id: " + id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("There is no user with email: " + email));
    }

    public void save(User user) {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new BadRequestException("Email already exists");
        }
    }

    public void delete(Long id) {
        User user = this.findById(id);
        userRepository.delete(user);
    }

    public User update(User user) {
        this.findById(user.getId());
        return userRepository.save(user);
    }

}
