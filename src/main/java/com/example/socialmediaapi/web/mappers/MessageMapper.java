package com.example.socialmediaapi.web.mappers;

import com.example.socialmediaapi.domain.Message;
import com.example.socialmediaapi.web.dto.MessageDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    List<MessageDto> toDto(List<Message> messages);
}
