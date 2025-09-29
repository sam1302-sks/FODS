package com.example.Telemedi.service;

import com.example.Telemedi.entity.*;
import com.example.Telemedi.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ChatHistoryService {

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    public ChatSession createChatSession(String sessionId, User user) {
        ChatSession session = new ChatSession(sessionId, user);
        return chatSessionRepository.save(session);
    }

    public Optional<ChatSession> findChatSession(String sessionId) {
        return chatSessionRepository.findById(sessionId);
    }

    public ChatMessage saveMessage(ChatSession session, ChatMessage.SenderType senderType,
                                   String messageText, String remedy, List<String> suggestions) {
        ChatMessage message = new ChatMessage(session, senderType, messageText);
        message.setRemedyText(remedy);

        if (suggestions != null && !suggestions.isEmpty()) {
            try {
                message.setSuggestions(objectMapper.writeValueAsString(suggestions));
            } catch (JsonProcessingException e) {
                // Handle JSON serialization error
                message.setSuggestions(null);
            }
        }

        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getChatHistory(String sessionId) {
        return chatMessageRepository.findMessagesBySessionId(sessionId);
    }

    public List<ChatSession> getUserChatSessions(User user) {
        return chatSessionRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<ChatSession> getRecentUserSessions(User user, int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return chatSessionRepository.findRecentSessionsByUser(user, since);
    }

    public void completeChatSession(String sessionId, String diagnosis) {
        Optional<ChatSession> sessionOpt = chatSessionRepository.findById(sessionId);
        if (sessionOpt.isPresent()) {
            ChatSession session = sessionOpt.get();
            session.setIsCompleted(true);
            session.setDiagnosis(diagnosis);
            chatSessionRepository.save(session);
        }
    }

    public MedicalHistory createMedicalHistory(User user, ChatSession session,
                                               List<String> symptoms, String diagnosis,
                                               String treatment, MedicalHistory.SeverityLevel severity) {
        MedicalHistory history = new MedicalHistory(user, session, "");

        try {
            history.setSymptoms(objectMapper.writeValueAsString(symptoms));
        } catch (JsonProcessingException e) {
            history.setSymptoms("[]");
        }

        history.setDiagnosis(diagnosis);
        history.setRecommendedTreatment(treatment);
        history.setSeverityLevel(severity);

        return medicalHistoryRepository.save(history);
    }

    public List<MedicalHistory> getUserMedicalHistory(User user) {
        return medicalHistoryRepository.findByUserOrderByConsultationDateDesc(user);
    }

    public long getUserTotalSessions(User user) {
        return chatSessionRepository.countSessionsByUser(user);
    }

    public long getUserTotalMessages(ChatSession session) {
        return chatMessageRepository.countMessagesBySession(session);
    }
}
