package com.example.socialmediaapi.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostDto {

    private Long id;

    @NotNull(message = "Title is required")
    @Size(max = 255, message = "Title must be up to 255 characters")
    private String title;

    @NotNull(message = "Text is required")
    private String text;

    private String image;
}
