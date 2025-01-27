package com.jean.user_service.controller;

import com.jean.user_service.domain.User;
import com.jean.user_service.repository.UserData;
import com.jean.user_service.repository.UserRepository;
import com.jean.user_service.util.FileResourceLoader;
import com.jean.user_service.util.UserUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(controllers = UserControllerTest.class)
@ComponentScan(basePackages = "com.jean.user_service")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserData data;

    @MockitoSpyBean
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

        BDDMockito.when(data.getUsers()).thenReturn(users);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    void findAll_ShouldReturnUserByName_WhenNameExists() throws Exception {
        var response = fileResourceLoader.readResourceFile("user/get-user-response-Jeant-name-200.json");

        BDDMockito.when(data.getUsers()).thenReturn(this.users);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users").param("name", this.users.getFirst().getFirstName()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    void findByName_ShouldReturnEmptyList_WhenNameDoesNotExist() throws Exception {
        var response = fileResourceLoader.readResourceFile("user/get-user-response-x-name-200.json");
        var name = "x";
        BDDMockito.when(data.getUsers()).thenReturn(this.users);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    void findById_ShouldReturnUser_WhenIdExists() throws Exception {
        var resnponse = fileResourceLoader.readResourceFile("user/get-user-response-1-id-200.json");
        var id = this.users.getFirst().getId();

        BDDMockito.when(data.getUsers()).thenReturn(this.users);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(resnponse));
    }

    @Test
    void findById_ShouldReturnException_WhenIdDoesNotExists() throws Exception {

        var id = 10L;

        BDDMockito.when(data.getUsers()).thenReturn(this.users);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void create_ShouldPersistUser() throws Exception {
        var request = fileResourceLoader.readResourceFile("user/post-user-request-200.json");
        var response = fileResourceLoader.readResourceFile("user/post-user-response-201.json");
        var novo = new User(10L, "Novo", "xxxx", "novo@email.com");

        BDDMockito.when(repository.create(ArgumentMatchers.any())).thenReturn(novo);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/users").content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    void delete_ShouldRemoveUser_WhenIdExists() throws Exception {

        var id = 1L;
        BDDMockito.when(data.getUsers()).thenReturn(this.users);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/users/{id}",id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    void delete_ShouldException_WhenIdDoesNotExists() throws Exception {

        var id = 10L;
        BDDMockito.when(data.getUsers()).thenReturn(this.users);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/users/{id}",id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    void update_ShouldModifyUserDetails_WhenIdExists() throws Exception {
        var request = fileResourceLoader.readResourceFile("user/put-user-request-1-id-200.json");

        BDDMockito.when(data.getUsers()).thenReturn(this.users);
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/users").content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    void update_ShouldExcpition_WhenIdDoesNotExists() throws Exception {
        var request = fileResourceLoader.readResourceFile("user/put-user-request-10-id-200.json");

        BDDMockito.when(data.getUsers()).thenReturn(this.users);
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/users").content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

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
}
