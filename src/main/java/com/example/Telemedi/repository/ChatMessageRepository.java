package com.example.Telemedi.repository;

import com.example.Telemedi.entity.ChatMessage;
import com.example.Telemedi.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByChatSessionOrderByTimestampAsc(ChatSession chatSession);

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatSession.sessionId = :sessionId ORDER BY cm.timestamp ASC")
    List<ChatMessage> findMessagesBySessionId(@Param("sessionId") String sessionId);

    @Query("SELECT COUNT(cm) FROM ChatMessage cm WHERE cm.chatSession = :session")
    long countMessagesBySession(@Param("session") ChatSession session);
}
