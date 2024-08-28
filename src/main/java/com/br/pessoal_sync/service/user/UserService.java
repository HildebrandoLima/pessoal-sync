package com.br.pessoal_sync.service.user;

import java.util.List;
import java.util.Optional;

import com.br.pessoal_sync.domain.dto.UserDto;
import com.br.pessoal_sync.domain.model.User;

public interface UserService {

    public Long createUser(UserDto userDto);

    public Optional<User> getUser(Long id);

    public List<User> getUsers();

    public Long updateUser(Long id, UserDto userDto);

    public void deleteUser(Long id);

}
