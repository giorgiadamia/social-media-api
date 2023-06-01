package com.example.socialmediaapi.web.controller;

import com.example.socialmediaapi.domain.User;
import com.example.socialmediaapi.service.FriendshipRequestService;
import com.example.socialmediaapi.web.dto.UserDto;
import com.example.socialmediaapi.web.jwt.JwtEntity;
import com.example.socialmediaapi.web.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendshipRequestController {

    private final FriendshipRequestService friendshipRequestService;

    private final UserMapper userMapper;

    @GetMapping
    public List<User> getFriends(@AuthenticationPrincipal JwtEntity user) {
        return friendshipRequestService.getFriends(user.getId());
    }

    @GetMapping("/followings")
    public List<UserDto> getFollowings(@AuthenticationPrincipal JwtEntity user) {

        List<User> followings = friendshipRequestService.getFollowings(user.getId());
        return userMapper.toDto(followings);
    }

    @PostMapping("/{receiverId}")
    public void sentFriendRequest(@AuthenticationPrincipal JwtEntity user,
                                  @PathVariable Long receiverId) {
        friendshipRequestService.sentFriendRequest(user.getId(), receiverId);
    }

    @PostMapping("/{senderId}/accept")
    public void acceptRequest(@AuthenticationPrincipal JwtEntity user,
                              @PathVariable Long senderId) {
        friendshipRequestService.acceptRequest(user.getId(), senderId);
    }

    @PostMapping("/{senderId}/reject")
    public void rejectRequest(@AuthenticationPrincipal JwtEntity user,
                              @PathVariable Long senderId) {
        friendshipRequestService.rejectRequest(user.getId(), senderId);
    }

    @DeleteMapping("/{friendId}")
    public void deleteFriend(@AuthenticationPrincipal JwtEntity user,
                             @PathVariable Long friendId) {
        friendshipRequestService.deleteFriend(user.getId(), friendId);
    }

    @DeleteMapping("/unfollow/{receiverId}")
    public void unfollow(@AuthenticationPrincipal JwtEntity user,
                         @PathVariable Long receiverId) {
        friendshipRequestService.unfollow(user.getId(), receiverId);
    }
}
