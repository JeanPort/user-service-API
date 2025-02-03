package com.jean.user_service.controller;

import com.jean.user_service.domain.Profile;
import com.jean.user_service.mapper.ProfileMapperImpl;
import com.jean.user_service.repository.ProfileRepository;
import com.jean.user_service.service.ProfileServiceImpl;
import com.jean.user_service.util.FileResourceLoader;
import com.jean.user_service.util.ProfileUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@WebMvcTest(controllers = ProfileController.class)
//@ComponentScan(basePackages = "com.jean.user_service")
@Import({ProfileMapperImpl.class, ProfileServiceImpl.class, ProfileRepository.class, ProfileUtil.class, FileResourceLoader.class})
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ProfileRepository repository;

    @Autowired
    private ProfileUtil util;
    @Autowired
    private FileResourceLoader fileResourceLoader;

    private List<Profile> profiles;

    @BeforeEach
    void init() {
        this.profiles = util.createListProfileTest();
    }

    @Test
    void findAll_shouldReturnListOfProfilesWhenNameIsNull() throws Exception {

        var response = fileResourceLoader.readResourceFile("profile/get-user-response-null-name-200.json");
        BDDMockito.when(repository.findAll()).thenReturn(this.profiles);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/profiles"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    void findAll_shouldReturnListOfProfilesByName() throws Exception {

        var response = fileResourceLoader.readResourceFile("profile/get-user-response-cacador-name-200.json");
        var name = "caçador";
        var returno = this.profiles.stream().filter(profile -> profile.getName().equalsIgnoreCase(name)).toList();
        BDDMockito.when(repository.findAllByName(name)).thenReturn(returno);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/profiles").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    void findAll_shouldReturnEmptyListWhenNameDoesNotExist() throws Exception {
        var response = fileResourceLoader.readResourceFile("profile/get-user-response-x-name-200.json");
        var name = "x";

        BDDMockito.when(repository.findAllByName(name)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/profiles").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    void findById_findById_shouldReturnProfileById() throws Exception {
        var response = fileResourceLoader.readResourceFile("profile/get-user-response-1-id-200.json");
        var id = 2L;

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(this.profiles.get(1)));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/profiles/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }



    @Test
    void findById_shouldThrowExceptionWhenProfileNotFoundById() throws Exception {
        var response = fileResourceLoader.readResourceFile("profile/get-user-response-20-id-404.json");
        var id = 20L;

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/profiles/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    void create_shouldReturnCreateProfile() throws Exception {
        var requst = fileResourceLoader.readResourceFile("profile/post-user-request-novo-nome-200.json");
        var response = fileResourceLoader.readResourceFile("profile/post-user-response-novo-nome-201.json");
        var novoProfile = new Profile(10L, "Novo", "Novo no elenco");


        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(novoProfile);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/profiles").content(requst).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    void create_shouldThrowExceptionWhenFieldIsNull() throws Exception {
        var request = fileResourceLoader.readResourceFile("profile/post-user-request-null-nome-400.json");
        var id = 20L;

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());
        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/v1/profiles").content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();
        var fildErrorName = "The 'name' field cannot be empty or null";
        var fildErrorDescription = "The 'description' field cannot be empty or null";

        Assertions.assertThat(resolvedException).isNotNull();
        Assertions.assertThat(resolvedException.getMessage()).contains(fildErrorName, fildErrorDescription);
    }

    @Test
    void create_shouldThrowExceptionWhenFieldIsEmpty() throws Exception {
        var request = fileResourceLoader.readResourceFile("profile/post-user-request-empty-nome-400.json");
        var id = 20L;

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());
        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/v1/profiles").content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();
        var fildErrorName = "The 'name' field cannot be empty or null";
        var fildErrorDescription = "The 'description' field cannot be empty or null";

        Assertions.assertThat(resolvedException).isNotNull();
        Assertions.assertThat(resolvedException.getMessage()).contains(fildErrorName, fildErrorDescription);
    }
}
