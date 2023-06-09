package com.example.socialmediaapi.repository;

import com.example.socialmediaapi.domain.Post;
import com.example.socialmediaapi.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByUserId(Long id);

    List<Post> findByUserInOrderByTimeDesc(List<User> users, Pageable pageable);
}
