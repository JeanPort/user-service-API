package com.jean.user_service.util;

import com.jean.user_service.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserUtil {

    public List<User> createListUserProd() {
        var user1 = new User(1L,"Jean","Port","jean@email.com");
        var user2 = new User(2L,"Pierre","Cosmann","pierre@email.com");
        var user3= new User(3L,"Ana","Aguiar","ana@email.com");

        return new ArrayList<>(List.of(user1,user2, user3));
    }


    public List<User> createListUserTest() {
        var user1 = new User(1L,"Jean T","Port","jean@email.com");
        var user2 = new User(2L,"Pierre T","Cosmann","pierre@email.com");
        var user3= new User(3L,"Ana T","Aguiar","ana@email.com");
        return new ArrayList<>(List.of(user1,user2, user3));
    }
}

