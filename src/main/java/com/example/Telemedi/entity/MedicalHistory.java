package com.example.Telemedi.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_history")
public class MedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ChatSession chatSession;

    @Column(name = "symptoms", columnDefinition = "JSON", nullable = false)
    private String symptoms; // JSON string

    private String diagnosis;

    @Column(name = "recommended_treatment", columnDefinition = "TEXT")
    private String recommendedTreatment;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity_level")
    private SeverityLevel severityLevel = SeverityLevel.Medium;

    @CreationTimestamp
    @Column(name = "consultation_date", updatable = false)
    private LocalDateTime consultationDate;

    @Column(name = "follow_up_required")
    private Boolean followUpRequired = false;

    @Column(name = "follow_up_date")
    private LocalDate followUpDate;

    // Constructors
    public MedicalHistory() {}

    public MedicalHistory(User user, ChatSession chatSession, String symptoms) {
        this.user = user;
        this.chatSession = chatSession;
        this.symptoms = symptoms;
    }

    // Getters and Setters
    public Long getHistoryId() { return historyId; }
    public void setHistoryId(Long historyId) { this.historyId = historyId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public ChatSession getChatSession() { return chatSession; }
    public void setChatSession(ChatSession chatSession) { this.chatSession = chatSession; }

    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getRecommendedTreatment() { return recommendedTreatment; }
    public void setRecommendedTreatment(String recommendedTreatment) { this.recommendedTreatment = recommendedTreatment; }

    public SeverityLevel getSeverityLevel() { return severityLevel; }
    public void setSeverityLevel(SeverityLevel severityLevel) { this.severityLevel = severityLevel; }

    public LocalDateTime getConsultationDate() { return consultationDate; }
    public void setConsultationDate(LocalDateTime consultationDate) { this.consultationDate = consultationDate; }

    public Boolean getFollowUpRequired() { return followUpRequired; }
    public void setFollowUpRequired(Boolean followUpRequired) { this.followUpRequired = followUpRequired; }

    public LocalDate getFollowUpDate() { return followUpDate; }
    public void setFollowUpDate(LocalDate followUpDate) { this.followUpDate = followUpDate; }

    public enum SeverityLevel {
        Low, Medium, High, Critical
    }
}
