package com.jean.user_service.service;

import com.jean.user_service.domain.User;
import com.jean.user_service.repository.UserRepository;
import com.jean.user_service.util.UserUtil;
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
public class UserServiceTest {

    @InjectMocks
    private IUserSerivice serivice;
    @Mock
    private UserRepository repository;
    private UserUtil util;
    private List<User> users;

    @BeforeEach
    void init() {
        this.users = util.createListUserTest();
    }

    @Test
    void findAll_ShouldReturnAllUser() {

        BDDMockito.when(repository.findAll()).thenReturn(this.users);

        var res = serivice.findAll(null);
        Assertions.assertThat(res).isNotNull().hasSameElementsAs(this.users);
    }

    @Test
    void findByName_ShouldReturnUser_WhenNameExists() {
        var name = this.users.getFirst();
        var list = this.users.stream().filter(user -> user.getFirstName().equalsIgnoreCase(name.getFirstName())).toList();
        BDDMockito.when(repository.findByName(name.getFirstName())).thenReturn(list);

        var res = serivice.findAll(name.getFirstName());
        Assertions.assertThat(res).isNotNull().hasSameElementsAs(list);
    }

    @Test
    void findByName_ShouldReturnEmptyList_WhenNameDoesNotExist() {
        var name = "x";

        BDDMockito.when(repository.findByName(name)).thenReturn(emptyList());

        var res = serivice.findAll(name);
        Assertions.assertThat(res).isNotNull().isEmpty();
    }

    @Test
    void findById_ShouldReturnUser_WhenIdExists() {
        var id = this.users.getFirst();
        BDDMockito.when(repository.findById(id.getId())).thenReturn(Optional.of(id));

        var res = serivice.findById(id.getId());
        Assertions.assertThat(res).isEqualTo(id).isNotNull();
    }

    @Test
    void findById_ShouldReturnException_WhenIdDoesNotExists() {
        var id = 10L;
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> serivice.findById(id)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void create_ShouldPersistUser() {
        var user = new User(10L, "novo", "novo novo", "novo@email");

        BDDMockito.when(repository.create(user)).thenReturn(user);

        var res = serivice.create(user);
        Assertions.assertThat(res).isEqualTo(user);

    }

    @Test
    void delete_ShouldRemoveUser_WhenIdExists() {

        var delete = this.users.getFirst();

        BDDMockito.when(repository.findById(delete.getId())).thenReturn(Optional.of(delete));
        BDDMockito.doNothing().when(repository).delete(delete);

        Assertions.assertThatNoException().isThrownBy(() -> serivice.delete(delete.getId()));

    }

    @Test
    void delete_ShouldException_WhenIdDoesNotExists() {

        var id = 100L;
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> serivice.delete(id)).isInstanceOf(ResponseStatusException.class);

    }

    @Test
    void update_ShouldModifyUserDetails_WhenIdExists() {
        var update = this.users.getFirst();
        var att = new User(update.getId(), "novo nome", "ultimo nome", "novo@email");

        BDDMockito.when(repository.findById(att.getId())).thenReturn(Optional.of(update));
        BDDMockito.doNothing().when(repository).update(att);

        Assertions.assertThatNoException().isThrownBy(() -> serivice.update(att));

    }

    @Test
    void update_ShouldExcpition_WhenIdDoesNotExists() {
        var id = new User(100L, "nana", "jaosdfa", "fadls@fdslafjs");

        BDDMockito.when(repository.findById(id.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> serivice.update(id)).isInstanceOf(ResponseStatusException.class);

    }
}