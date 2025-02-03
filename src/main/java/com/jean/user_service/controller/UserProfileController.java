package com.jean.user_service.controller;

import com.jean.user_service.domain.User;
import com.jean.user_service.domain.UserProfile;
import com.jean.user_service.mapper.UserProfileMapper;
import com.jean.user_service.response.UserGetResponse;
import com.jean.user_service.response.UserProfileGetResponse;
import com.jean.user_service.response.UserProfileUserGetResponse;
import com.jean.user_service.service.IUserProfileSerivece;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/user-profiles")
public class UserProfileController {


    private IUserProfileSerivece serivece;
    private final UserProfileMapper mapper;

    public UserProfileController(IUserProfileSerivece serivece, UserProfileMapper mapper) {
        this.serivece = serivece;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<UserProfileGetResponse>> findAll() {

        var res = serivece.findAll();
        var response = mapper.toUserProfileGetResponse(res);
        return ResponseEntity.ok(response);
    }

    @GetMapping("profiles/{id}/users")
    public ResponseEntity<List<UserProfileUserGetResponse>> findAllUserByProfileId(@PathVariable Long id) {
        var res = serivece.findAllUserByProfileId(id);
        var response = mapper.toUserProfileUserGetResponse(res);
        return ResponseEntity.ok(response);
    }
}
