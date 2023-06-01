package com.example.socialmediaapi.service;

import com.example.socialmediaapi.domain.Post;
import com.example.socialmediaapi.domain.User;
import com.example.socialmediaapi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final UserService userService;
    private final FriendshipRequestService friendshipRequestService;

    private final PostRepository postRepository;

    public List<Post> getFeed(Long id, int page, int size, Sort.Direction direction) {
        User user = userService.getUserById(id);

        List<User> followings = friendshipRequestService.getFollowings(id);
        followings.add(user);

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "time"));
        return postRepository.findByUserInOrderByTimeDesc(followings, pageable);
    }
}
