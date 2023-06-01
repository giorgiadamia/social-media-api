package com.example.socialmediaapi.web.controller;

import com.example.socialmediaapi.domain.Message;
import com.example.socialmediaapi.service.MessageService;
import com.example.socialmediaapi.web.dto.MessageDto;
import com.example.socialmediaapi.web.jwt.JwtEntity;
import com.example.socialmediaapi.web.mappers.MessageMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final MessageMapper messageMapper;

    @PostMapping("/{receiverId}")
    public void sentMessage(@AuthenticationPrincipal JwtEntity user,
                            @PathVariable Long receiverId,
                            @Valid @RequestBody MessageDto messageDto) {
        messageService.sentMessage(user.getId(), receiverId, messageDto);
    }

    @GetMapping
    public List<MessageDto> getMessages(@AuthenticationPrincipal JwtEntity user) {
        List<Message> messages = messageService.getMessages(user.getId());
        return messageMapper.toDto(messages);
    }
}
