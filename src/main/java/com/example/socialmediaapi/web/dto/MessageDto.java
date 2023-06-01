package com.example.socialmediaapi.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageDto {

    private Long id;

    @NotNull(message = "Text is required")
    private String text;
    private UserDto sender;
    private UserDto receiver;
}
