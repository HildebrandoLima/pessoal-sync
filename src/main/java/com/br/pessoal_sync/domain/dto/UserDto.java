package com.br.pessoal_sync.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDto(
    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome não deve exceder 100 caracteres")
    String name,
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    String email,
    @NotBlank(message = "O CPF é obrigatório")
    @Size(min = 14, max = 14, message = "O CPF deve ter exatamente 14 caracteres")
    String cpf,
    boolean active
){}
