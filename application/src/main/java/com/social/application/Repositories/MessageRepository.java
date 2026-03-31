package com.social.application.Repositories;
import com.social.application.Models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>{
    List<Message> findBySender_IdAndReceiver_IdOrderByCreatedAtAsc(long senderId, long receiverId);
}
