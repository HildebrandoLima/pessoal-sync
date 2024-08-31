package com.br.pessoal_sync.service.user;

import java.util.List;

import com.br.pessoal_sync.domain.dto.UserDto;
import com.br.pessoal_sync.domain.model.User;

public interface UserService {

    public Long createUser(UserDto userDto);

    public UserDto getUser(Long id);

    public List<UserDto> getUsers();

    public Long updateUser(User user, UserDto userDto);

    public void deleteUser(Long id);

}
