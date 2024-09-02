package com.br.pessoal_sync.domain.dto;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddressDto(

    Long id,

    @NotBlank(message = "CEP é obrigatório.")
    @Size(min = 9, max = 9, message = "CEP inválido.")
    String cep,

    @NotBlank(message = "Logradouro é obrigatório.")
    String logradouro,

    @NotBlank(message = "Complemento é obrigatório.")
    String complemento,

    @NotBlank(message = "Bairro é obrigatório.")
    String bairro,

    @NotBlank(message = "Localidade é obrigatório.")
    String localidade,

    @NotBlank(message = "UF é obrigatório.")
    String uf,

    @NotNull(message = "UsuárioId é obrigatório.")
    Long userId,

    boolean active,
    Instant createdAt,
    Instant updatedAt
){}
