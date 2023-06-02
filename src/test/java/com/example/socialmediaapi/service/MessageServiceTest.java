package com.example.socialmediaapi.service;

import com.example.socialmediaapi.domain.Message;
import com.example.socialmediaapi.domain.User;
import com.example.socialmediaapi.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // injection
    }

    @Test
    public void testSendMessage() {
        Long senderId = 1L;
        Long receiverId = 2L;
        String text = "Hello, receiver!";

        User sender = new User();
        sender.setId(senderId);

        User receiver = new User();
        receiver.setId(receiverId);

        receiver.getFriends().add(sender);
        sender.getFriends().add(receiver);

        when(userService.getUserById(senderId)).thenReturn(sender);
        when(userService.getUserById(receiverId)).thenReturn(receiver);

        messageService.sendMessage(senderId, receiverId, text);

        verify(userService, times(1)).getUserById(senderId);
        verify(userService, times(1)).getUserById(receiverId);
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    public void testSendMessageSameUser() {
        Long id = 1L;
        Long receiverId = 1L;
        String text = "Hello";

        assertThrows(IllegalArgumentException.class, () -> messageService.sendMessage(id, receiverId, text));

        verify(userService, never()).getUserById(anyLong());
        verify(messageRepository, never()).save(any(Message.class));
    }


    @Test
    public void testSendMessageNotFriends() {
        Long senderId = 1L;
        Long receiverId = 2L;
        String text = "Hello, receiver!";

        User sender = new User();
        sender.setId(senderId);

        User receiver = new User();
        receiver.setId(receiverId);

        when(userService.getUserById(senderId)).thenReturn(sender);
        when(userService.getUserById(receiverId)).thenReturn(receiver);

        assertThrows(IllegalCallerException.class, () -> messageService.sendMessage(senderId, receiverId, text));

        verify(messageRepository, never()).save(any(Message.class));
    }

    @Test
    public void testGetMessages() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        List<Message> messages = new ArrayList<>();
        messages.add(new Message());
        messages.add(new Message());

        when(userService.getUserById(userId)).thenReturn(user);
        when(messageRepository.findMessagesBySender(user)).thenReturn(messages);

        List<Message> result = messageService.getMessages(userId);

        assertEquals(messages.size(), result.size());
        verify(messageRepository, times(1)).findMessagesBySender(user);
    }
}