package com.example.repository;

import com.example.entity.Message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    // Add any custom queries if needed (e.g., finding by postedBy)
    List<Message> findByPostedBy(Integer postedBy);
}