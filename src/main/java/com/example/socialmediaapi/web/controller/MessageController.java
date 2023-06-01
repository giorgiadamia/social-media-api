package com.example.socialmediaapi.web.controller;

import com.example.socialmediaapi.domain.Message;
import com.example.socialmediaapi.service.MessageService;
import com.example.socialmediaapi.web.dto.MessageDto;
import com.example.socialmediaapi.web.jwt.JwtEntity;
import com.example.socialmediaapi.web.mappers.MessageMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
@Tag(name = "Message Controller", description = "message API")
public class MessageController {

    private final MessageService messageService;
    private final MessageMapper messageMapper;

    @PostMapping("/{receiverId}")
    @Operation(summary = "Send a message")
    public void sendMessage(@AuthenticationPrincipal JwtEntity user,
                            @PathVariable Long receiverId,
                            @NotNull(message = "Text is required") @RequestParam String text) {
        messageService.sendMessage(user.getId(), receiverId, text);
    }

    @GetMapping
    @Operation(summary = "Get all messages that user have sent")
    public List<MessageDto> getMessages(@AuthenticationPrincipal JwtEntity user) {
        List<Message> messages = messageService.getMessages(user.getId());
        return messageMapper.toDto(messages);
    }
}
