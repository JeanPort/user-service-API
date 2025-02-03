package com.jean.user_service.service;

import com.jean.user_service.domain.UserProfile;
import com.jean.user_service.repository.UserProfileRepository;
import com.jean.user_service.util.UserProfileUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceTest {

    @InjectMocks
    private UserProfileImpl service;

    @Mock
    private UserProfileRepository repository;

    @InjectMocks
    private UserProfileUtil util;

    private List<UserProfile> list;

    @BeforeEach
    void init() {
        this.list = util.getListUserProfile();
    }

    @Test
    void shouldReturnListOfUserProfiles_whenSuccessfull(){
        BDDMockito.when(repository.findAll()).thenReturn(this.list);

        var res = service.findAll();
        Assertions.assertThat(res).isNotNull().hasSameElementsAs(this.list);
    }

    @Test
    void shouldReturnListOfUsers_whenFindingUsersByProfileId(){
        var idProfile = 1L;
        var returno = this.list.stream().filter(userProfile -> userProfile.getProfile().getId().equals(idProfile)).map(UserProfile::getUser).toList();

        BDDMockito.when(repository.findUserByProfile(idProfile)).thenReturn(returno);

        var res = service.findAllUserByProfileId(idProfile);
        Assertions.assertThat(res).isNotNull().hasSameElementsAs(returno);
    }

    @Test
    void shouldReturnEmptyListOfUsers_whenProfileIdNotFound(){
        var idProfile = 3L;

        BDDMockito.when(repository.findUserByProfile(idProfile)).thenReturn(Collections.emptyList());
        var res = service.findAllUserByProfileId(idProfile);

        Assertions.assertThat(res).isNotNull().isEmpty();
    }
}
