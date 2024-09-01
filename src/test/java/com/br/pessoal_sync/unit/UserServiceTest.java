package com.br.pessoal_sync.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import com.br.pessoal_sync.domain.dto.AddressDto;
import com.br.pessoal_sync.domain.dto.UserDto;
import com.br.pessoal_sync.domain.model.Address;
import com.br.pessoal_sync.domain.model.User;
import com.br.pessoal_sync.repository.UserRepository;
import com.br.pessoal_sync.service.user.UserImplService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    private UserImplService userService;

    private List<AddressDto> addressListEmpity = Collections.emptyList();
    private UserDto userDto = new UserDto(null, "Dell", "dell.dev@email.com", "914.667.290-74", true, null, null, addressListEmpity);

    @Test
    @DisplayName("should create a user with success")
    void shouldCreateUserWithSuccess() {
        // Arange
        User user = new User();
        user.setId(1L);
        user.setName(userDto.name());
        user.setEmail(userDto.email());
        user.setCpf(userDto.cpf());
        user.setIsActive(true);
        user.setCreatedAt(Instant.now());
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        Long userId = userService.createUser(userDto);

        // Assert
        assertNotNull(userId);
        assertEquals(user.getId(), userId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("should retrieve a user by id")
    void shouldRetrieveUserById() {
        // Arrange
        Long userId = 1L;
        Instant date = Instant.now();
    
        User userModel = new User(userId, "Dell", "dell.dev@email.com", "914.667.290-74", true, date, date, null);
    
        Address addressModel = new Address(1L, "70361-721", "Rua A", "...", "Centro", "Fortaleza", "CE", true, date, date, userModel);
        List<Address> addressModelList = Collections.singletonList(addressModel);
    
        userModel.setAddress(addressModelList);

        AddressDto addressDto = new AddressDto(1L, "70361-721", "Rua A", "...", "Centro", "Fortaleza", "CE", userId, true, date, date);
        UserDto userDto = new UserDto(userId, "Dell", "dell.dev@email.com", "914.667.290-74", true, date, date, Collections.singletonList(addressDto));

        when(userRepository.findById(userId)).thenReturn(Optional.of(userModel));
    
        // Act
        UserDto result = userService.getUser(userId);
    
        // Assert
        assertNotNull(result);
        assertEquals(userDto, result);
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("should retrieve all users")
    void shouldRetrieveAllUsers() {
        Instant date = Instant.now();

        AddressDto addressDto = new AddressDto(1L, "70361-721", "Rua A", "...", "Centro", "Fortaleza", "CE", 1L, true, date, date);
        Address addressModel = new Address(1L, "70361-721", "Rua A", "...", "Centro", "Fortaleza", "CE", true, date, date, null);

        List<AddressDto> addressDtoList = Collections.singletonList(addressDto);
        List<Address> addressModelList = Collections.singletonList(addressModel);
        List<Address> addressListEmpty = Collections.emptyList();

        // Arrange
        List<UserDto> usersDto = Arrays.asList(
            new UserDto(1L, "Dell", "dell.dev@email.com", "914.667.290-74", true, date, date, addressDtoList),
            new UserDto(2L, "Ashley", "ashley@email.com", "662.569.440-11", false, date, date, addressListEmpity)
        );
        List<User> usersModel =  Arrays.asList(
            new User(1L, "Dell", "dell.dev@email.com", "914.667.290-74", true, date, date, addressModelList),
            new User(2L, "Ashley", "ashley@email.com", "662.569.440-11", false, date, date, addressListEmpty)
        );

        when(userRepository.findAll()).thenReturn(usersModel);

        // Act
        List<UserDto> result = userService.getUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(usersDto, result);
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("should update a user with success")
    void shouldUpdateUserWithSuccess() {
        // Arrange
        User existingUser  = new User();
        existingUser.setId(1L);
        existingUser.setName(userDto.name());
        existingUser.setEmail(userDto.email());
        existingUser.setCpf(userDto.cpf());
        existingUser.setIsActive(true);
        existingUser.setUpdatedAt(Instant.now());

        User updatedUser = new User();
        updatedUser.setId(existingUser.getId());
        updatedUser.setName(userDto.name());
        updatedUser.setEmail(userDto.email());
        updatedUser.setCpf(userDto.cpf());
        updatedUser.setIsActive(userDto.active());
        updatedUser.setUpdatedAt(Instant.now());

        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // Act
        Long userId = userService.updateUser(existingUser, userDto);

        // Assert
        assertNotNull(userId);
        assertEquals(existingUser.getId(), userId);
        assertEquals(updatedUser.getId(), userId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("should delete a user with success")
    void shouldDeleteUserWithSuccess() {
        // Arrange
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);

        // Act and Assert
        assertDoesNotThrow(() -> userService.deleteUser(userId));
        verify(userRepository).deleteById(userId);
    }

    @Test
    @DisplayName("should validate a user name conflict message")
    void shouldvalidateUserNameConflictMessage() {
        // Arange
        when(userRepository.findByName(userDto.name())).thenReturn(Collections.singletonList(new User()));

        // Act
        String result = userService.validateUser(userDto);

        // Assert
        assertEquals("Nome já está em uso.", result);
    }

    @Test
    @DisplayName("should validate a user e-mail conflict message")
    void shouldvalidateUserEmailConflictMessage() {
        // Arange
        when(userRepository.findByName(userDto.name())).thenReturn(Collections.emptyList());
        when(userRepository.findByEmail(userDto.email())).thenReturn(Collections.singletonList(new User()));

        // Act
        String result = userService.validateUser(userDto);

        // Assert
        assertEquals("E-mail já está em uso.", result);
    }

    @Test
    @DisplayName("should validate a user cpf conflict message")
    void shouldvalidateUserCpfConflictMessage() {
        // Arange
        when(userRepository.findByName(userDto.name())).thenReturn(Collections.emptyList());
        when(userRepository.findByEmail(userDto.email())).thenReturn(Collections.emptyList());
        when(userRepository.findByCpf(userDto.cpf())).thenReturn(Collections.singletonList(new User()));

        // Act
        String result = userService.validateUser(userDto);

        // Assert
        assertEquals("CPF já está em uso.", result);
    }

    @Test
    @DisplayName("should validate a user error conflict message")
    void shouldvalidateUserErrorConflictMessage() {
        // Arange
        when(userRepository.findByName(userDto.name())).thenReturn(Collections.emptyList());
        when(userRepository.findByEmail(userDto.email())).thenReturn(Collections.emptyList());
        when(userRepository.findByCpf(userDto.cpf())).thenReturn(Collections.emptyList());

        // Act
        String result = userService.validateUser(userDto);

        // Assert
        assertNull(result);
        assertEquals(null, result);
    }

}
