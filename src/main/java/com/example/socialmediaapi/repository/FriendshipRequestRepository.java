package com.example.socialmediaapi.repository;

import com.example.socialmediaapi.domain.FriendshipRequest;
import com.example.socialmediaapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendshipRequestRepository extends JpaRepository<FriendshipRequest, Long> {

    Optional<FriendshipRequest> findFriendshipRequestBySenderAndReceiver(User sender, User receiver);
}
