package com.example.socialmediaapi.web.controller;

import com.example.socialmediaapi.domain.User;
import com.example.socialmediaapi.service.FriendshipRequestService;
import com.example.socialmediaapi.web.dto.user.UserDto;
import com.example.socialmediaapi.web.jwt.JwtEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendshipRequestController {

    private final FriendshipRequestService friendshipRequestService;

    @GetMapping
    public Set<User> getFriends(@AuthenticationPrincipal JwtEntity user) {
        return friendshipRequestService.getFriends(user.getId());
    }

    @GetMapping("/followings")
    public Set<UserDto> getFollowings(@AuthenticationPrincipal JwtEntity user) {
        return friendshipRequestService.getFollowings(user.getId());
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
}
