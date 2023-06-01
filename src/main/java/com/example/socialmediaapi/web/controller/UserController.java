package com.example.socialmediaapi.web.controller;

import com.example.socialmediaapi.domain.Post;
import com.example.socialmediaapi.domain.User;
import com.example.socialmediaapi.service.PostService;
import com.example.socialmediaapi.service.UserService;
import com.example.socialmediaapi.web.dto.PostDto;
import com.example.socialmediaapi.web.dto.UserDto;
import com.example.socialmediaapi.web.jwt.JwtEntity;
import com.example.socialmediaapi.web.mappers.PostMapper;
import com.example.socialmediaapi.web.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final PostService postService;

    private final UserMapper userMapper;
    private final PostMapper postMapper;

    @PutMapping
    public void updateUser(@AuthenticationPrincipal JwtEntity user,
                           @Validated @RequestBody UserDto userDto) {
        userService.updateUser(user.getId(), userDto);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return userMapper.toDto(user);
    }

    @DeleteMapping
    public void deleteUser(@AuthenticationPrincipal JwtEntity user) {
        userService.deleteUser(user.getId());
    }

    @GetMapping("/{id}/posts")
    public List<PostDto> getPostsByUserId(@PathVariable Long id) {
        List<Post> posts = postService.getAllPostsByUserId(id);
        return postMapper.toDto(posts);
    }

    @PostMapping("/posts")
    public void createPost(@AuthenticationPrincipal JwtEntity user,
                           @Validated @RequestBody PostDto postDto) {
        Post post = postMapper.toEntity(postDto);
        postService.createPost(post, user.getId());
    }
}
