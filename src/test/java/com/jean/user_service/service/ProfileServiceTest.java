package com.jean.user_service.service;


import com.jean.user_service.domain.Profile;
import com.jean.user_service.repository.ProfileRepository;
import com.jean.user_service.util.ProfileUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {

    @InjectMocks
    private ProfileServiceImpl service;

    @Mock
    private ProfileRepository repository;

    @InjectMocks
    private ProfileUtil util;

    List<Profile> profiles;

    @BeforeEach
    void init() {
        this.profiles = util.createListProfileTest();
    }

    @Test
    void findAll_shouldReturnListOfProfilesWhenNameIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(this.profiles);

        var res = service.findAll(null);
        Assertions.assertThat(res).isNotNull().hasSameElementsAs(this.profiles);
    }

    @Test
    void findAll_shouldReturnListOfProfilesByName() {
        var profile = this.profiles.getFirst();
        var retorno = this.profiles.stream().filter(profile1 -> profile1.getName().equalsIgnoreCase(profile.getName())).toList();

        BDDMockito.when(repository.findAllByName(profile.getName())).thenReturn(retorno);

        var res = service.findAll(profile.getName());
        Assertions.assertThat(res).isNotNull().contains(profile);
    }

    @Test
    void findAll_shouldReturnEmptyListWhenNameDoesNotExist() {
        var name = "x";

        BDDMockito.when(repository.findAllByName(name)).thenReturn(emptyList());

        var res = service.findAll(name);
        Assertions.assertThat(res).isNotNull().isEmpty();
    }

    @Test
    void findById_findById_shouldReturnProfileById() {
        var response = this.profiles.getFirst();

        BDDMockito.when(repository.findById(response.getId())).thenReturn(Optional.of(response));
        var res = service.findById(response.getId());

        Assertions.assertThat(res).isNotNull().isEqualTo(response);
    }



    @Test
    void findById_shouldThrowExceptionWhenProfileNotFoundById() {

        var id = 10L;
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.findById(id)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void create_shouldReturnCreateProfile() {
        var profileCreated = new Profile(4L, "novo", "novo novo");

        BDDMockito.when(repository.save(profileCreated)).thenReturn(profileCreated);

        var res = service.create(profileCreated);

        Assertions.assertThat(res).isNotNull().isEqualTo(profileCreated);
    }
}
