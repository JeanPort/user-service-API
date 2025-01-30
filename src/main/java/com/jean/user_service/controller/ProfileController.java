package com.jean.user_service.controller;

import com.jean.user_service.mapper.ProfileMapper;
import com.jean.user_service.request.ProfilePostRequest;
import com.jean.user_service.response.ProfileGetResponse;
import com.jean.user_service.response.ProfilePostResponse;
import com.jean.user_service.service.IProfileSerivce;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/profiles")
public class ProfileController {

    private final IProfileSerivce serivce;
    private final ProfileMapper mapper;

    public ProfileController(IProfileSerivce serivce, ProfileMapper mapper) {
        this.serivce = serivce;
        this.mapper = mapper;
    }

    @GetMapping()
    public ResponseEntity<List<ProfileGetResponse>> findAll(@RequestParam(required = false) String name) {
        var res = serivce.findAll(name);
        var response = mapper.toListProfileGetResponse(res);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProfileGetResponse> findById(@PathVariable Long id) {
        var res = serivce.findById(id);
        var response = mapper.toProfileGetResponse(res);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProfilePostResponse> create(@RequestBody @Valid ProfilePostRequest request) {
        var profile = mapper.toProfile(request);
        var res = serivce.create(profile);
        var response = mapper.toProfilePostResponse(res);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
