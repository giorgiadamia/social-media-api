package com.example.socialmediaapi.web.controller;

import com.example.socialmediaapi.domain.Post;
import com.example.socialmediaapi.service.PostService;
import com.example.socialmediaapi.web.dto.PostDto;
import com.example.socialmediaapi.web.jwt.JwtEntity;
import com.example.socialmediaapi.web.mappers.PostMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Validated
public class PostController {

    private final PostService postService;

    private final PostMapper postMapper;

    @GetMapping("/{id}")
    public PostDto getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        return postMapper.toDto(post);
    }

    @PutMapping("/{postId}")
    public void updatePost(@AuthenticationPrincipal JwtEntity user,
                           @PathVariable Long postId,
                           @Valid @RequestBody PostDto postDto) {
        postService.updatePost(user.getId(), postId, postDto);
    }

    @DeleteMapping("/{id}")
    public void deletePostById(@AuthenticationPrincipal JwtEntity user,
                               @PathVariable Long id) {
        postService.deletePost(user.getId(), id);
    }
}
