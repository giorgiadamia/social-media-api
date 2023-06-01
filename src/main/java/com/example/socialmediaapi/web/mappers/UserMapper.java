package com.example.socialmediaapi.web.mappers;

import com.example.socialmediaapi.domain.User;
import com.example.socialmediaapi.web.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto userDto);
}
