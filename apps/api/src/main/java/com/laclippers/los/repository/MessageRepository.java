package com.laclippers.los.repository;

import com.laclippers.los.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderIdOrReceiverIdOrderByCreatedAtAsc(Long senderId, Long receiverId);

    List<Message> findBySenderIdAndReceiverIdOrderByCreatedAtAsc(Long senderId, Long receiverId);
}