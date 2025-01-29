package com.jean.user_service.service;

import com.jean.user_service.domain.Profile;

import java.util.List;

public interface IProfileSerivce {

    List<Profile> findAll(String name);
    Profile findById(Long id);
    Profile create(Profile profile);
}
