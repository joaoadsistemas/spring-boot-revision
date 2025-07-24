package com.spring.user_service.repository;

import com.spring.user_service.model.UserProfile;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    @Query("SELECT up FROM UserProfile up join fetch up.user u join fetch up.profile p")
    List<UserProfile> retrieveAll();

    @Override
    //@EntityGraph(attributePaths = {"user", "profile"}) -> first way to use
    @EntityGraph(value = "UserProfile.fullDetails")
    List<UserProfile> findAll();
}
