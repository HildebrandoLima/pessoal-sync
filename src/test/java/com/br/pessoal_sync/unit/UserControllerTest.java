package com.br.pessoal_sync.unit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.br.pessoal_sync.controller.UserController;
import com.br.pessoal_sync.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("should return badrequest when name is empty")
    void shouldReturnBadRequestWhenNameIsEmpty() throws Exception {
        String userDtoJson = "{ \"name\": \"\", \"email\": \"dell.dev@email.com\", \"cpf\": \"064.380.240-15\"}";

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data[0].name")
                .value("Nome é obrigatório"));
    }

    @Test
    @DisplayName("should return badrequest when name is size")
    void shouldReturnBadRequestWhenNameSize() throws Exception {
        String userDtoJson = "{ \"name\": \"Bartholomew J. Calloway-Smithington-Jones III, the Venerable and Esteemed Grandmaster of the Order of the Most Noble and Ancient Guild of Artisans and Craftspersons\", \"email\": \"dell.dev@email.com\", \"cpf\": \"064.380.240-15\"}";

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data[0].name")
                .value("O nome não deve exceder 100 caracteres"));
    }

    @Test
    @DisplayName("should return badrequest when email is empty")
    void shouldReturnBadRequestWhenEmailIsEmpty() throws Exception {
        String userDtoJson = "{ \"name\": \"Dell\", \"email\": \"\", \"cpf\": \"064.380.240-15\"}";

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data[0].email")
                .value("E-mail é obrigatório"));
    }

    @Test
    @DisplayName("should return badrequest when email is invalid")
    void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        String userDtoJson = "{ \"name\": \"Dell\", \"email\": \"dell.devemail\", \"cpf\": \"064.380.240-15\"}";

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data[0].email")
                .value("E-mail inválido"));
    }

    /*
    @Test
    @DisplayName("should return badrequest when cpf is empty")
    void shouldReturnBadRequestWhenCpfIsEmpty() throws Exception {
        String userDtoJson = "{ \"name\": \"Dell\", \"email\": \"dell.dev@email.com\", \"cpf\": \"\"}";

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data[0].cpf")
                .value("CPF é obrigatório"));
    }
    */

    @Test
    @DisplayName("should return badrequest when cpf is invalid")
    void shouldReturnBadRequestWhenCpfIsInvalid() throws Exception {
        String userDtoJson = "{ \"name\": \"Dell\", \"email\": \"dell.dev@email.com\", \"cpf\": \"06438024015\"}";

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data[0].cpf")
                .value("CPF inválido"));
    }

}
