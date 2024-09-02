package com.br.pessoal_sync.unit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.br.pessoal_sync.domain.dto.AddressDto;
import com.br.pessoal_sync.domain.model.Address;
import com.br.pessoal_sync.domain.model.User;
import com.br.pessoal_sync.repository.AddressRepository;
import com.br.pessoal_sync.repository.UserRepository;
import com.br.pessoal_sync.service.address.AddressImplService;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AddressImplService addressService;

    @Test
    @DisplayName("should create a address with success")
    void shouldCreateAddressWithSuccess() {
        // Arange
        Long userId = 1L;
        Instant date = Instant.now();
        List<Address> addressListEmpity = Collections.emptyList();
        User user = new User(userId, "Dell", "dell.dev@email.com", "914.667.290-74", true, date, date, addressListEmpity);
        AddressDto addressDto = new AddressDto(1L, "70361-721", "Rua A", "...", "Centro", "Fortaleza", "CE", 1L, true, date, date);

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
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        // Act
        String cep = addressService.createAddress(user, addressDto);

        // Assert
        assertNotNull(cep);
        assertEquals(user.getId(), address.getUser().getId());
        assertEquals(userId, address.getUser().getId());
        assertEquals(userId, user.getId());
        verify(addressRepository).save(any(Address.class));
    }

    @Test
    @DisplayName("should delete a address with success")
    void shouldDeleteAddressWithSuccess() {
        // Arrange
        Long addressId = 1L;
        doNothing().when(addressRepository).deleteById(addressId);

        // Act and Assert
        assertDoesNotThrow(() -> addressService.deleteAddress(addressId));
        verify(addressRepository).deleteById(addressId);
    }

    @Test
    @DisplayName("should should return user when exists")
    void shouldReturnUserWhenUserExists() {
        // Arrange
        User user = new User();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = addressService.validateGetUser(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    @DisplayName("should return empty when user does not exists")
    void shouldReturnEmptyWhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = addressService.validateGetUser(1L);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("should return address when address exists")
    void shouldReturnAddressWhenAddressExists() {
        // Arrange
        Address address = new Address();

        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));

        // Act
        Optional<Address> result = addressService.validateGetAddress(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(address, result.get());
    }

    @Test
    @DisplayName("should return empty when address does not exist")
    void shouldReturnEmptyWhenAddressDoesNotExist() {
        // Arrange
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Address> result = addressService.validateGetAddress(1L);

        // Assert
        assertTrue(result.isEmpty());
    }

}
