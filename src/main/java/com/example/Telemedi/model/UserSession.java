package com.example.Telemedi.model;

public class UserSession {
    private String sessionId;
    private TrieNode currentNode;
    private long lastActivity;

    public UserSession(String sessionId, TrieNode startNode) {
        this.sessionId = sessionId;
        this.currentNode = startNode;
        this.lastActivity = System.currentTimeMillis();
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public TrieNode getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(TrieNode currentNode) {
        this.currentNode = currentNode;
        this.lastActivity = System.currentTimeMillis();
    }

    public long getLastActivity() {
        return lastActivity;
    }

    public void updateActivity() {
        this.lastActivity = System.currentTimeMillis();
    }
}
