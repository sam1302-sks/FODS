package com.example.Telemedi.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_preferences")
public class UserPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "preference_id")
    private Long preferenceId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(length = 10)
    private String language = "en";

    @Column(name = "notification_enabled")
    private Boolean notificationEnabled = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "theme_preference")
    private ThemePreference themePreference = ThemePreference.light;

    @Enumerated(EnumType.STRING)
    @Column(name = "privacy_level")
    private PrivacyLevel privacyLevel = PrivacyLevel.private_level;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public UserPreferences() {}

    public UserPreferences(User user) {
        this.user = user;
    }

    // Getters and Setters
    public Long getPreferenceId() { return preferenceId; }
    public void setPreferenceId(Long preferenceId) { this.preferenceId = preferenceId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public Boolean getNotificationEnabled() { return notificationEnabled; }
    public void setNotificationEnabled(Boolean notificationEnabled) { this.notificationEnabled = notificationEnabled; }

    public ThemePreference getThemePreference() { return themePreference; }
    public void setThemePreference(ThemePreference themePreference) { this.themePreference = themePreference; }

    public PrivacyLevel getPrivacyLevel() { return privacyLevel; }
    public void setPrivacyLevel(PrivacyLevel privacyLevel) { this.privacyLevel = privacyLevel; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public enum ThemePreference {
        light, dark, auto
    }

    public enum PrivacyLevel {
        public_level, private_level
    }
}
