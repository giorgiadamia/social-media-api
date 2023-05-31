package com.example.socialmediaapi.web.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JwtRequest {

    @NotNull(message = "Username should be not null")
    private String username;

    @NotNull(message = "Password should be not null")
    private String password;
}
