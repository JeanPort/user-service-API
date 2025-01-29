package com.jean.user_service.service;

import com.jean.user_service.domain.Profile;
import com.jean.user_service.exception.NotFoundException;
import com.jean.user_service.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileServiceImpl implements IProfileSerivce{

    private ProfileRepository repository;

    public ProfileServiceImpl(ProfileRepository repository) {
        this.repository = repository;
    }


    @Override
    public List<Profile> findAll(String name) {
        return name == null? repository.findAll() : repository.findAllByName(name);
    }

    @Override
    public Profile findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Não encontrado"));
    }

    @Override
    public Profile create(Profile profile) {
        return repository.save(profile);
    }
}
