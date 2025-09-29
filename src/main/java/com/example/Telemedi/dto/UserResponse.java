package com.example.Telemedi.dto;

import java.time.LocalDateTime;

public class UserResponse {

    private Long userId;
    private String email;
    private String fullName;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private long totalSessions;
    private long totalConsultations;

    public UserResponse() {}

    public UserResponse(Long userId, String email, String fullName) {
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public long getTotalSessions() { return totalSessions; }
    public void setTotalSessions(long totalSessions) { this.totalSessions = totalSessions; }

    public long getTotalConsultations() { return totalConsultations; }
    public void setTotalConsultations(long totalConsultations) { this.totalConsultations = totalConsultations; }
}
