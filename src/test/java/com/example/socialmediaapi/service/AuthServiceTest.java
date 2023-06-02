package com.example.socialmediaapi.service;

import com.example.socialmediaapi.domain.Role;
import com.example.socialmediaapi.domain.User;
import com.example.socialmediaapi.web.dto.auth.JwtRequest;
import com.example.socialmediaapi.web.dto.auth.JwtResponse;
import com.example.socialmediaapi.web.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // injection
    }

    @Test
    void testLogin() {
        JwtRequest loginRequest = new JwtRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword("password");

        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setRoles(Set.of(Role.ROLE_USER));

        JwtResponse expectedResponse = new JwtResponse();
        expectedResponse.setId(1L);
        expectedResponse.setUsername("username");
        expectedResponse.setAccessToken("access_token");
        expectedResponse.setRefreshToken("refresh_token");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userService.getUserByUsername("username"))
                .thenReturn(user);
        when(jwtTokenProvider.createAccessToken(1L, "username", Set.of(Role.ROLE_USER)))
                .thenReturn("access_token");
        when(jwtTokenProvider.createRefreshToken(1L, "username"))
                .thenReturn("refresh_token");

        JwtResponse actualResponse = authService.login(loginRequest);

        assertEquals(expectedResponse.getId(), actualResponse.getId());
        assertEquals(expectedResponse.getUsername(), actualResponse.getUsername());
        assertEquals(expectedResponse.getAccessToken(), actualResponse.getAccessToken());
        assertEquals(expectedResponse.getRefreshToken(), actualResponse.getRefreshToken());

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService, times(1))
                .getUserByUsername("username");
        verify(jwtTokenProvider, times(1))
                .createAccessToken(1L, "username", Set.of(Role.ROLE_USER));
        verify(jwtTokenProvider, times(1))
                .createRefreshToken(1L, "username");
    }

    @Test
    void testRefresh() {
        String refreshToken = "refresh_token";

        JwtResponse expectedResponse = new JwtResponse();
        expectedResponse.setId(1L);
        expectedResponse.setUsername("username");
        expectedResponse.setAccessToken("access_token");
        expectedResponse.setRefreshToken("new_refresh_token");

        when(jwtTokenProvider.refreshUserTokens(refreshToken))
                .thenReturn(expectedResponse);

        JwtResponse actualResponse = authService.refresh(refreshToken);

        assertEquals(expectedResponse.getId(), actualResponse.getId());
        assertEquals(expectedResponse.getUsername(), actualResponse.getUsername());
        assertEquals(expectedResponse.getAccessToken(), actualResponse.getAccessToken());
        assertEquals(expectedResponse.getRefreshToken(), actualResponse.getRefreshToken());

        verify(jwtTokenProvider, times(1))
                .refreshUserTokens(refreshToken);
    }
}