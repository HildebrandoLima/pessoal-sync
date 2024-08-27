package com.br.pessoal_sync.http.controller;

import java.util.List;
import jakarta.validation.Valid;

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
import com.br.pessoal_sync.domain.exception.Response;
import com.br.pessoal_sync.domain.service.UserService;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Response> createUser(@Valid @RequestBody UserDto userDto) {
        userService.createUser(userDto);
        Response response = new Response(
            "Registro criado.",
            List.of(),
            HttpStatus.CREATED.value()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getUserById(@PathVariable("id") Long id) {
        var user = userService.getUserById(id);
        Response response = new Response(
            "Registro listado.",
            List.of(user.get()),
            HttpStatus.OK.value()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Response> getUsers() {
        var users = userService.getUsersAll();
        Response response = new Response(
            "Registros listados.",
            List.of(users),
            HttpStatus.OK.value()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserDto userDto) {
        userService.updateUser(id, userDto);
        Response response = new Response(
            "Registro alterado.",
            List.of(),
            HttpStatus.NO_CONTENT.value()
        );
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        Response response = new Response(
            "Registro deletado.",
            List.of(),
            HttpStatus.NO_CONTENT.value()
        );
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
