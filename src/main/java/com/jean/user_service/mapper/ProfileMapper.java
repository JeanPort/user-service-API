package com.jean.user_service.mapper;

import com.jean.user_service.domain.Profile;
import com.jean.user_service.request.ProfilePostRequest;
import com.jean.user_service.response.ProfileGetResponse;
import com.jean.user_service.response.ProfilePostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProfileMapper {

    Profile toProfile(ProfilePostRequest request);
    ProfilePostResponse toProfilePostResponse(Profile profile);
    ProfileGetResponse toProfileGetResponse(Profile profile);
    List<ProfileGetResponse> toListProfileGetResponse(List<Profile> profiles);
}
