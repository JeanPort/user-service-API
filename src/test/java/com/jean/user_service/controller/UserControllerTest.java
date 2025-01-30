package com.jean.user_service.controller;

import com.jean.user_service.domain.User;
import com.jean.user_service.mapper.ProfileMapper;
import com.jean.user_service.mapper.UserMapper;
import com.jean.user_service.mapper.UserMapperImpl;
import com.jean.user_service.repository.ProfileRepository;
import com.jean.user_service.repository.UserRepository;
import com.jean.user_service.service.ProfileServiceImpl;
import com.jean.user_service.service.UserServiceImpl;
import com.jean.user_service.util.FileResourceLoader;
import com.jean.user_service.util.ProfileUtil;
import com.jean.user_service.util.UserUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@WebMvcTest(controllers = UserController.class)
//@ComponentScan(basePackages = "com.jean.user_service")
@Import({UserMapperImpl.class, UserServiceImpl.class, UserRepository.class, UserUtil.class, FileResourceLoader.class, UserController.class})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository repository;

    @Autowired
    private UserUtil util;
    @Autowired
    private FileResourceLoader fileResourceLoader;
    private List<User> users;

    @BeforeEach
    void init() {
        users = util.createListUserTest();
    }

    @Test
    void findAll_ShouldReturnAllUser() throws Exception {

        var response = fileResourceLoader.readResourceFile("user/get-user-response-null-name-200.json");

        BDDMockito.when(repository.findAll()).thenReturn(users);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    void findAll_ShouldReturnUserByName_WhenNameExists() throws Exception {
        var response = fileResourceLoader.readResourceFile("user/get-user-response-Jeant-name-200.json");
        var firstName = users.getFirst().getFirstName();
        var list = users.stream().filter(user -> user.getFirstName().equalsIgnoreCase(firstName)).toList();

        BDDMockito.when(repository.findAllByFirstNameIgnoreCase(firstName)).thenReturn(list);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users").param("name", this.users.getFirst().getFirstName()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    void findByName_ShouldReturnEmptyList_WhenNameDoesNotExist() throws Exception {
        var response = fileResourceLoader.readResourceFile("user/get-user-response-x-name-200.json");
        var name = "x";
        BDDMockito.when(repository.findAllByFirstNameIgnoreCase(name)).thenReturn(emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    void findById_ShouldReturnUser_WhenIdExists() throws Exception {
        var resnponse = fileResourceLoader.readResourceFile("user/get-user-response-1-id-200.json");
        var usuario = this.users.getFirst();

        BDDMockito.when(repository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/{id}", usuario.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(resnponse));
    }

    @Test
    void findById_ShouldReturnException_WhenIdDoesNotExists() throws Exception {

        var id = 10L;
        var response = fileResourceLoader.readResourceFile("user/excption-user-response.json");

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    void create_ShouldPersistUser() throws Exception {
        var request = fileResourceLoader.readResourceFile("user/post-user-request-200.json");
        var response = fileResourceLoader.readResourceFile("user/post-user-response-201.json");
        var retorno = new User(10L, "Novo", "xxxx", "novo@email.com");

        BDDMockito.when(repository.findByEmailIgnoreCase(retorno.getEmail())).thenReturn(Optional.empty());
        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(retorno);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/users").content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    void create_ShouldExcpition_WhenEmailExists() throws Exception {

        var request = fileResourceLoader.readResourceFile("user/put-user-request-1-id-email-200.json");
        var response = fileResourceLoader.readResourceFile("/user/excption-user-response-email.json");
        var user = new User(null, "jean", "pport", "pierre@email.com");

        BDDMockito.when(repository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/users").content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    void delete_ShouldRemoveUser_WhenIdExists() throws Exception {

        var user = users.getFirst();
        BDDMockito.when(repository.findById(user.getId())).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/users/{id}",user.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    void delete_ShouldException_WhenIdDoesNotExists() throws Exception {

        var id = 10L;
        var response = fileResourceLoader.readResourceFile("user/excption-user-response.json");
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/users/{id}",id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    void update_ShouldModifyUserDetails_WhenIdExists() throws Exception {
        var request = fileResourceLoader.readResourceFile("user/put-user-request-1-id-200.json");
        var user = users.getFirst();
        var userEmail = users.getLast();

        BDDMockito.when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        BDDMockito.when(repository.findByEmailIgnoreCaseAndIdNot(userEmail.getEmail(), user.getId())).thenReturn(Optional.of(user));
        BDDMockito.when(repository.save(user)).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/users").content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    void update_ShouldExcpition_WhenIdDoesNotExists() throws Exception {
        var request = fileResourceLoader.readResourceFile("user/put-user-request-10-id-200.json");
        var response = fileResourceLoader.readResourceFile("user/excption-user-response.json");
        var id   = 10L;
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/users").content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    void update_ShouldExcpition_WhenEmailExists() throws Exception {

        var request = fileResourceLoader.readResourceFile("user/put-user-request-1-id-email-200.json");
        var response = fileResourceLoader.readResourceFile("/user/excption-user-response-email.json");
        var user = new User(1L, "jean", "pport", "pierre@email.com");
        BDDMockito.when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        BDDMockito.when(repository.findByEmailIgnoreCaseAndIdNot(user.getEmail(), user.getId())).thenReturn(Optional.of(user));
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/users").content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    void create_ShouldBadRequstWhenFieldIsEmpty() throws Exception {
        var request = fileResourceLoader.readResourceFile("user/post-user-request-empty-400.json");


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/v1/users").content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        Exception resolvedException = mvcResult.getResolvedException();

        var firstNameError = "The field 'firstName' connot be null";
        var lastNameError = "The field 'lastName' connot be null";
        var emailError = "The field 'email' is not valid";

        Assertions.assertThat(resolvedException).isNotNull();
        Assertions.assertThat(resolvedException.getMessage()).contains(firstNameError, lastNameError, emailError);
    }

    @Test
    void create_ShouldBadRequstWhenFieldIsBlanck() throws Exception {
        var request = fileResourceLoader.readResourceFile("user/post-user-request-blanck-400.json");


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/v1/users").content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        Exception resolvedException = mvcResult.getResolvedException();

        var firstNameError = "The field 'firstName' connot be null";
        var lastNameError = "The field 'lastName' connot be null";
        var emailError = "The field 'email' is not valid";

        Assertions.assertThat(resolvedException).isNotNull();
        Assertions.assertThat(resolvedException.getMessage()).contains(firstNameError, lastNameError, emailError);
    }

    @Test
    void update_ShouldBadRequstWhenFieldIsEmpty() throws Exception {
        var request = fileResourceLoader.readResourceFile("user/put-user-request-empty-400.json");


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/v1/users").content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        Exception resolvedException = mvcResult.getResolvedException();

        var idError = "The field 'id' cannot null";
        var firstNameError = "The field 'firstName' connot be null";
        var lastNameError = "The field 'lastName' connot be null";
        var emailError = "The field 'email' is not valid";

        Assertions.assertThat(resolvedException).isNotNull();
        Assertions.assertThat(resolvedException.getMessage()).contains(idError, firstNameError, lastNameError, emailError);
    }

    @Test
    void update_ShouldBadRequstWhenFieldIsBlack() throws Exception {
        var request = fileResourceLoader.readResourceFile("user/put-user-request-blanck-400.json");


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/v1/users").content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        Exception resolvedException = mvcResult.getResolvedException();

        var idError = "The field 'id' cannot null";
        var firstNameError = "The field 'firstName' connot be null";
        var lastNameError = "The field 'lastName' connot be null";
        var emailError = "The field 'email' is not valid";

        Assertions.assertThat(resolvedException).isNotNull();
        Assertions.assertThat(resolvedException.getMessage()).contains(idError, firstNameError, lastNameError, emailError);
    }
}