package com.br.pessoal_sync.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import com.br.pessoal_sync.domain.dto.UserDto;
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

    @Test
    @DisplayName("should create a user with success")
    void shouldCreateUserWithSuccess() {
        // Arange
        UserDto userDto = new UserDto("Dell", "dell.dev@email.com", "914.667.290-74", true);
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
        User user = new User(1L, "Dell", "dell.dev@email.com", "914.667.290-74", true, Instant.now(), null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUser(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("should retrieve all users")
    void shouldRetrieveAllUsers() {
        // Arrange
        List<User> users = Arrays.asList(
            new User(1L, "Dell", "dell.dev@email.com", "914.667.290-74", true, Instant.now(), null),
            new User(2L, "Ashley", "ashley@email.com", "662.569.440-11", false, Instant.now(), null)
        );
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(users, result);
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("should update a user with success")
    void shouldUpdateUserWithSuccess() {
        // Arrange
        UserDto userDto = new UserDto("Dell", "dell.dev@email.com", "914.667.290-74", true);

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
        UserDto userDto = new UserDto("Dell", "dell.dev@email.com", "914.667.290-74", true);
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
        UserDto userDto = new UserDto("Dell", "dell.dev@email.com", "914.667.290-74", true);
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
        UserDto userDto = new UserDto("Dell", "dell.dev@email.com", "914.667.290-74", true);
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
        UserDto userDto = new UserDto("Dell", "dell.dev@email.com", "914.667.290-74", true);
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
