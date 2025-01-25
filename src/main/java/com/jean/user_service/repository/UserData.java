package com.jean.user_service.repository;

import com.jean.user_service.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserData {

    User user1 = new User(1L,"Jean","Port","jean@email.com");
    User user2 = new User(2L,"Ana","Borges","ana@email.com");
    User user3 = new User(3L,"Pierre","Cosmann","pierre@email.com");

    private final List<User> users =new ArrayList<>(List.of(user1,user2, user3));;

    public List<User> getUsers() {
        return users;
    }
}
