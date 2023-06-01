package com.example.socialmediaapi.repository;

import com.example.socialmediaapi.domain.Message;
import com.example.socialmediaapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findMessagesBySender(User sender);
}
