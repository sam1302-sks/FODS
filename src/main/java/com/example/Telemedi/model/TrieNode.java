package com.example.Telemedi.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrieNode {
    private Map<String, TrieNode> children;
    private String response;
    private String remedy;
    private List<String> suggestions;
    private boolean isEndNode;

    public TrieNode() {
        this.children = new HashMap<>();
        this.isEndNode = false;
    }

    public TrieNode(String response) {
        this();
        this.response = response;
    }

    public Map<String, TrieNode> getChildren() {
        return children;
    }

    public void setChildren(Map<String, TrieNode> children) {
        this.children = children;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
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

    public boolean isEndNode() {
        return isEndNode;
    }

    public void setEndNode(boolean endNode) {
        isEndNode = endNode;
    }

    public void addChild(String keyword, TrieNode node) {
        children.put(keyword.toLowerCase(), node);
    }

    public TrieNode getChild(String keyword) {
        return children.get(keyword.toLowerCase());
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }
}
