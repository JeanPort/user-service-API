package com.jean.user_service.mapper;

import com.jean.user_service.domain.User;
import com.jean.user_service.request.UserPostRequest;
import com.jean.user_service.request.UserPutRequest;
import com.jean.user_service.response.UserGetResponse;
import com.jean.user_service.response.UserPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(4,10))")
    User toUser(UserPostRequest request);


    User toUser(UserPutRequest request);

    UserGetResponse toUserGetResponse(User user);
    List<UserGetResponse> toListUserGetResponse(List<User> users);

    UserPostResponse toUserPostResponse(User user);
}
