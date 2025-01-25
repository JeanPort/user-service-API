package com.jean.user_service.service;

import com.jean.user_service.domain.User;

import java.util.List;

public interface IUserSerivice {

    List<User> findAll(String name);
    User findById(Long id);
    User create(User user);
    void delete(Long id);
    void update(User user);
}
