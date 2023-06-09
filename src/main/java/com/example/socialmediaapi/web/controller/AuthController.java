package com.example.socialmediaapi.web.controller;

import com.example.socialmediaapi.domain.User;
import com.example.socialmediaapi.service.AuthService;
import com.example.socialmediaapi.service.UserService;
import com.example.socialmediaapi.web.dto.auth.JwtRequest;
import com.example.socialmediaapi.web.dto.auth.JwtResponse;
import com.example.socialmediaapi.web.dto.UserDto;
import com.example.socialmediaapi.web.mappers.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Auth Controller", description = "Auth API")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping("/login")
    @Operation(summary = "Login")
    public JwtResponse login(@Valid @RequestBody JwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register")
    public void register(@Valid @RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        userService.createUser(user);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token")
    public JwtResponse refresh(@RequestBody String refreshToken) {
        return authService.refresh(refreshToken);
    }
}
