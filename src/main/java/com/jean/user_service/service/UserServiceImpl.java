package com.jean.user_service.service;

import com.jean.user_service.domain.User;
import com.jean.user_service.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        return repository.findById(id).orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, "Não encontrado"));
    }

    @Override
    public User create(User user) {
        return repository.create(user);
    }

    @Override
    public void delete(Long id) {
        var res = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não encontrado"));
        repository.delete(res);
    }

    @Override
    public void update(User user) {
        repository.findById(user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não encontrado"));
        repository.update(user);
    }
}
