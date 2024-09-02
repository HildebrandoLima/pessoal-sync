package com.br.pessoal_sync.service.address;

import java.time.Instant;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.pessoal_sync.domain.dto.AddressDto;
import com.br.pessoal_sync.domain.exception.InternalServerException;
import com.br.pessoal_sync.domain.model.Address;
import com.br.pessoal_sync.domain.model.User;
import com.br.pessoal_sync.repository.AddressRepository;
import com.br.pessoal_sync.repository.UserRepository;

@Service
public class AddressImplService implements AddressService {

    private AddressRepository addressRepository;
    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(AddressService.class);

    @Autowired
    public AddressImplService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public String createAddress(User user, AddressDto addressDto) {
        try {
            var address = new Address();
            address.setCep(addressDto.cep());
            address.setLogradouro(addressDto.logradouro());
            address.setComplemento(addressDto.complemento());
            address.setBairro(addressDto.bairro());
            address.setLocalidade(addressDto.localidade());
            address.setUf(addressDto.uf());
            address.setIsActive(true);
            address.setCreatedAt(Instant.now());
            address.setUser(user);
            return addressRepository.save(address).getCep();
        } catch (InternalServerException e) {
            InternalServerMessage(e);
            throw e;
        }
    }

    public void deleteAddress(Long id) {
        try {
            addressRepository.deleteById(id);
        } catch (InternalServerException e) {
            InternalServerMessage(e);
            throw e;
        }
    }

    public Optional<User> validateGetUser(Long id) {
        return userRepository.findById(id);
    }

    public Optional<Address> validateGetAddress(Long id) {
        return addressRepository.findById(id);
    }

    private void InternalServerMessage(InternalServerException e) {
        logger.error("Erro inesperado ao deletar usuário: ", e);
    }

}
