package com.example.socialmediaapi.web.mappers;

import com.example.socialmediaapi.domain.User;
import com.example.socialmediaapi.web.dto.UserDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    List<UserDto> toDto(List<User> users);

    User toEntity(UserDto userDto);
}
