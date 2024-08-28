package com.br.pessoal_sync.unit;

import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import com.br.pessoal_sync.domain.dto.UserDto;
import com.br.pessoal_sync.domain.exception.ConflictException;
import com.br.pessoal_sync.domain.exception.NotFoundException;
import com.br.pessoal_sync.domain.model.User;
import com.br.pessoal_sync.http.validator.UserValidator;
import com.br.pessoal_sync.service.user.UserImplService;
import com.br.pessoal_sync.service.user.UserService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserValidator userValidator;

    @Mock
    private UserImplService userDataService;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("should create a user with success")
    void shouldCreateUserWithSuccess() {
        // Arange
        UserDto userDto = new UserDto("Dell", "dell.dev@email.com", "064.380.240-15", true);

        // Act
        when(userDataService.createUser(userDto)).thenReturn(1L);
        Long userId = userService.createUser(userDto);

        // Assert
        assertNotNull(userId);
        verify(userValidator).validateUser(userDto);
        verify(userDataService).createUser(userDto);
    }

    @Test
    @DisplayName("should create a user with conflict exception")
    void shouldCreateUserConflictException() {
        // Arange
        UserDto userDto = new UserDto("Dell", "dell.dev@email.com", "064.380.240-15", true);

        // Act
        doThrow(new ConflictException("Nome já está em uso.")).when(userValidator).validateUser(userDto);

        // Assert
        assertThrows(ConflictException.class, () -> userService.createUser(userDto));
    }

    @Test
    @DisplayName("should retrieve a user by id")
    void shouldRetrieveUserById() {
        // Arrange
        Long userId = 1L;
        User user = new User("Dell", "dell.dev@email.com", "123.456.789-01", true, Instant.now(), null);
        when(userDataService.getUser(userId)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUser(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userDataService).getUser(userId);
    }

    @Test
    @DisplayName("should retrieve a user by id with notFound exception")
    void shouldRetrieveUserByIdNotFoundException() {
        // Arange
        Long userId = 1L;

        // Act
        doThrow(new NotFoundException("Usuário não encontrado.")).when(userValidator).validateAndGetUser(userId);

        // Assert
        assertThrows(NotFoundException.class, () -> userService.getUser(userId));
    }

    @Test
    @DisplayName("should retrieve all users")
    void shouldRetrieveAllUsers() {
        // Arrange
        List<User> users = Arrays.asList(
            new User("Dell", "dell.dev@email.com", "123.456.789-01", true, Instant.now(), null),
            new User("Alice", "alice@example.com", "987.654.321-00", false, Instant.now(), null)
        );
        when(userDataService.getUsers()).thenReturn(users);

        // Act
        List<User> result = userService.getUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(users, result);
        verify(userDataService).getUsers();
    }

    @Test
    @DisplayName("should update a user with success")
    void shouldUpdateUserWithSuccess() {
        // Arrange
        Long userId = 1L;
        UserDto userDto = new UserDto("Dell", "dell.dev@email.com", "123.456.789-01", true);
        when(userDataService.updateUser(userId, userDto)).thenReturn(userId);

        // Act
        Long result = userService.updateUser(userId, userDto);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result);
        verify(userDataService).updateUser(userId, userDto);
    }

    @Test
    @DisplayName("should update a user with notFound exception")
    void shouldUpdateUserNotFoundException() {
        // Arange
        Long userId = 1L;
        UserDto userDto = new UserDto("Dell", "dell.dev@email.com", "123.456.789-01", true);

        // Act
        doThrow(new NotFoundException("Usuário não encontrado.")).when(userValidator).validateAndGetUser(userId);

        // Assert
        assertThrows(NotFoundException.class, () -> userService.updateUser(userId, userDto));
    }

    @Test
    @DisplayName("should delete a user with success")
    void shouldDeleteUserWithSuccess() {
        // Arrange
        Long userId = 1L;

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userDataService).deleteUser(userId);
    }

    @Test
    @DisplayName("should update a user with notFound exception")
    void shouldDeleteUserNotFoundException() {
        // Arange
        Long userId = 1L;

        // Act
        doThrow(new NotFoundException("Usuário não encontrado.")).when(userValidator).validateAndGetUser(userId);

        // Assert
        assertThrows(NotFoundException.class, () -> userService.deleteUser(userId));
    }

}
