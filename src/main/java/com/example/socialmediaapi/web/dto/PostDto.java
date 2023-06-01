package com.example.socialmediaapi.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Post DTO")
public class PostDto {

    @Schema(description = "Post id")
    private Long id;

    @Schema(description = "Post title", example = "How I Met Your Mother")
    @NotNull(message = "Title is required")
    @Size(max = 255, message = "Title must be up to 255 characters")
    private String title;

    @Schema(description = "Post text", example = "How I Met Your Mother (often abbreviated as HIMYM) is an American sitcom, created by Craig Thomas and Carter Bays for CBS. The series...")
    @NotNull(message = "Text is required")
    private String text;

    @Schema(description = "Post image")
    private String image;

    @Schema(description = "Post user")
    private UserDto user;
}
