package com.jean.user_service.util;

import com.jean.user_service.domain.Profile;
import com.jean.user_service.domain.User;
import com.jean.user_service.domain.UserProfile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserProfileUtil {

    public List<UserProfile> getListUserProfile() {

        var profile1 = new Profile(1L, "Administrador", "Ele administra");
        var profile2 = new Profile(2L, "Gerente", "Ele gerencia");

        var up1 = new UserProfile(1L , new User(1L, "Jean", "Port", "jean@email.com"), profile1);
        var up2 = new UserProfile(2L , new User(1L, "Ana", "Borges", "ana@email.com"), profile2);
        var up3 = new UserProfile(3L , new User(1L, "Pierre", "Cosmann", "pierre@email.com"), profile1);

        return new ArrayList<>(List.of(up1, up2, up3));
    }
}
