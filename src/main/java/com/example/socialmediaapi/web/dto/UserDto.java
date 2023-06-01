package com.example.socialmediaapi.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {

    private Long id;

    @NotNull(message = "Username must be not null.")
    @NotBlank(message = "Username cant be blank")
    @Size(max = 255, message = "Username length must be smaller than 255 symbols.")
    private String username;

    @NotNull(message = "Email must be not null")
    @NotBlank(message = "Email cant be blank")
    @Size(max = 255, message = "Email length must be smaller than 255 symbols.")
    @Email(message = "Invalid email format")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password must be not null.")
    @NotBlank(message = "Password cant be blank")
    private String password;
}
