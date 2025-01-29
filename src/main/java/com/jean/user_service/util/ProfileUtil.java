package com.jean.user_service.util;

import com.jean.user_service.domain.Profile;
import com.jean.user_service.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProfileUtil {

    public List<Profile> createListProfileTest() {
        var prof1 = new Profile(1L,"Atirador","Gosta de atira");
        var prof2 = new Profile(2L,"Caçador","Gosta de caça");
        var prof3= new Profile(3L,"Nadador","Gosta de nada");
        return new ArrayList<>(List.of(prof1,prof2, prof3));
    }
}

