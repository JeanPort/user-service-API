package com.jean.user_service.mapper;

import com.jean.user_service.domain.User;
import com.jean.user_service.domain.UserProfile;
import com.jean.user_service.response.UserProfileGetResponse;
import com.jean.user_service.response.UserProfileUserGetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProfileMapper {

    List<UserProfileGetResponse> toUserProfileGetResponse(List<UserProfile> users);

    List<UserProfileUserGetResponse> toUserProfileUserGetResponse(List<User> users);
}
