package com.br.pessoal_sync.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.br.pessoal_sync.controller.AddressController;
import com.br.pessoal_sync.domain.dto.AddressDto;
import com.br.pessoal_sync.domain.model.Address;
import com.br.pessoal_sync.domain.model.User;
import com.br.pessoal_sync.service.address.AddressImplService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@WebMvcTest(AddressController.class)
public class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressImplService addressService;

    @Test
    @DisplayName("should return created success")
    void shouldReturnCreatedSuccess() throws Exception {
        String addressDtoJson = "{ \"userId\": 1, \"cep\": \"72220-167\", \"logradouro\": \"Rua Dr. Zé\", \"complemento\": \"Lado Ímpar\", \"bairro\": \"Centro\", \"localidade\": \"Fortaleza\", \"uf\": \"CE\"}";

        when(addressService.validateGetUser(1L)).thenReturn(Optional.of(new User()));
        when(addressService.createAddress(any(User.class), any(AddressDto.class))).thenReturn("72220-167");

        mockMvc.perform(post("/api/address")
            .contentType(MediaType.APPLICATION_JSON)
            .content(addressDtoJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.message").value("Registro criado."))
            .andExpect(jsonPath("$.data").value("72220-167"))
            .andExpect(jsonPath("$.details").value(""))
            .andExpect(jsonPath("$.status").value(201));
    }

    @Test
    @DisplayName("should return badrequest when cep is invalid")
    void shouldReturnBadRequestWhenCepIsInvalid() throws Exception {
        String addressDtoJson = "{ \"userId\": 1, \"cep\": \"72220167\", \"logradouro\": \"Rua Dr. Zé\", \"complemento\": \"Lado Ímpar\", \"bairro\": \"Centro\", \"localidade\": \"Fortaleza\", \"uf\": \"CE\"}";

        mockMvc.perform(post("/api/address")
            .contentType(MediaType.APPLICATION_JSON)
            .content(addressDtoJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Informe os dados corretamente."))
            .andExpect(jsonPath("$.data[0].cep").value("CEP inválido."))
            .andExpect(jsonPath("$.details").exists())
            .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("should return badrequest when cep is empty")
    void shouldReturnBadRequestWhenCepIsEmpty() throws Exception {
        String addressDtoJson = "{ \"userId\": 1, \"cep\": \"72220-167\", \"logradouro\": \"\", \"complemento\": \"Lado Ímpar\", \"bairro\": \"Centro\", \"localidade\": \"Fortaleza\", \"uf\": \"CE\"}";

        mockMvc.perform(post("/api/address")
            .contentType(MediaType.APPLICATION_JSON)
            .content(addressDtoJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Informe os dados corretamente."))
            .andExpect(jsonPath("$.data[0].logradouro").value("Logradouro é obrigatório."))
            .andExpect(jsonPath("$.details").exists())
            .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("should return badrequest when complement is empty")
    void shouldReturnBadRequestWhenComplementIsEmpty() throws Exception {
        String addressDtoJson = "{ \"userId\": 1, \"cep\": \"72220-167\", \"logradouro\": \"Rua Dr. Zé\", \"complemento\": \"\", \"bairro\": \"Centro\", \"localidade\": \"Fortaleza\", \"uf\": \"CE\"}";

        mockMvc.perform(post("/api/address")
            .contentType(MediaType.APPLICATION_JSON)
            .content(addressDtoJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Informe os dados corretamente."))
            .andExpect(jsonPath("$.data[0].complemento").value("Complemento é obrigatório."))
            .andExpect(jsonPath("$.details").exists())
            .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("should return badrequest when neighborhood is empty")
    void shouldReturnBadRequestWhenNeighborhoodIsEmpty() throws Exception {
        String addressDtoJson = "{ \"userId\": 1, \"cep\": \"72220-167\", \"logradouro\": \"Rua Dr. Zé\", \"complemento\": \"Lado Ímpar\", \"bairro\": \"\", \"localidade\": \"Fortaleza\", \"uf\": \"CE\"}";

        mockMvc.perform(post("/api/address")
            .contentType(MediaType.APPLICATION_JSON)
            .content(addressDtoJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Informe os dados corretamente."))
            .andExpect(jsonPath("$.data[0].bairro").value("Bairro é obrigatório."))
            .andExpect(jsonPath("$.details").exists())
            .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("should return badrequest when locality is empty")
    void shouldReturnBadRequestWhenLocalityIsEmpty() throws Exception {
        String addressDtoJson = "{ \"userId\": 1, \"cep\": \"72220-167\", \"logradouro\": \"Rua Dr. Zé\", \"complemento\": \"Lado Ímpar\", \"bairro\": \"Centro\", \"localidade\": \"\", \"uf\": \"CE\"}";

        mockMvc.perform(post("/api/address")
            .contentType(MediaType.APPLICATION_JSON)
            .content(addressDtoJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Informe os dados corretamente."))
            .andExpect(jsonPath("$.data[0].localidade").value("Localidade é obrigatório."))
            .andExpect(jsonPath("$.details").exists())
            .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("should return badrequest when uf is empty")
    void shouldReturnBadRequestWhenUFIsEmpty() throws Exception {
        String addressDtoJson = "{ \"userId\": 1, \"cep\": \"72220-167\", \"logradouro\": \"Rua Dr. Zé\", \"complemento\": \"Lado Ímpar\", \"bairro\": \"Centro\", \"localidade\": \"Fortaleza\", \"uf\": \"\"}";

        mockMvc.perform(post("/api/address")
            .contentType(MediaType.APPLICATION_JSON)
            .content(addressDtoJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Informe os dados corretamente."))
            .andExpect(jsonPath("$.data[0].uf").value("UF é obrigatório."))
            .andExpect(jsonPath("$.details").exists())
            .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("should return error when created user is not found")
    void shouldReturnErrorWhenCreatedUserIsNotFound() throws Exception {
        Long userId = 1L;
        String addressDtoJson = "{ \"userId\": 1, \"cep\": \"72220-167\", \"logradouro\": \"Rua Dr. Zé\", \"complemento\": \"Lado Ímpar\", \"bairro\": \"Centro\", \"localidade\": \"Fortaleza\", \"uf\": \"CE\"}";

        when(addressService.validateGetUser(userId)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/address")
            .contentType(MediaType.APPLICATION_JSON)
            .content(addressDtoJson))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Registro não encontrado."))
            .andExpect(jsonPath("$.data").isEmpty())
            .andExpect(jsonPath("$.details").value("Usuário não encontrado."))
            .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("should return deleted success")
    void shouldReturnDeletedSuccess() throws Exception {
        Long addressId = 1L;

        doNothing().when(addressService).deleteAddress(addressId);
        when(addressService.validateGetAddress(addressId)).thenReturn(Optional.of(new Address()));

         mockMvc.perform(delete("/api/address/{id}", addressId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Registro deletado."))
            .andExpect(jsonPath("$.data").isEmpty())
            .andExpect(jsonPath("$.details").value(""))
            .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    @DisplayName("should return error when deleted address is not found")
    void shouldReturnErrorWhenDeletedAddressIsNotFound() throws Exception {
        Long addressId = 1L;

        when(addressService.validateGetAddress(addressId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/address/{id}", addressId))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Registro não encontrado."))
            .andExpect(jsonPath("$.data").isEmpty())
            .andExpect(jsonPath("$.details").value("Endereço não encontrado."))
            .andExpect(jsonPath("$.status").value(404));
    }

}
