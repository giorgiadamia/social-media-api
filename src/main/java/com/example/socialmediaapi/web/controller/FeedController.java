package com.example.socialmediaapi.web.controller;

import com.example.socialmediaapi.domain.Post;
import com.example.socialmediaapi.service.FeedService;
import com.example.socialmediaapi.web.dto.PostDto;
import com.example.socialmediaapi.web.jwt.JwtEntity;
import com.example.socialmediaapi.web.mappers.PostMapper;
import com.example.socialmediaapi.web.mappers.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
@Tag(name = "Feed Controller", description = "feed API")
public class FeedController {

    private final FeedService feedService;

    private final PostMapper postMapper;
    private final UserMapper userMapper;

    @GetMapping
    @Operation(summary = "Get feed")
    public List<PostDto> getFeed(
            @AuthenticationPrincipal JwtEntity user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        List<Post> posts = feedService.getFeed(user.getId(), page, size, direction);
        List<PostDto> postDtos = new ArrayList<>();

        for (Post post: posts) {
            PostDto postDto = postMapper.toDto(post);
            postDto.setUser(userMapper.toDto(post.getUser()));
            postDtos.add(postDto);
        }
        return postDtos;
    }
}
