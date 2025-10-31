package com.example.Telemedi.model;

public class TrieNode {
    private CustomMap<String, TrieNode> children;
    private String response;
    private String remedy;
    private CustomList<String> suggestions;
    private boolean isEndNode;

    public TrieNode() {
        this.children = new CustomMap<>();
        this.suggestions = new CustomList<>();
        this.isEndNode = false;
    }

    public TrieNode(String response) {
        this();
        this.response = response;
    }

    public CustomMap<String, TrieNode> getChildren() {
        return children;
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

    public java.util.List<String> getSuggestions() {
        return suggestions != null ? suggestions.toJavaList() : new java.util.ArrayList<>();
    }

    public void setSuggestions(CustomList<String> suggestions) {
        this.suggestions = suggestions;
    }

    public void setSuggestionsArray(String[] suggestionsArray) {
        this.suggestions = new CustomList<>(suggestionsArray);
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
