package com.example.Telemedi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    @JsonIgnore
    private ChatSession chatSession;

    @Enumerated(EnumType.STRING)
    @Column(name = "sender_type", nullable = false)
    private SenderType senderType;

    @Column(name = "message_text", columnDefinition = "TEXT", nullable = false)
    private String messageText;

    @Column(name = "remedy_text", columnDefinition = "TEXT")
    private String remedyText;

    @Column(name = "suggestions", columnDefinition = "JSON")
    private String suggestions; // JSON string

    @CreationTimestamp
    @Column(name = "timestamp", updatable = false)
    private LocalDateTime timestamp;

    @Column(name = "is_system_message")
    private Boolean isSystemMessage = false;

    // Constructors
    public ChatMessage() {}

    public ChatMessage(ChatSession chatSession, SenderType senderType, String messageText) {
        this.chatSession = chatSession;
        this.senderType = senderType;
        this.messageText = messageText;
    }

    // Getters and Setters
    public Long getMessageId() { return messageId; }
    public void setMessageId(Long messageId) { this.messageId = messageId; }

    public ChatSession getChatSession() { return chatSession; }
    public void setChatSession(ChatSession chatSession) { this.chatSession = chatSession; }

    public SenderType getSenderType() { return senderType; }
    public void setSenderType(SenderType senderType) { this.senderType = senderType; }

    public String getMessageText() { return messageText; }
    public void setMessageText(String messageText) { this.messageText = messageText; }

    public String getRemedyText() { return remedyText; }
    public void setRemedyText(String remedyText) { this.remedyText = remedyText; }

    public String getSuggestions() { return suggestions; }
    public void setSuggestions(String suggestions) { this.suggestions = suggestions; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Boolean getIsSystemMessage() { return isSystemMessage; }
    public void setIsSystemMessage(Boolean isSystemMessage) { this.isSystemMessage = isSystemMessage; }

    public enum SenderType {
        user, bot
    }
}
