package com.jean.user_service.controller;

import com.jean.user_service.domain.User;
import com.jean.user_service.mapper.UserMapper;
import com.jean.user_service.request.UserPostRequest;
import com.jean.user_service.request.UserPutRequest;
import com.jean.user_service.response.UserGetResponse;
import com.jean.user_service.response.UserPostResponse;
import com.jean.user_service.service.IUserSerivice;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/users")
public class UserController {

    private final IUserSerivice serivice;
    private final UserMapper mapper;

    public UserController(IUserSerivice serivice, UserMapper mapper) {
        this.serivice = serivice;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<UserGetResponse>> findAll(@RequestParam(required = false) String name){
        var res = serivice.findAll(name);
        var response = mapper.toListUserGetResponse(res);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserGetResponse> findById(@PathVariable Long id) {
        var res = serivice.findById(id);
        var response = mapper.toUserGetResponse(res);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        serivice.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping()
    public ResponseEntity<Void> update(@RequestBody @Valid UserPutRequest request){
        var res = mapper.toUser(request);
        serivice.update(res);
        return ResponseEntity.noContent().build();
    }

    @PostMapping()
    public ResponseEntity<UserPostResponse> create(@RequestBody @Valid UserPostRequest request){
        var user = mapper.toUser(request);
        user = serivice.create(user);
        var response = mapper.toUserPostResponse(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
