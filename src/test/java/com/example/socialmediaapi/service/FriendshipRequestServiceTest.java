package com.example.socialmediaapi.service;

import com.example.socialmediaapi.domain.FriendshipRequest;
import com.example.socialmediaapi.domain.FriendshipStatus;
import com.example.socialmediaapi.domain.User;
import com.example.socialmediaapi.repository.FriendshipRequestRepository;
import com.example.socialmediaapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FriendshipRequestServiceTest {

    @Mock
    private FriendshipRequestRepository friendshipRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private FriendshipRequestService friendshipRequestService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // injection
    }

    @Test
    public void testGetFriends() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        List<User> friends = new ArrayList<>();
        friends.add(new User());
        friends.add(new User());
        user.setFriends(friends);

        when(userService.getUserById(userId)).thenReturn(user);

        List<User> result = friendshipRequestService.getFriends(userId);

        assertEquals(friends.size(), result.size());
    }

    @Test
    public void testGetFollowings() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        List<FriendshipRequest> requests = new ArrayList<>();
        requests.add(new FriendshipRequest());
        requests.add(new FriendshipRequest());
        user.setSentFriendshipRequests(requests);

        when(userService.getUserById(userId)).thenReturn(user);

        List<User> result = friendshipRequestService.getFollowings(userId);

        assertEquals(requests.size(), result.size());
    }

    @Test
    public void testSendFriendRequest() {
        Long senderId = 1L;
        Long receiverId = 2L;

        User sender = new User();
        User receiver = new User();

        when(userService.getUserById(senderId)).thenReturn(sender);
        when(userService.getUserById(receiverId)).thenReturn(receiver);

        friendshipRequestService.sendFriendRequest(senderId, receiverId);

        verify(friendshipRequestRepository, times(1)).save(any(FriendshipRequest.class));
    }

    @Test
    public void testSendFriendRequestSameUser() {
        Long userId = 1L;

        assertThrows(IllegalArgumentException.class, () -> friendshipRequestService.sendFriendRequest(userId, userId));

        verify(friendshipRequestRepository, never()).save(any(FriendshipRequest.class));
    }

    @Test
    public void testSendFriendRequestAlreadyFollowing() {
        // Arrange
        Long senderId = 1L;
        Long receiverId = 2L;

        User sender = new User();
        sender.setId(senderId);

        User receiver = new User();
        receiver.setId(receiverId);

        FriendshipRequest friendshipRequest = new FriendshipRequest(1L, sender, receiver, FriendshipStatus.PENDING);
        sender.getSentFriendshipRequests().add(friendshipRequest);

        when(userService.getUserById(senderId)).thenReturn(sender);
        when(userService.getUserById(receiverId)).thenReturn(receiver);

        assertThrows(IllegalArgumentException.class, () -> friendshipRequestService.sendFriendRequest(senderId, receiverId));

        verify(friendshipRequestRepository, never()).save(any(FriendshipRequest.class));
    }

    @Test
    public void testAcceptRequest() {
        Long userId = 1L;
        Long senderId = 2L;

        User user = new User();
        User sender = new User();
        FriendshipRequest friendshipRequest = new FriendshipRequest(1L, sender, user, FriendshipStatus.PENDING);

        when(userService.getUserById(userId)).thenReturn(user);
        when(userService.getUserById(senderId)).thenReturn(sender);
        when(friendshipRequestRepository.findFriendshipRequestBySenderAndReceiver(sender, user))
                .thenReturn(Optional.of(friendshipRequest));

        friendshipRequestService.acceptRequest(userId, senderId);

        assertEquals(FriendshipStatus.ACCEPTED, friendshipRequest.getStatus());
        assertEquals(user, friendshipRequest.getReceiver());
        assertEquals(sender, friendshipRequest.getSender());

        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).save(sender);
        verify(friendshipRequestRepository, times(1)).save(friendshipRequest);

        assertTrue(user.getFriends().contains(sender));
        assertTrue(sender.getFriends().contains(user));
    }

    @Test
    public void testAcceptRequestSameUser() {
        Long userId = 1L;

        assertThrows(IllegalArgumentException.class, () -> friendshipRequestService.acceptRequest(userId, userId));

        verify(friendshipRequestRepository, never()).save(any(FriendshipRequest.class));
    }

    @Test
    public void testAcceptRequestNotFollowing() {
        Long userId = 1L;
        Long senderId = 2L;

        User user = new User();
        User sender = new User();

        when(userService.getUserById(userId)).thenReturn(user);
        when(userService.getUserById(senderId)).thenReturn(sender);
        when(friendshipRequestRepository.findFriendshipRequestBySenderAndReceiver(sender, user))
                .thenReturn(Optional.empty());

        assertThrows(IllegalCallerException.class, () -> friendshipRequestService.acceptRequest(userId, senderId));

        verify(friendshipRequestRepository, never()).save(any(FriendshipRequest.class));
    }

    @Test
    public void testRejectRequest() {
        Long userId = 1L;
        Long senderId = 2L;

        User user = new User();
        User sender = new User();
        FriendshipRequest friendshipRequest = new FriendshipRequest(1L, sender, user, FriendshipStatus.PENDING);

        when(userService.getUserById(userId)).thenReturn(user);
        when(userService.getUserById(senderId)).thenReturn(sender);
        when(friendshipRequestRepository.findFriendshipRequestBySenderAndReceiver(sender, user))
                .thenReturn(Optional.of(friendshipRequest));

        friendshipRequestService.rejectRequest(userId, senderId);

        assertEquals(FriendshipStatus.DENIED, friendshipRequest.getStatus());

        verify(friendshipRequestRepository, times(1)).save(friendshipRequest);
    }

    @Test
    public void testRejectRequestSameUser() {
        Long userId = 1L;

        assertThrows(IllegalArgumentException.class, () -> friendshipRequestService.rejectRequest(userId, userId));

        verify(friendshipRequestRepository, never()).save(any(FriendshipRequest.class));
    }

    @Test
    public void testRejectRequestNotFollowing() {
        Long userId = 1L;
        Long senderId = 2L;

        User user = new User();
        User sender = new User();

        when(userService.getUserById(userId)).thenReturn(user);
        when(userService.getUserById(senderId)).thenReturn(sender);
        when(friendshipRequestRepository.findFriendshipRequestBySenderAndReceiver(sender, user))
                .thenReturn(Optional.empty());

        assertThrows(IllegalCallerException.class, () -> friendshipRequestService.rejectRequest(userId, senderId));

        verify(friendshipRequestRepository, never()).save(any(FriendshipRequest.class));
    }

    @Test
    public void testDeleteFriend() {
        Long userId = 1L;
        Long friendId = 2L;

        User user = new User();
        User friend = new User();
        FriendshipRequest friendshipRequest = new FriendshipRequest(1L, user, friend, FriendshipStatus.ACCEPTED);
        user.getFriends().add(friend);
        friend.getFriends().add(user);

        when(userService.getUserById(userId)).thenReturn(user);
        when(userService.getUserById(friendId)).thenReturn(friend);
        when(friendshipRequestRepository.findFriendshipRequestBySenderAndReceiver(friend, user))
                .thenReturn(Optional.of(friendshipRequest));
        when(friendshipRequestRepository.findFriendshipRequestBySenderAndReceiver(user, friend))
                .thenReturn(Optional.of(friendshipRequest));

        friendshipRequestService.deleteFriend(userId, friendId);

        assertFalse(user.getFriends().contains(friend));
        assertFalse(friend.getFriends().contains(user));

        verify(friendshipRequestRepository, times(1)).delete(friendshipRequest);
        verify(friendshipRequestRepository, times(1)).save(friendshipRequest);
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).save(friend);
    }

    @Test
    public void testDeleteFriendSameUser() {
        Long userId = 1L;

        assertThrows(IllegalArgumentException.class, () -> friendshipRequestService.deleteFriend(userId, userId));

        verify(friendshipRequestRepository, never()).delete(any(FriendshipRequest.class));
        verify(friendshipRequestRepository, never()).save(any(FriendshipRequest.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testDeleteFriendNotFriends() {
        Long userId = 1L;
        Long friendId = 2L;

        User user = new User();
        User friend = new User();

        when(userService.getUserById(userId)).thenReturn(user);
        when(userService.getUserById(friendId)).thenReturn(friend);
        when(friendshipRequestRepository.findFriendshipRequestBySenderAndReceiver(friend, user))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> friendshipRequestService.deleteFriend(userId, friendId));

        verify(friendshipRequestRepository, never()).delete(any(FriendshipRequest.class));
        verify(friendshipRequestRepository, never()).save(any(FriendshipRequest.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testUnfollow() {
        Long userId = 1L;
        Long receiverId = 2L;

        User user = new User();
        User receiver = new User();
        FriendshipRequest friendshipRequest = new FriendshipRequest(1L, user, receiver, FriendshipStatus.ACCEPTED);

        when(userService.getUserById(userId)).thenReturn(user);
        when(userService.getUserById(receiverId)).thenReturn(receiver);
        when(friendshipRequestRepository.findFriendshipRequestBySenderAndReceiver(user, receiver))
                .thenReturn(Optional.of(friendshipRequest));

        friendshipRequestService.unfollow(userId, receiverId);

        verify(friendshipRequestRepository, times(1)).delete(friendshipRequest);
    }

    @Test
    public void testUnfollowSameUser() {
        Long userId = 1L;

        assertThrows(IllegalStateException.class, () -> friendshipRequestService.unfollow(userId, userId));

        verify(friendshipRequestRepository, never()).delete(any(FriendshipRequest.class));
    }

    @Test
    public void testUnfollowNotFollowing() {
        Long userId = 1L;
        Long receiverId = 2L;

        User user = new User();
        User receiver = new User();

        when(userService.getUserById(userId)).thenReturn(user);
        when(userService.getUserById(receiverId)).thenReturn(receiver);
        when(friendshipRequestRepository.findFriendshipRequestBySenderAndReceiver(user, receiver))
                .thenReturn(Optional.empty());

        assertThrows(IllegalCallerException.class, () -> friendshipRequestService.unfollow(userId, receiverId));

        verify(friendshipRequestRepository, never()).delete(any(FriendshipRequest.class));
    }
}