package com.jean.user_service.service;

import com.jean.user_service.domain.User;
import com.jean.user_service.domain.UserProfile;

import java.util.List;

public interface IUserProfileSerivece {

    List<UserProfile> findAll();

    List<User> findAllUserByProfileId(Long id);
}
