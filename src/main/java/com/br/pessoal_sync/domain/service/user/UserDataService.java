package com.br.pessoal_sync.domain.service.user;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.pessoal_sync.data.repository.UserRepository;
import com.br.pessoal_sync.domain.dto.UserDto;
import com.br.pessoal_sync.domain.exception.NotFoundException;
import com.br.pessoal_sync.domain.model.User;

@Service
public class UserDataService implements UserInterfaceService {

    private UserRepository userRepository;

    @Autowired
    public UserDataService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long createUser(UserDto userDto) {
        var user = new User();
        user.setName(userDto.name());
        user.setEmail(userDto.email());
        user.setCpf(userDto.cpf());
        user.setIsActive(true);
        user.setCreatedAt(Instant.now());
        return userRepository.save(user).getId();
    }

    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Long updateUser(Long id, UserDto userdDto) {
        User user = userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        user.setName(userdDto.name());
        user.setEmail(userdDto.email());
        user.setIsActive(userdDto.active());
        user.setUpdatedAt(Instant.now());
        return userRepository.save(user).getId();
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Usuário não encontrado com ID: " + id);
        }
        userRepository.deleteById(id);
    }
}
