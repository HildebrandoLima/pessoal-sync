package com.br.pessoal_sync.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDto(
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "O nome não deve exceder 100 caracteres")
    String name,
    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    String email,
    @NotBlank(message = "CPF é obrigatório")
    @Size(min = 14, max = 14, message = "CPF inválido")
    String cpf,
    boolean active
){}
