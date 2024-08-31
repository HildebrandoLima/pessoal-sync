package com.br.pessoal_sync.service.user;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.pessoal_sync.domain.dto.AddressDto;
import com.br.pessoal_sync.domain.dto.UserDto;
import com.br.pessoal_sync.domain.exception.InternalServerException;
import com.br.pessoal_sync.domain.model.User;
import com.br.pessoal_sync.repository.UserRepository;

@Service
public class UserImplService implements UserService {

    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserImplService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long createUser(UserDto userDto) {
        try {
            var user = new User();
            user.setName(userDto.name());
            user.setEmail(userDto.email());
            user.setCpf(userDto.cpf());
            user.setIsActive(true);
            user.setCreatedAt(Instant.now());
            return userRepository.save(user).getId();
        } catch (InternalServerException e) {
            InternalServerMessage(e);
            throw e;
        }
    }

    public UserDto getUser(Long id) {
        return validateGetUser(id).map(user -> {
            List<AddressDto> addressDtos = user.getAddress()
            .stream()
            .map(address -> new AddressDto(
                address.getId(),
                address.getCep(),
                address.getLogradouro(),
                address.getComplemento(),
                address.getBairro(),
                address.getLocalidade(),
                address.getUf(),
                address.getUser().getId(),
                address.getIsActive(),
                address.getCreatedAt(),
                address.getUpdatedAt()
            )).toList();

            return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCpf(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                addressDtos
            );
        }).orElse(null);
    }

    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
            .map(user -> {
                List<AddressDto> addressDtos = user.getAddress()
                    .stream()
                    .map(address -> new AddressDto(
                        address.getId(),
                        address.getCep(),
                        address.getLogradouro(),
                        address.getComplemento(),
                        address.getBairro(),
                        address.getLocalidade(),
                        address.getUf(),
                        user.getId(),
                        address.getIsActive(),
                        address.getCreatedAt(),
                        address.getUpdatedAt()
                    )).collect(Collectors.toList());

                return new UserDto(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getCpf(),
                    user.isActive(),
                    user.getCreatedAt(),
                    user.getUpdatedAt(),
                    addressDtos
                );
            })
            .collect(Collectors.toList());
    }

    public Long updateUser(User user, UserDto userDto) {
        try {
            user.setName(userDto.name());
            user.setEmail(userDto.email());
            user.setIsActive(userDto.active());
            user.setUpdatedAt(Instant.now());
            return userRepository.save(user).getId();
        } catch (InternalServerException e) {
            InternalServerMessage(e);
            throw e;
        }
    }

    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (InternalServerException e) {
            InternalServerMessage(e);
            throw e;
        }
    }

    public String validateUser(UserDto userDto) {
        if (!userRepository.findByName(userDto.name()).isEmpty()) {
            return "Nome já está em uso.";
        }

        if (!userRepository.findByEmail(userDto.email()).isEmpty()) {
            return "E-mail já está em uso.";
        }

        if (!userRepository.findByCpf(userDto.cpf()).isEmpty()) {
            return "CPF já está em uso.";
        }
        return null;
    }

    public Optional<User> validateGetUser(Long id) {
        return userRepository.findById(id);
    }

    private void InternalServerMessage(InternalServerException e) {
        logger.error("Erro inesperado ao deletar usuário: ", e);
    }
}
