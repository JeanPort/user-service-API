package com.jean.user_service.repository;

import com.jean.user_service.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final UserData data;

    public UserRepository(UserData data) {
        this.data = data;
    }

    public List<User> findAll() {
        return data.getUsers();
    }

    public List<User> findByName(String name) {
        return data.getUsers().stream().filter(user -> user.getFirstName().equalsIgnoreCase(name)).toList();
    }

    public Optional<User> findById(Long id) {
        return data.getUsers().stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    public User create(User user) {
        data.getUsers().add(user);
        return user;
    }

    public void delete(User user) {
        data.getUsers().remove(user);
    }
    public void update(User user) {
        delete(user);
        data.getUsers().add(user);
    }
}
