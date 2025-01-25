package com.jean.user_service.util;

import com.jean.user_service.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserUtil {

    public List<User> createListUserProd() {
        var user1 = User.builder().id(1L).firstName("Jean").lastName("Port").email("jean@email.com").build();
        var user2 = User.builder().id(2L).firstName("Pierre").lastName("Cosmann").email("pierre@email.com").build();
        var user3 = User.builder().id(3L).firstName("Ana").lastName("Borges").email("ana@email.com").build();
        return new ArrayList<>(List.of(user1,user2, user3));
    }


    public List<User> createListUserTest() {
        var user1 = User.builder().id(1L).firstName("Jean T").lastName("Port T").email("jean@email.com").build();
        var user2 = User.builder().id(2L).firstName("Pierre T").lastName("Cosmann T").email("pierre@email.com").build();
        var user3 = User.builder().id(3L).firstName("Ana T").lastName("Borges T").email("ana@email.com").build();
        return new ArrayList<>(List.of(user1,user2, user3));
    }
}

