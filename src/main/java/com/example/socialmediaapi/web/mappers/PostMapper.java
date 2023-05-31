package com.example.socialmediaapi.web.mappers;

import com.example.socialmediaapi.domain.Post;
import com.example.socialmediaapi.web.dto.post.PostDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    PostDto toDto(Post post);

    List<PostDto> toDto(List<Post> posts);

    Post toEntity(PostDto postDto);
}
