package com.example.socialmediaapi.service;

import com.example.socialmediaapi.domain.Role;
import com.example.socialmediaapi.domain.User;
import com.example.socialmediaapi.repository.UserRepository;
import com.example.socialmediaapi.web.dto.UserDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserByUsername() {
        String username = "user";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        User result = userService.getUserByUsername(username);

        assertEquals(user, result);

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testGetUserByUsernameNotFoundUser() {
        String username = "user";

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserByUsername(username));

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testGetUserById() {
        Long id = 1L;
        User user = new User();
        user.setId(id);

        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));

        User result = userService.getUserById(id);

        assertEquals(user, result);

        verify(userRepository, times(1)).findById(id);
    }

    @Test
    public void testGetUserByIdNotFoundUser() {
        Long id = 100L;

        when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(id));

        verify(userRepository, times(1)).findById(id);
    }

    @Test
    public void testUpdateUser() {
        Long id = 1L;
        UserDto userDto = new UserDto();
        userDto.setUsername("newUsername");
        userDto.setEmail("newEmail@example.com");
        userDto.setPassword("newPassword");

        User existingUser = new User();
        existingUser.setId(id);

        when(userRepository.findById(id))
                .thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(userDto.getPassword()))
                .thenReturn("encodedPassword");
        when(userRepository.save(existingUser))
                .thenReturn(existingUser);

        assertDoesNotThrow(() -> userService.updateUser(id, userDto));

        assertEquals(userDto.getUsername(), existingUser.getUsername());
        assertEquals(userDto.getEmail(), existingUser.getEmail());
        assertEquals("encodedPassword", existingUser.getPassword());

        verify(userRepository, times(1)).findById(id);
        verify(passwordEncoder, times(1)).encode(userDto.getPassword());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void testUpdateUserNotFoundUser() {
        Long id = 100L;
        UserDto userDto = new UserDto();

        when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(id, userDto));

        verify(userRepository, times(1)).findById(id);
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(passwordEncoder);
    }

    @Test
    public void testCreateUser() {
        String decodedPassword = "testPassword";

        User user = new User();
        user.setUsername("testUser");
        user.setPassword(decodedPassword);

        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword()))
                .thenReturn("encodedPassword");
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        assertDoesNotThrow(() -> userService.createUser(user));

        assertEquals("testUser", user.getUsername());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(Set.of(Role.ROLE_USER), user.getRoles());

        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(passwordEncoder, times(1)).encode(decodedPassword);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testCreateUserExistingUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");

        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        assertThrows(IllegalStateException.class, () -> userService.createUser(user));

        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(passwordEncoder);
    }

    @Test
    public void testDeleteUser() {
        Long id = 1L;

        assertDoesNotThrow(() -> userService.deleteUser(id));

        verify(userRepository, times(1)).deleteById(id);
    }
}