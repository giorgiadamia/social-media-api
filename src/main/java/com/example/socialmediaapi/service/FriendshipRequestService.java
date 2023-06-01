package com.example.socialmediaapi.service;

import com.example.socialmediaapi.domain.FriendshipRequest;
import com.example.socialmediaapi.domain.FriendshipStatus;
import com.example.socialmediaapi.domain.User;
import com.example.socialmediaapi.repository.FriendshipRequestRepository;
import com.example.socialmediaapi.repository.UserRepository;
import com.example.socialmediaapi.web.dto.UserDto;
import com.example.socialmediaapi.web.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendshipRequestService {

    private final FriendshipRequestRepository friendshipRequestRepository;
    private final UserRepository userRepository;

    private final UserService userService;
    private final UserMapper userMapper;

    @Transactional
    public Set<User> getFriends(Long id) {
        User userFromMemory = userService.getUserById(id);
        return userFromMemory.getFriends();
    }

    @Transactional
    public Set<UserDto> getFollowings(Long id) {
        User user = userService.getUserById(id);
        return user.getSentFriendshipRequests().stream()
                .map(request -> userMapper.toDto(request.getReceiver()))
                .collect(Collectors.toSet());
    }

    @Transactional
    public void sentFriendRequest(Long id, Long receiverId) {
        if (id.equals(receiverId)) {
            throw new IllegalArgumentException("It's a same user");
        }

        User sender = userService.getUserById(id);
        User receiver = userService.getUserById(receiverId);

        Optional<FriendshipRequest> isAlreadyFollowing = sender.getSentFriendshipRequests().stream()
                .filter(s -> s.getReceiver().equals(receiver))
                .findFirst();

        if (isAlreadyFollowing.isPresent()) {
            throw new IllegalArgumentException("This user is already following");
        }

        FriendshipRequest friendshipRequest = new FriendshipRequest();
        friendshipRequest.setReceiver(receiver);
        friendshipRequest.setSender(sender);
        friendshipRequest.setStatus(FriendshipStatus.PENDING);

        friendshipRequestRepository.save(friendshipRequest);
    }

    @Transactional
    public void acceptRequest(Long id, Long senderId) {
        if (id.equals(senderId)) {
            throw new IllegalArgumentException("It's a same user");
        }

        User sender = userService.getUserById(senderId);
        User receiver = userService.getUserById(id);

        Optional<FriendshipRequest> friendshipRequestOptional = friendshipRequestRepository
                .findFriendshipRequestBySenderAndReceiver(sender, receiver);

        if (friendshipRequestOptional.isEmpty()) {
            throw new IllegalCallerException("It is not user's follower");
        }

        FriendshipRequest friendshipRequest = friendshipRequestOptional.get();
        friendshipRequest.setStatus(FriendshipStatus.ACCEPTED);
        friendshipRequest.setReceiver(receiver);
        friendshipRequest.setSender(sender);

        sender.getFriends().add(receiver);
        receiver.getFriends().add(sender);

        friendshipRequestRepository.save(friendshipRequest);
        userRepository.save(sender);
        userRepository.save(receiver);
    }

    @Transactional
    public void rejectRequest(Long id, Long senderId) {
        if (id.equals(senderId)) {
            throw new IllegalArgumentException("It's a same user");
        }

        User sender = userService.getUserById(senderId);
        User receiver = userService.getUserById(id);

        Optional<FriendshipRequest> friendshipRequestOptional = friendshipRequestRepository
                .findFriendshipRequestBySenderAndReceiver(sender, receiver);

        if (friendshipRequestOptional.isEmpty()) {
            throw new IllegalCallerException("It is not user's follower");
        }

        FriendshipRequest friendshipRequest = friendshipRequestOptional.get();
        friendshipRequest.setStatus(FriendshipStatus.DENIED);

        friendshipRequestRepository.save(friendshipRequest);
    }

    @Transactional
    public void deleteFriend(Long id, Long friendId) {
        if (id.equals(friendId)) {
            throw new IllegalArgumentException("It's a same user");
        }

        User user = userService.getUserById(id);
        User friend = userService.getUserById(friendId);

        if (!user.getFriends().contains(friend)) {
            throw new IllegalArgumentException("These users are not friends");
        }

        user.getFriends().remove(friend);
        friend.getFriends().remove(user);

        Optional<FriendshipRequest> friendshipRequestOptional = friendshipRequestRepository
                .findFriendshipRequestBySenderAndReceiver(friend, user);

        if (friendshipRequestOptional.isEmpty()) {
            throw new IllegalCallerException("It is not user's follower");
        }
        FriendshipRequest friendshipRequest = friendshipRequestOptional.get();
        friendshipRequest.setStatus(FriendshipStatus.DENIED);

        friendshipRequestRepository.save(friendshipRequest);
        userRepository.save(user);
        userRepository.save(friend);
    }
}
