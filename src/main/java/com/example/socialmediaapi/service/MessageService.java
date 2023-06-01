package com.example.socialmediaapi.service;

import com.example.socialmediaapi.domain.Message;
import com.example.socialmediaapi.domain.User;
import com.example.socialmediaapi.repository.MessageRepository;
import com.example.socialmediaapi.web.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    private final UserService userService;

    @Transactional
    public void sentMessage(Long id, Long receiverId, MessageDto messageDto) {
        if (id.equals(receiverId)) {
            throw new IllegalArgumentException("It's the same user");
        }

        User sender = userService.getUserById(id);
        User receiver = userService.getUserById(receiverId);

        if (!sender.getFriends().contains(receiver)) {
            throw new IllegalCallerException("These users are not friends");
        }

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setText(messageDto.getText());

        messageRepository.save(message);
    }

    public List<Message> getMessages(Long id) {
        User sender = userService.getUserById(id);
        return messageRepository.findMessagesBySender(sender);
    }
}
