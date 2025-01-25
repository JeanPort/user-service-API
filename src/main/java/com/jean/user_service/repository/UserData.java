package com.jean.user_service.repository;

import com.jean.user_service.domain.User;
import com.jean.user_service.util.UserUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserData {

    private List<User> users;

    public UserData() {
        var user1 = User.builder().id(1L).firstName("Jean").lastName("Port").email("jean@email.com").build();
        var user2 = User.builder().id(2L).firstName("Pierre").lastName("Cosmann").email("pierre@email.com").build();
        var user3 = User.builder().id(3L).firstName("Ana").lastName("Borges").email("ana@email.com").build();
        this.users = new ArrayList<>(List.of(user1,user2, user3));
    }

    public List<User> getUsers() {
        return users;
    }
}
