package com.example.Telemedi.repository;

import com.example.Telemedi.entity.ChatSession;
import com.example.Telemedi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, String> {

    List<ChatSession> findByUserOrderByCreatedAtDesc(User user);

    List<ChatSession> findByUserAndIsCompletedOrderByCreatedAtDesc(User user, Boolean isCompleted);

    Optional<ChatSession> findBySessionIdAndUser(String sessionId, User user);

    @Query("SELECT cs FROM ChatSession cs WHERE cs.user = :user AND cs.createdAt >= :since ORDER BY cs.createdAt DESC")
    List<ChatSession> findRecentSessionsByUser(@Param("user") User user, @Param("since") LocalDateTime since);

    @Query("SELECT COUNT(cs) FROM ChatSession cs WHERE cs.user = :user")
    long countSessionsByUser(@Param("user") User user);
}
