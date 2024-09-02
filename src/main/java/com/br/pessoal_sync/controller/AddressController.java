package com.br.pessoal_sync.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.pessoal_sync.domain.dto.AddressDto;
import com.br.pessoal_sync.domain.exception.NotFoundException;
import com.br.pessoal_sync.domain.model.Response;
import com.br.pessoal_sync.domain.model.User;
import com.br.pessoal_sync.service.address.AddressImplService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/address")
@Validated
public class AddressController {

    private AddressImplService addressService;

    @Autowired
    public AddressController(AddressImplService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<Response> createAddress(@Valid @RequestBody AddressDto addressDto) {
        User user = addressService.validateGetUser(addressDto.userId())
        .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        String cep = addressService.createAddress(user, addressDto);
        return response("Registro criado.", cep, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteAddressById(@PathVariable("id") Long id) {
        addressService.validateGetAddress(id)
        .orElseThrow(() -> new NotFoundException("Endereço não encontrado."));

        addressService.deleteAddress(id);
        return response("Registro deletado.", List.of(), HttpStatus.OK);
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
