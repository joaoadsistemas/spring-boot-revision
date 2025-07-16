package com.spring.user_service.data;

import com.spring.user_service.model.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserData {

    private Set<User> user = new HashSet<>();

    public Set<User> getUsers() {
        var u1 = User.builder().id(1L).firstName("John").lastName("Doe").email("johndoe@gmail.com").build();
        var u2 = User.builder().id(2L).firstName("Robert").lastName("Kaleb").email("robertkaleb@gmail.com").build();
        var u3 = User.builder().id(3L).firstName("Miguel").lastName("Stuart").email("miguelstuart@gmail.com").build();
        var u4 = User.builder().id(4L).firstName("Sarah").lastName("Obama").email("sarahobama@gmail.com").build();
        var u5 = User.builder().id(5L).firstName("Olivia").lastName("Trump").email("oliviatrump@gmail.com").build();

        user.addAll(new HashSet<>(Set.of(u1, u2, u3, u4, u5)));
        return user;
    }
}
