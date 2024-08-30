package com.br.pessoal_sync.controller;

import java.util.List;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.pessoal_sync.domain.dto.UserDto;
import com.br.pessoal_sync.domain.exception.ConflictException;
import com.br.pessoal_sync.domain.exception.NotFoundException;
import com.br.pessoal_sync.domain.exception.Response;
import com.br.pessoal_sync.domain.model.User;
import com.br.pessoal_sync.service.user.UserImplService;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private UserImplService userService;

    @Autowired
    public UserController(UserImplService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Response> createUser(@Valid @RequestBody UserDto userDto) {
        String message = userService.validateUser(userDto);
        if (message != null) {
            throw new ConflictException(message);
        }
        Long userId = userService.createUser(userDto);
        return response("Registro criado.", List.of(userId), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getUserById(@PathVariable("id") Long id) {
        validateUserException(id);
        var user = userService.getUser(id);
        return response("Registro listado.", List.of(user), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Response> getUsers() {
        var users = userService.getUsers();
        return response("Registros listados.", users, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserDto userDto) {
        User user = userService.validateGetUser(id)
        .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        userService.updateUser(user, userDto);
        return response("Registro alterado.", List.of(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteUserById(@PathVariable("id") Long id) {
        validateUserException(id);
        userService.deleteUser(id);
        return response("Registro deletado.", List.of(), HttpStatus.OK);
    }

    private void validateUserException(Long id) {
        userService.validateGetUser(id)
        .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
    }

    private ResponseEntity<Response> response(String message, Object data, HttpStatus status) {
        Response response = new Response(
            message,
            data,
            "",
            status.value()
        );
        return new ResponseEntity<>(response, status);
    }
}
