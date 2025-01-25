package com.jean.user_service.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class User {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

}
