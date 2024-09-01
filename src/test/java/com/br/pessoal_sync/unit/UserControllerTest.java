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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.br.pessoal_sync.controller.UserController;
import com.br.pessoal_sync.domain.dto.AddressDto;
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
                .andExpect(jsonPath("$.data").value(userId.intValue()))
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
                .andExpect(jsonPath("$.message").value("Informe os dados corretamente."))
                .andExpect(jsonPath("$.data[0].name").value("Nome é obrigatório."))
                .andExpect(jsonPath("$.details").exists())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("should return badrequest when name is size")
    void shouldReturnBadRequestWhenNameSize() throws Exception {
        String userDtoJson = "{ \"name\": \"Bartholomew J. Calloway-Smithington-Jones III, the Venerable and Esteemed Grandmaster of the Order of the Most Noble and Ancient Guild of Artisans and Craftspersons\", \"email\": \"dell.dev@email.com\", \"cpf\": \"064.380.240-15\"}";

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Informe os dados corretamente."))
                .andExpect(jsonPath("$.data[0].name").value("O nome não deve exceder 100 caracteres."))
                .andExpect(jsonPath("$.details").exists())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("should return badrequest when email is empty")
    void shouldReturnBadRequestWhenEmailIsEmpty() throws Exception {
        String userDtoJson = "{ \"name\": \"Dell\", \"email\": \"\", \"cpf\": \"064.380.240-15\"}";

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Informe os dados corretamente."))
                .andExpect(jsonPath("$.data[0].email").value("E-mail é obrigatório."))
                .andExpect(jsonPath("$.details").exists())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("should return badrequest when email is invalid")
    void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        String userDtoJson = "{ \"name\": \"Dell\", \"email\": \"dell.devemail\", \"cpf\": \"064.380.240-15\"}";

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Informe os dados corretamente."))
                .andExpect(jsonPath("$.data[0].email").value("E-mail inválido."))
                .andExpect(jsonPath("$.details").exists())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("should return badrequest when cpf is empty")
    void shouldReturnBadRequestWhenCpfIsEmpty() throws Exception {
        String userDtoJson = "{ \"name\": \"Dell\", \"email\": \"dell.dev@email.com\", \"cpf\": \"\"}";

        mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userDtoJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Informe os dados corretamente."))
            .andExpect(jsonPath("$.data[0].cpf[0]").value("CPF inválido."))
            .andExpect(jsonPath("$.data[0].cpf[1]").value("CPF é obrigatório."))
            .andExpect(jsonPath("$.details").exists())
            .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("should return badrequest when cpf is invalid")
    void shouldReturnBadRequestWhenCpfIsInvalid() throws Exception {
        String userDtoJson = "{ \"name\": \"Dell\", \"email\": \"dell.dev@email.com\", \"cpf\": \"438024015\"}";

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Informe os dados corretamente."))
                .andExpect(jsonPath("$.data[0].cpf").value("CPF inválido."))
                .andExpect(jsonPath("$.details").exists())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("should return a get user by id")
    void shouldReturnGetUserById() throws Exception {
        Instant createdAt = Instant.now();
        Instant updatedAt = Instant.now();
        Long userId = 1L;
    
        AddressDto addressDto = new AddressDto(1L, "70361-721", "Rua A", "...", "Centro", "Fortaleza", "CE", userId, true, createdAt, updatedAt);

        List<AddressDto> addressList = Collections.singletonList(addressDto);

        UserDto userDto = new UserDto(userId, "Fátima Catarina Mariah Brito", "fatima-brito86@moncoes.com.br", "175.471.282-70", true, createdAt, updatedAt, addressList);

        User userModel = new User();

        when(userService.validateGetUser(userId)).thenReturn(Optional.of(userModel));
        when(userService.getUser(userId)).thenReturn(userDto);

        mockMvc.perform(get("/api/users/{id}", userId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Registro listado."))
            .andExpect(jsonPath("$.data.id").value(userDto.id()))
            .andExpect(jsonPath("$.data.name").value(userDto.name()))
            .andExpect(jsonPath("$.data.email").value(userDto.email()))
            .andExpect(jsonPath("$.data.cpf").value(userDto.cpf()))
            .andExpect(jsonPath("$.data.active").value(userDto.active()))
            .andExpect(jsonPath("$.data.updatedAt").value(userDto.createdAt().toString()))
            .andExpect(jsonPath("$.data.createdAt").value(userDto.updatedAt().toString()))
            .andExpect(jsonPath("$.data.addresses[0]").exists())
            .andExpect(jsonPath("$.data.addresses[0].id").value(userDto.addresses().get(0).id()))
            .andExpect(jsonPath("$.data.addresses[0].cep").value(userDto.addresses().get(0).cep()))
            .andExpect(jsonPath("$.data.addresses[0].logradouro").value(userDto.addresses().get(0).logradouro()))
            .andExpect(jsonPath("$.data.addresses[0].complemento").value(userDto.addresses().get(0).complemento()))
            .andExpect(jsonPath("$.data.addresses[0].bairro").value(userDto.addresses().get(0).bairro()))
            .andExpect(jsonPath("$.data.addresses[0].localidade").value(userDto.addresses().get(0).localidade()))
            .andExpect(jsonPath("$.data.addresses[0].uf").value(userDto.addresses().get(0).uf()))
            .andExpect(jsonPath("$.data.addresses[0].userId").value(userDto.addresses().get(0).userId()))
            .andExpect(jsonPath("$.data.addresses[0].active").value(userDto.addresses().get(0).active()))
            .andExpect(jsonPath("$.data.addresses[0].createdAt").value(userDto.addresses().get(0).createdAt().toString()))
            .andExpect(jsonPath("$.data.addresses[0].updatedAt").value(userDto.addresses().get(0).updatedAt().toString()))
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
    
        AddressDto addressDto = new AddressDto(1L, "70361-721", "Rua A", "...", "Centro", "Fortaleza", "CE", 1L, true, createdAt0, updatedAt0);

        List<AddressDto> addressList = Collections.singletonList(addressDto);
        List<AddressDto> addressListEmpity = Collections.emptyList();

        List<UserDto> users = Arrays.asList(
            new UserDto(1L, "Fátima Catarina Mariah Brito", "fatima-brito86@moncoes.com.br", "175.471.282-70", false, createdAt0, updatedAt0, addressList),
            new UserDto(2L, "Sophie Silvana da Mata", "sophie.silvana.damata@lojascentrodamoda.com.br", "767.439.425-53", true, createdAt1, updatedAt1, addressListEmpity)
        );
    
        when(userService.getUsers()).thenReturn(users);
    
        mockMvc.perform(get("/api/users")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Registros listados."))

            .andExpect(jsonPath("$.data[0].id").value(users.get(0).id()))
            .andExpect(jsonPath("$.data[0].name").value(users.get(0).name()))
            .andExpect(jsonPath("$.data[0].email").value(users.get(0).email()))
            .andExpect(jsonPath("$.data[0].cpf").value(users.get(0).cpf()))
            .andExpect(jsonPath("$.data[0].active").value(users.get(0).active()))
            .andExpect(jsonPath("$.data[0].updatedAt").value(users.get(0).updatedAt().toString()))
            .andExpect(jsonPath("$.data[0].createdAt").value(users.get(0).createdAt().toString()))
            .andExpect(jsonPath("$.data[0].addresses[0]").exists())
            .andExpect(jsonPath("$.data[0].addresses[0].id").value(users.get(0).addresses().get(0).id()))
            .andExpect(jsonPath("$.data[0].addresses[0].cep").value(users.get(0).addresses().get(0).cep()))
            .andExpect(jsonPath("$.data[0].addresses[0].logradouro").value(users.get(0).addresses().get(0).logradouro()))
            .andExpect(jsonPath("$.data[0].addresses[0].complemento").value(users.get(0).addresses().get(0).complemento()))
            .andExpect(jsonPath("$.data[0].addresses[0].bairro").value(users.get(0).addresses().get(0).bairro()))
            .andExpect(jsonPath("$.data[0].addresses[0].localidade").value(users.get(0).addresses().get(0).localidade()))
            .andExpect(jsonPath("$.data[0].addresses[0].uf").value(users.get(0).addresses().get(0).uf()))
            .andExpect(jsonPath("$.data[0].addresses[0].userId").value(users.get(0).addresses().get(0).userId()))
            .andExpect(jsonPath("$.data[0].addresses[0].active").value(users.get(0).addresses().get(0).active()))
            .andExpect(jsonPath("$.data[0].addresses[0].createdAt").value(users.get(0).addresses().get(0).createdAt().toString()))
            .andExpect(jsonPath("$.data[0].addresses[0].updatedAt").value(users.get(0).addresses().get(0).updatedAt().toString()))

            .andExpect(jsonPath("$.data[1].id").value(users.get(1).id()))
            .andExpect(jsonPath("$.data[1].name").value(users.get(1).name()))
            .andExpect(jsonPath("$.data[1].email").value(users.get(1).email()))
            .andExpect(jsonPath("$.data[1].cpf").value(users.get(1).cpf()))
            .andExpect(jsonPath("$.data[1].active").value(users.get(1).active()))
            .andExpect(jsonPath("$.data[1].updatedAt").value(users.get(1).updatedAt().toString()))
            .andExpect(jsonPath("$.data[1].createdAt").value(users.get(1).createdAt().toString()))
            .andExpect(jsonPath("$.data[1].addresses").isEmpty())

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
