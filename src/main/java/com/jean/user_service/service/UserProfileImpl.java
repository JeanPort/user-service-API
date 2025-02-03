package com.jean.user_service.service;

import com.jean.user_service.domain.User;
import com.jean.user_service.domain.UserProfile;
import com.jean.user_service.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProfileImpl implements IUserProfileSerivece {

    private final UserProfileRepository repository;

    public UserProfileImpl(UserProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<UserProfile> findAll() {
        return repository.findAll();
    }

    @Override
    public List<User> findAllUserByProfileId(Long id) {
        return repository.findUserByProfile(id);
    }
}
