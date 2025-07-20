package com.spring.user_service.repository;

import com.spring.exception.BadRequestException;
import com.spring.exception.NotFoundException;
import com.spring.user_service.data.UserData;
import com.spring.user_service.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final UserData userData;

    public Set<User> findAll() {
        return userData.getUsers();
    }

    public User findById(Long id) {
        return userData.getUsers().stream().filter(user -> user.getId().equals(id)).findFirst().orElseThrow(
                () -> new NotFoundException(String.format("User with id %s not found", id))
        );
    }

    public User findByEmail(String email) {
        return userData.getUsers().stream().filter(user -> user.getEmail().contains(email)).findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("User with email %s not found", email)));
    }

    public void save(User user) {
        boolean idExists = userData.getUsers().stream()
                .anyMatch(existingUser -> existingUser.getId().equals(user.getId()));

        boolean emailExists = userData.getUsers().stream()
                .anyMatch(existingUser -> existingUser.getEmail().equalsIgnoreCase(user.getEmail()));

        if (idExists) {
            throw new BadRequestException("User with the same ID already exists");
        }

        if (emailExists) {
            throw new BadRequestException("User with the same email already exists");
        }

        userData.getUsers().add(user);
    }

    public void delete(Long id) {
        var existingUser = findById(id);
        userData.getUsers().remove(existingUser);
    }

    public User update(User user) {
        delete(user.getId());
        save(user);
        return user;
    }

}
