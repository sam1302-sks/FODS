package com.example.Telemedi.service;

import com.example.Telemedi.dto.QueryRequest;
import com.example.Telemedi.dto.BotResponse;
import com.example.Telemedi.entity.*;
import com.example.Telemedi.model.ConversationalTrie;
import com.example.Telemedi.model.TrieNode;
import com.example.Telemedi.model.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatBotService {

    @Autowired
    private ConversationalTrie conversationalTrie;

    @Autowired
    private ChatHistoryService chatHistoryService;

    @Autowired
    private UserService userService;

    private Map<String, UserSession> activeSessions = new ConcurrentHashMap<>();
    private Map<String, User> sessionUsers = new ConcurrentHashMap<>();

    public BotResponse processQuery(QueryRequest request) {
        return processQuery(request, null);
    }

    public BotResponse processQuery(QueryRequest request, String userEmail) {
        String sessionId = request.getSessionId();

        // Create new session if doesn't exist
        if (sessionId == null || sessionId.isEmpty()) {
            sessionId = UUID.randomUUID().toString();
            UserSession newSession = new UserSession(sessionId, conversationalTrie.getRoot());
            activeSessions.put(sessionId, newSession);

            // Create database session if user is logged in
            if (userEmail != null) {
                Optional<User> userOpt = userService.findByEmail(userEmail);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    sessionUsers.put(sessionId, user);
                    chatHistoryService.createChatSession(sessionId, user);
                }
            }

            BotResponse response = new BotResponse(sessionId, conversationalTrie.getRoot().getResponse());
            response.setSuggestions(Arrays.asList("headache", "fever", "cough", "stomach pain", "body ache"));

            // Save bot message to database
            saveBotMessage(sessionId, response.getMessage(), null, response.getSuggestions());

            return response;
        }

        UserSession session = activeSessions.get(sessionId);
        if (session == null) {
            // Session expired or invalid, create new one
            return processQuery(new QueryRequest(null, ""), userEmail);
        }

        // Save user message to database
        saveUserMessage(sessionId, request.getMessage());

        // Process user message
        TrieNode currentNode = session.getCurrentNode();
        TrieNode nextNode = conversationalTrie.findBestMatch(currentNode, request.getMessage());

        BotResponse response = new BotResponse();
        response.setSessionId(sessionId);

        if (nextNode != null) {
            session.setCurrentNode(nextNode);
            response.setMessage(nextNode.getResponse());
            response.setRemedy(nextNode.getRemedy());
            response.setSuggestions(nextNode.getSuggestions());
            response.setConversationEnd(nextNode.isEndNode());

            // Save bot message to database
            saveBotMessage(sessionId, response.getMessage(), response.getRemedy(), response.getSuggestions());

            if (nextNode.isEndNode()) {
                // Complete conversation and create medical history
                completeConversation(sessionId, nextNode.getRemedy());
                activeSessions.remove(sessionId);
                sessionUsers.remove(sessionId);
            }
        } else {
            // No match found, ask for clarification
            response.setMessage("I didn't quite understand that. Could you please provide more details about your symptoms?");
            response.setSuggestions(getAvailableOptions(currentNode));

            // Save bot message to database
            saveBotMessage(sessionId, response.getMessage(), null, response.getSuggestions());
        }

        return response;
    }

    private void saveUserMessage(String sessionId, String message) {
        try {
            Optional<ChatSession> sessionOpt = chatHistoryService.findChatSession(sessionId);
            if (sessionOpt.isPresent()) {
                chatHistoryService.saveMessage(sessionOpt.get(), ChatMessage.SenderType.user,
                        message, null, null);
            }
        } catch (Exception e) {
            // Log error but don't break the conversation
            System.err.println("Error saving user message: " + e.getMessage());
        }
    }

    private void saveBotMessage(String sessionId, String message, String remedy, List<String> suggestions) {
        try {
            Optional<ChatSession> sessionOpt = chatHistoryService.findChatSession(sessionId);
            if (sessionOpt.isPresent()) {
                chatHistoryService.saveMessage(sessionOpt.get(), ChatMessage.SenderType.bot,
                        message, remedy, suggestions);
            }
        } catch (Exception e) {
            // Log error but don't break the conversation
            System.err.println("Error saving bot message: " + e.getMessage());
        }
    }

    private void completeConversation(String sessionId, String diagnosis) {
        try {
            User user = sessionUsers.get(sessionId);
            if (user != null) {
                // Complete chat session
                chatHistoryService.completeChatSession(sessionId, diagnosis);

                // Create medical history record
                Optional<ChatSession> sessionOpt = chatHistoryService.findChatSession(sessionId);
                if (sessionOpt.isPresent()) {
                    // Extract symptoms from conversation
                    List<String> symptoms = extractSymptomsFromConversation(sessionId);

                    chatHistoryService.createMedicalHistory(
                            user,
                            sessionOpt.get(),
                            symptoms,
                            diagnosis,
                            diagnosis,
                            MedicalHistory.SeverityLevel.Medium
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Error completing conversation: " + e.getMessage());
        }
    }

    private List<String> extractSymptomsFromConversation(String sessionId) {
        // This is a simplified implementation
        // In a real application, you'd analyze the conversation to extract symptoms
        List<String> symptoms = new ArrayList<>();

        try {
            List<ChatMessage> messages = chatHistoryService.getChatHistory(sessionId);
            for (ChatMessage message : messages) {
                if (message.getSenderType() == ChatMessage.SenderType.user) {
                    // Simple keyword extraction
                    String text = message.getMessageText().toLowerCase();
                    if (text.contains("headache")) symptoms.add("headache");
                    if (text.contains("fever")) symptoms.add("fever");
                    if (text.contains("cough")) symptoms.add("cough");
                    if (text.contains("stomach")) symptoms.add("stomach pain");
                    if (text.contains("body") || text.contains("ache")) symptoms.add("body ache");
                }
            }
        } catch (Exception e) {
            symptoms.add("general consultation");
        }

        return symptoms.isEmpty() ? Arrays.asList("general consultation") : symptoms;
    }

    public List<ChatSession> getUserChatHistory(String userEmail) {
        Optional<User> userOpt = userService.findByEmail(userEmail);
        if (userOpt.isPresent()) {
            return chatHistoryService.getUserChatSessions(userOpt.get());
        }
        return new ArrayList<>();
    }

    public List<ChatMessage> getSessionMessages(String sessionId, String userEmail) {
        Optional<User> userOpt = userService.findByEmail(userEmail);
        if (userOpt.isPresent()) {
            return chatHistoryService.getChatHistory(sessionId);
        }
        return new ArrayList<>();
    }

    private List<String> getAvailableOptions(TrieNode node) {
        return new ArrayList<>(node.getChildren().keySet());
    }

    public void cleanupExpiredSessions() {
        long currentTime = System.currentTimeMillis();
        long sessionTimeout = 30 * 60 * 1000; // 30 minutes

        activeSessions.entrySet().removeIf(entry ->
                currentTime - entry.getValue().getLastActivity() > sessionTimeout);
    }


}
