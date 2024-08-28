package com.br.pessoal_sync.domain.service.user;

import java.util.List;

import com.br.pessoal_sync.domain.dto.UserDto;
import com.br.pessoal_sync.domain.model.User;

public interface UserInterfaceService {

    public Long createUser(UserDto userDto);

    public User getUser(Long id);

    public List<User> getUsers();

    public Long updateUser(Long id, UserDto userDto);

    public void deleteUser(Long id);

}
