package com.spring.user_service.service;

import com.spring.user_service.mapper.UserMapper;
import com.spring.user_service.model.User;
import com.spring.user_service.repository.UserHardCodeRepository;
import com.spring.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserHardCodeRepository userHardCodeRepository;
    private final UserRepository userRepository;
    private static final UserMapper MAPPER = UserMapper.INSTANCE;

    public Set<User> findAll() {
        //return new HashSet<>(userHardCodeRepository.findAll());
        return new HashSet<>(userRepository.findAll());
    }

    public User findById(Long id) {
        return userHardCodeRepository.findById(id);
    }

    public User findByEmail(String email) {
        return userHardCodeRepository.findByEmail(email);
    }

    public void save(User user) {
        userHardCodeRepository.save(user);
    }

    public void delete(Long id) {
        userHardCodeRepository.delete(id);
    }

    public User update(User user) {
        return userHardCodeRepository.update(user);
    }

}
