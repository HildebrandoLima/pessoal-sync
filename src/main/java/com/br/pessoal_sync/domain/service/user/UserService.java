package com.br.pessoal_sync.domain.service.user;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.pessoal_sync.domain.dto.UserDto;
import com.br.pessoal_sync.domain.exception.ConflictException;
import com.br.pessoal_sync.domain.exception.InternalServerException;
import com.br.pessoal_sync.domain.exception.NotFoundException;
import com.br.pessoal_sync.domain.model.User;
import com.br.pessoal_sync.http.validator.UserValidator;

@Service
public class UserService implements UserInterfaceService {

    private final UserValidator userValidator;
    private final UserDataService userDataService;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserValidator userValidator, UserDataService userDataService) {
        this.userValidator = userValidator;
        this.userDataService = userDataService;
    }

    public Long createUser(UserDto userDto) {
        try {
            userValidator.validateUser(userDto);
            return userDataService.createUser(userDto);
        } catch (ConflictException e) {
            logger.error("Erro ao criar usuário: ", e);
            throw e;
        } catch (InternalServerException e) {
            logger.error("Erro inesperado ao criar usuário: ", e);
            throw e;
        }
    }

    public Optional<User> getUser(Long id) {
        userValidator.validateAndGetUser(id);
        return userDataService.getUser(id);
    }

    public List<User> getUsers() {
        return userDataService.getUsers();
    }

    public Long updateUser(Long id, UserDto userDto) {
        try {
            userValidator.validateAndGetUser(id);
            return userDataService.updateUser(id, userDto);
        } catch (NotFoundException e) {
            logger.error("Erro ao buscar usuário: ", e);
            throw e;
        } catch (InternalServerException e) {
            logger.error("Erro inesperado ao alterar usuário: ", e);
            throw e;
        }
    }

    public void deleteUser(Long id) {
        try {
            userValidator.validateAndGetUser(id);
            userDataService.deleteUser(id);
        } catch (NotFoundException e) {
            logger.error("Erro ao buscar usuário: ", e);
            throw e;
        } catch (InternalServerException e) {
            logger.error("Erro inesperado ao deletar usuário: ", e);
            throw e;
        }
    }
}
