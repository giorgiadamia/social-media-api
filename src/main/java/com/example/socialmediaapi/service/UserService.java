package com.example.socialmediaapi.service;

import com.example.socialmediaapi.domain.Role;
import com.example.socialmediaapi.domain.User;
import com.example.socialmediaapi.repository.UserRepository;
import com.example.socialmediaapi.web.dto.user.UserDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username: " + username + " is not found"));
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with this id does not exist"));
    }

    @Transactional
    public void updateUser(Long id, UserDto userDto) {
        User userFromMemory = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with this id does not exist"));
        if (userDto.getUsername() != null) {
            userFromMemory.setUsername(userDto.getUsername());
        }
        if (userDto.getEmail() != null) {
            userFromMemory.setEmail(userDto.getEmail());
        }
        if (userDto.getPassword() != null) {
            userFromMemory.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        userRepository.save(userFromMemory);
    }

    @Transactional
    public void createUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("User with such username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(Role.ROLE_USER));
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
