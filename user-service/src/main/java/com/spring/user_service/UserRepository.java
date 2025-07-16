package com.spring.user_service;

import com.spring.user_service.data.UserData;
import com.spring.user_service.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final UserData userData;

    public Set<User> findAll() {
        return userData.getUsers();
    }

    public Optional<User> findById(Long id) {
        return userData.getUsers().stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    public Optional<User> findByEmail(String email) {
        return userData.getUsers().stream().filter(user -> user.getEmail().contains(email)).findFirst();
    }

    public void save(User user) {
        var existingUser = findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return;
        }
        existingUser = findById(user.getId());
        if (existingUser.isPresent()) {
            return;
        }
        userData.getUsers().add(user);
    }

    public void delete(Long id) {
        var existingUser = findById(id);
        existingUser.ifPresent(user -> userData.getUsers().remove(user));
    }

    public Optional<User> update(User user) {
        delete(user.getId());
        save(user);
        return findById(user.getId());
    }

}
