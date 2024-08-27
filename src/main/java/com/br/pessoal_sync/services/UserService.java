package com.br.pessoal_sync.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.br.pessoal_sync.dtos.UserDto;
import com.br.pessoal_sync.exception.ConflictException;
import com.br.pessoal_sync.exception.NotFoundException;
import com.br.pessoal_sync.models.User;
import com.br.pessoal_sync.repositories.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long createUser(UserDto userDto) {
        checkUser(userDto);
        var user = new User();
        user.setName(userDto.name());
        user.setEmail(userDto.email());
        user.setCpf(userDto.cpf());
        user.setIsActive(true);
        user.setCreatedAt(Instant.now());
    
        var userId = userRepository.save(user);
        return userId.getId();
    }

    public Optional<User> getUserById(Long id) {
        var user = existingUser(id);
        return user;
    }

    public List<User> getUsersAll() {
        return userRepository.findAll();
    }

    public void updateUser(Long id, UserDto userdDto) {
        Optional<User> existing = existingUser(id);

        User user = existing.get();
        user.setName(userdDto.name());
        user.setEmail(userdDto.email());
        user.setIsActive(userdDto.active());
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        existingUser(id);
        userRepository.deleteById(id);
    }

    private void checkUser(UserDto userDto) {
        if (
            !userRepository.findByName(userDto.name()).isEmpty()
            &&
            !userRepository.findByEmail(userDto.email()).isEmpty()
            &&
            !userRepository.findByCpf(userDto.cpf()).isEmpty()
            ) {
            throw new ConflictException();
        }
    }

    private Optional<User> existingUser(Long id) {
        var user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException();
        }
        return user;
    }
}
