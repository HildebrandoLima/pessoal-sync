package com.br.pessoal_sync.http.validator;

import com.br.pessoal_sync.data.repository.UserRepository;
import com.br.pessoal_sync.domain.dto.UserDto;
import com.br.pessoal_sync.domain.exception.ConflictException;
import com.br.pessoal_sync.domain.exception.NotFoundException;
import com.br.pessoal_sync.domain.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    private final UserRepository userRepository;

    @Autowired
    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateUser(UserDto userDto) {
        if (!userRepository.findByName(userDto.name()).isEmpty()) {
            throw new ConflictException("Nome já está em uso.");
        }

        if (!userRepository.findByEmail(userDto.email()).isEmpty()) {
            throw new ConflictException("E-mail já está em uso.");
        }

        if (!userRepository.findByCpf(userDto.cpf()).isEmpty()) {
            throw new ConflictException("CPF já está em uso.");
        }
    }

    public User validateAndGetUser(Long id) {
        return userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
    }
}
