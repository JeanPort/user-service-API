package com.jean.user_service.repository;

import com.jean.user_service.domain.User;
import com.jean.user_service.util.UserUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @InjectMocks
    public UserRepository repository;

    @InjectMocks
    public UserUtil util;

    @Mock
    public UserData data;

    private List<User> users;

    @BeforeEach
    void init() {

        users = util.createListUserTest();
    }

    @Test
    void findAll_ShouldReturnAllUser() {

        BDDMockito.when(data.getUsers()).thenReturn(users);

        var res = repository.findAll();
        Assertions.assertThat(res).isNotNull().hasSameElementsAs(users);
    }

    @Test
    void findByName_ShouldReturnUser_WhenNameExists() {
        var name = users.getFirst();

        BDDMockito.when(data.getUsers()).thenReturn(users);

        List<User> res = repository.findByName(name.getFirstName());
        Assertions.assertThat(res).isNotNull().contains(name);
    }

    @Test
    void findByName_ShouldReturnEmptyList_WhenNameDoesNotExist() {
        var name = "x";
        BDDMockito.when(data.getUsers()).thenReturn(users);
        var res = repository.findByName(name);

        Assertions.assertThat(res).isNotNull().isEmpty();
    }

    @Test
    void findById_ShouldReturnOptionUser_WhenIdExists() {
        var user = users.getFirst();
        BDDMockito.when(data.getUsers()).thenReturn(users);

        var res = repository.findById(user.getId());
        Assertions.assertThat(res).isPresent().contains(user);
    }

    @Test
    void findById_ShouldReturnOptionEmpty_WhenIdDoesNotExists() {
        var id = 10L;
        BDDMockito.when(data.getUsers()).thenReturn(users);

        var res = repository.findById(id);
        Assertions.assertThat(res).isEmpty().isNotNull();
    }

    @Test
    void create_ShouldPersistUser() {

        var novo = new User(10L, "Novo", "novo novo", "novo@email.com");
        BDDMockito.when(data.getUsers()).thenReturn(users);

        var res = repository.create(novo);
        Assertions.assertThat(res).isEqualTo(novo);
    }

    @Test
    void delete_ShouldRemoveUser_WhenIdExists() {

        var deleteUser = users.getFirst();
        BDDMockito.when(data.getUsers()).thenReturn(users);

        repository.delete(deleteUser);
        var res = repository.findById(deleteUser.getId());
        Assertions.assertThat(users).doesNotContain(deleteUser);
        Assertions.assertThat(res).isEmpty();

    }

    @Test
    void update_ShouldModifyUserDetails_WhenIdExists() {

        var update = new User(1L, "Atualizado", "ATT", "novo@email");
        BDDMockito.when(data.getUsers()).thenReturn(users);

        repository.update(update);
        System.out.println("MOSTRANDO USERS " + users);

        var res = repository.findById(update.getId());
        Assertions.assertThat(res).isPresent();
        Assertions.assertThat(res.get().getFirstName()).isEqualTo(update.getFirstName());

    }
}