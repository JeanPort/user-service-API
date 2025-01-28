package com.jean.user_service.service;

import com.jean.user_service.domain.User;
import com.jean.user_service.exception.NotFoundException;
import com.jean.user_service.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserSerivice {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByName(name);
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id).orElseThrow(() ->  new NotFoundException("Usuario não encontrado"));
    }

    @Override
    public User create(User user) {
        return repository.create(user);
    }

    @Override
    public void delete(Long id) {
        var res = repository.findById(id).orElseThrow(() -> new NotFoundException("Usuario não encontrado"));
        repository.delete(res);
    }

    @Override
    public void update(User user) {
        repository.findById(user.getId()).orElseThrow(() -> new NotFoundException("Usuario não encontrado"));
        repository.update(user);
    }
}
