package com.br.pessoal_sync.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.br.pessoal_sync.controller.UserController;
import com.br.pessoal_sync.domain.dto.UserDto;
import com.br.pessoal_sync.domain.model.User;
import com.br.pessoal_sync.service.user.UserImplService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserImplService userService;

    @Test
    @DisplayName("should return created success")
    void shouldReturnCreatedSuccess() throws Exception {
        String userDtoJson = "{ \"name\": \"Dell\", \"email\": \"dell.dev@email.com\", \"cpf\": \"064.380.240-15\"}";

        Long userId = 1L;
        when(userService.createUser(any())).thenReturn(userId);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDtoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Registro criado."))
                .andExpect(jsonPath("$.data[0]").value(userId.intValue()))
                .andExpect(jsonPath("$.details").value(""))
                .andExpect(jsonPath("$.status").value(201));
    }

    @Test
    @DisplayName("should return badrequest when name is empty")
    void shouldReturnBadRequestWhenNameIsEmpty() throws Exception {
        String userDtoJson = "{ \"name\": \"\", \"email\": \"dell.dev@email.com\", \"cpf\": \"064.380.240-15\"}";

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data[0].name")
                .value("Nome é obrigatório."));
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
                .value("O nome não deve exceder 100 caracteres."));
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
                .value("E-mail é obrigatório."));
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
                .value("E-mail inválido."));
    }

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

    @Test
    @DisplayName("should return badrequest when cpf is invalid")
    void shouldReturnBadRequestWhenCpfIsInvalid() throws Exception {
        String userDtoJson = "{ \"name\": \"Dell\", \"email\": \"dell.dev@email.com\", \"cpf\": \"438024015\"}";

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data[0].cpf")
                .value("CPF inválido."));
    }

    @Test
    @DisplayName("should return a get user by id")
    void shouldReturnGetUserById() throws Exception {
        Instant createdAt = Instant.now();
        Instant updatedAt = Instant.now();

        Long userId = 1L;
        User user = new User(userId, "Fátima Catarina Mariah Brito", "fatima-brito86@moncoes.com.br", "175.471.282-70", true, createdAt, updatedAt);

        when(userService.validateGetUser(userId)).thenReturn(Optional.of(user));
        when(userService.getUser(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/{id}", userId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Registro listado."))
            .andExpect(jsonPath("$.data[0].id").value(userId))
            .andExpect(jsonPath("$.data[0].name").value("Fátima Catarina Mariah Brito"))
            .andExpect(jsonPath("$.data[0].email").value("fatima-brito86@moncoes.com.br"))
            .andExpect(jsonPath("$.data[0].cpf").value("175.471.282-70"))
            .andExpect(jsonPath("$.data[0].isActive").value(true))
            .andExpect(jsonPath("$.data[0].updatedAt").value(updatedAt.toString()))
            .andExpect(jsonPath("$.data[0].createdAt").value(createdAt.toString()))
            .andExpect(jsonPath("$.details").value(""))
            .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    @DisplayName("should return a get of users success")
    void shouldReturnGetOfUsersSuccess() throws Exception {
        Instant createdAt0 = Instant.now();
        Instant updatedAt0 = Instant.now();
        Instant createdAt1 = Instant.now();
        Instant updatedAt1 = Instant.now();

        List<User> users = Arrays.asList(
            new User(1L, "Fátima Catarina Mariah Brito", "fatima-brito86@moncoes.com.br", "175.471.282-70", false, createdAt0, updatedAt0),
            new User(2L, "Sophie Silvana da Mata", "sophie.silvana.damata@lojascentrodamoda.com.br", "767.439.425-53", true, createdAt1, updatedAt1)
        );
    
        when(userService.getUsers()).thenReturn(users);
    
        mockMvc.perform(get("/api/users")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Registros listados."))

            .andExpect(jsonPath("$.data[0].id").value(1L))
            .andExpect(jsonPath("$.data[0].name").value("Fátima Catarina Mariah Brito"))
            .andExpect(jsonPath("$.data[0].email").value("fatima-brito86@moncoes.com.br"))
            .andExpect(jsonPath("$.data[0].cpf").value("175.471.282-70"))
            .andExpect(jsonPath("$.data[0].isActive").value(false))
            .andExpect(jsonPath("$.data[0].updatedAt").value(updatedAt0.toString()))
            .andExpect(jsonPath("$.data[0].createdAt").value(createdAt0.toString()))

            .andExpect(jsonPath("$.data[1].id").value(2L))
            .andExpect(jsonPath("$.data[1].name").value("Sophie Silvana da Mata"))
            .andExpect(jsonPath("$.data[1].email").value("sophie.silvana.damata@lojascentrodamoda.com.br"))
            .andExpect(jsonPath("$.data[1].cpf").value("767.439.425-53"))
            .andExpect(jsonPath("$.data[1].isActive").value(true))
            .andExpect(jsonPath("$.data[1].updatedAt").value(updatedAt1.toString()))
            .andExpect(jsonPath("$.data[1].createdAt").value(createdAt1.toString()))

            .andExpect(jsonPath("$.details").value(""))
            .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    @DisplayName("should return updated success")
    void shouldReturnUpdatedSuccess() throws Exception {
        Long userId = 1L;
        String userDtoJson = "{ \"name\": \"Dell\", \"email\": \"dell.dev@email.com\", \"cpf\": \"984.199.960-92\", \"active\": true }";

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Dell");
        existingUser.setEmail("dell.dev@email.com");
        existingUser.setCpf("984.199.960-92");
        existingUser.setIsActive(true);
        existingUser.setUpdatedAt(Instant.now());

        User updatedUser = new User();
        updatedUser.setId(existingUser.getId());
        updatedUser.setName(existingUser.getName());
        updatedUser.setEmail(existingUser.getEmail());
        updatedUser.setCpf(existingUser.getCpf());
        updatedUser.setIsActive(existingUser.getIsActive());
        updatedUser.setUpdatedAt(existingUser.getUpdatedAt());

        when(userService.validateGetUser(userId)).thenReturn(Optional.of(existingUser));
        when(userService.updateUser(any(User.class), any(UserDto.class))).thenReturn(updatedUser.getId());

        mockMvc.perform(put("/api/users/{id}", userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(userDtoJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Registro alterado."))
            .andExpect(jsonPath("$.data").isEmpty())
            .andExpect(jsonPath("$.details").value(""))
            .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    @DisplayName("should return error when updated user is not found")
    void shouldReturnErrorWhenUpdatedUserIsNotFound() throws Exception {
        Long userId = 1L;
        String userDtoJson = "{ \"name\": \"Dell\", \"email\": \"dell.dev@email.com\", \"cpf\": \"984.199.960-92\", \"active\": true }";

        when(userService.validateGetUser(userId)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/users/{id}", userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(userDtoJson))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Registro não encontrado."))
            .andExpect(jsonPath("$.data").isEmpty())
            .andExpect(jsonPath("$.details").value("Usuário não encontrado."))
            .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("should return deleted success")
    void shouldReturnDeletedSuccess() throws Exception {
        Long userId = 1L;

        doNothing().when(userService).deleteUser(userId);
        when(userService.validateGetUser(userId)).thenReturn(Optional.of(new User()));

         mockMvc.perform(delete("/api/users/{id}", userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Registro deletado."))
            .andExpect(jsonPath("$.data").isEmpty())
            .andExpect(jsonPath("$.details").value(""))
            .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    @DisplayName("should return error when deleted user is not found")
    void shouldReturnErrorWhenDeletedUserIsNotFound() throws Exception {
        Long userId = 1L;

        when(userService.validateGetUser(userId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/users/{id}", userId))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Registro não encontrado."))
            .andExpect(jsonPath("$.data").isEmpty())
            .andExpect(jsonPath("$.details").value("Usuário não encontrado."))
            .andExpect(jsonPath("$.status").value(404));
    }

}
