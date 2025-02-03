package com.jean.user_service.repository;

import com.jean.user_service.domain.User;
import com.jean.user_service.domain.UserProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    @EntityGraph(attributePaths = {"user", "profile"})
    List<UserProfile> findAll();

    @Query("SELECT up.user FROM UserProfile up WHERE up.profile.id  = ?1")
    List<User> findUserByProfile(Long id);
}
