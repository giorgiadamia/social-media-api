package com.example.socialmediaapi.web.controller;

import com.example.socialmediaapi.domain.User;
import com.example.socialmediaapi.service.AuthService;
import com.example.socialmediaapi.service.UserService;
import com.example.socialmediaapi.web.dto.auth.JwtRequest;
import com.example.socialmediaapi.web.dto.auth.JwtResponse;
import com.example.socialmediaapi.web.dto.user.UserDto;
import com.example.socialmediaapi.web.mappers.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping("/login")
    public JwtResponse login(@Valid @RequestBody JwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public void register(@Valid @RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        userService.createUser(user);
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody String refreshToken) {
        return authService.refresh(refreshToken);
    }
}
