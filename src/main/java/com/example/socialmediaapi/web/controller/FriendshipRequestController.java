package com.example.socialmediaapi.web.controller;

import com.example.socialmediaapi.domain.User;
import com.example.socialmediaapi.service.FriendshipRequestService;
import com.example.socialmediaapi.web.dto.UserDto;
import com.example.socialmediaapi.web.jwt.JwtEntity;
import com.example.socialmediaapi.web.mappers.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
@Tag(name = "Friendship Controller", description = "friendship API")
public class FriendshipRequestController {

    private final FriendshipRequestService friendshipRequestService;

    private final UserMapper userMapper;

    @GetMapping
    @Operation(summary = "Get user's friends")
    public List<UserDto> getFriends(@AuthenticationPrincipal JwtEntity user) {
        List<User> friends = friendshipRequestService.getFriends(user.getId());
        return userMapper.toDto(friends);
    }

    @GetMapping("/followings")
    @Operation(summary = "Get user's followings")
    public List<UserDto> getFollowings(@AuthenticationPrincipal JwtEntity user) {

        List<User> followings = friendshipRequestService.getFollowings(user.getId());
        return userMapper.toDto(followings);
    }

    @PostMapping("/{receiverId}")
    @Operation(summary = "Send a friend request")
    public void sendFriendRequest(@AuthenticationPrincipal JwtEntity user,
                                  @PathVariable Long receiverId) {
        friendshipRequestService.sendFriendRequest(user.getId(), receiverId);
    }

    @PostMapping("/{senderId}/accept")
    @Operation(summary = "Accept a friend request")
    public void acceptRequest(@AuthenticationPrincipal JwtEntity user,
                              @PathVariable Long senderId) {
        friendshipRequestService.acceptRequest(user.getId(), senderId);
    }

    @PostMapping("/{senderId}/reject")
    @Operation(summary = "Reject a friend request")
    public void rejectRequest(@AuthenticationPrincipal JwtEntity user,
                              @PathVariable Long senderId) {
        friendshipRequestService.rejectRequest(user.getId(), senderId);
    }

    @DeleteMapping("/{friendId}")
    @Operation(summary = "Delete a friend")
    public void deleteFriend(@AuthenticationPrincipal JwtEntity user,
                             @PathVariable Long friendId) {
        friendshipRequestService.deleteFriend(user.getId(), friendId);
    }

    @DeleteMapping("/unfollow/{receiverId}")
    @Operation(summary = "Unfollow from a user")
    public void unfollow(@AuthenticationPrincipal JwtEntity user,
                         @PathVariable Long receiverId) {
        friendshipRequestService.unfollow(user.getId(), receiverId);
    }
}
