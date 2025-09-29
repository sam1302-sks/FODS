package com.example.Telemedi.dto;

import java.util.List;

public class BotResponse {
    private String sessionId;
    private String message;
    private String remedy;
    private List<String> suggestions;
    private boolean conversationEnd;

    public BotResponse() {}

    public BotResponse(String sessionId, String message) {
        this.sessionId = sessionId;
        this.message = message;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRemedy() {
        return remedy;
    }

    public void setRemedy(String remedy) {
        this.remedy = remedy;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public boolean isConversationEnd() {
        return conversationEnd;
    }

    public void setConversationEnd(boolean conversationEnd) {
        this.conversationEnd = conversationEnd;
    }
}
