-- Create Database
CREATE DATABASE telemedi_db;
USE telemedi_db;

-- Users Table
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    date_of_birth DATE,
    gender ENUM('Male', 'Female', 'Other'),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    last_login TIMESTAMP NULL,
    profile_image VARCHAR(500) NULL
);

-- Chat Sessions Table
CREATE TABLE chat_sessions (
    session_id VARCHAR(255) PRIMARY KEY,
    user_id INT NOT NULL,
    session_title VARCHAR(255) DEFAULT 'Medical Consultation',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_completed BOOLEAN DEFAULT FALSE,
    diagnosis VARCHAR(1000) NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at)
);

-- Chat Messages Table
CREATE TABLE chat_messages (
    message_id INT PRIMARY KEY AUTO_INCREMENT,
    session_id VARCHAR(255) NOT NULL,
    sender_type ENUM('user', 'bot') NOT NULL,
    message_text TEXT NOT NULL,
    remedy_text TEXT NULL,
    suggestions JSON NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_system_message BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (session_id) REFERENCES chat_sessions(session_id) ON DELETE CASCADE,
    INDEX idx_session_id (session_id),
    INDEX idx_timestamp (timestamp)
);

-- Medical History Table
CREATE TABLE medical_history (
    history_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    session_id VARCHAR(255) NOT NULL,
    symptoms JSON NOT NULL,
    diagnosis VARCHAR(1000) NULL,
    recommended_treatment TEXT NULL,
    severity_level ENUM('Low', 'Medium', 'High', 'Critical') DEFAULT 'Medium',
    consultation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    follow_up_required BOOLEAN DEFAULT FALSE,
    follow_up_date DATE NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (session_id) REFERENCES chat_sessions(session_id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_consultation_date (consultation_date)
);

-- User Preferences Table
CREATE TABLE user_preferences (
    preference_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    language VARCHAR(10) DEFAULT 'en',
    notification_enabled BOOLEAN DEFAULT TRUE,
    theme_preference ENUM('light', 'dark', 'auto') DEFAULT 'light',
    privacy_level ENUM('public', 'private') DEFAULT 'private',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_prefs (user_id)
);

-- Insert Sample Data
INSERT INTO users (email, password_hash, full_name, phone, date_of_birth, gender) VALUES 
('samarth@example.com', '$2a$10$encoded_password_hash', 'Samarth Patil', '+91-9876543210', '1995-06-15', 'Male'),
('K@example.com', '$2a$10$encoded_password_hash', 'Krupa', '+91-8765432109', '1992-03-22', 'Female');

-- Create indexes for better performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_chat_sessions_user_created ON chat_sessions(user_id, created_at DESC);
CREATE INDEX idx_medical_history_user_date ON medical_history(user_id, consultation_date DESC);

-- Views for easy querying
CREATE VIEW user_chat_summary AS
SELECT 
    u.user_id,
    u.full_name,
    u.email,
    COUNT(DISTINCT cs.session_id) as total_sessions,
    MAX(cs.updated_at) as last_consultation,
    COUNT(mh.history_id) as total_medical_records
FROM users u
LEFT JOIN chat_sessions cs ON u.user_id = cs.user_id
LEFT JOIN medical_history mh ON u.user_id = mh.user_id
WHERE u.is_active = TRUE
GROUP BY u.user_id, u.full_name, u.email;

-- Stored procedures for common operations
DELIMITER //

CREATE PROCEDURE GetUserChatHistory(IN user_email VARCHAR(255))
BEGIN
    SELECT 
        cs.session_id,
        cs.session_title,
        cs.created_at,
        cs.is_completed,
        COUNT(cm.message_id) as message_count
    FROM users u
    JOIN chat_sessions cs ON u.user_id = cs.user_id
    LEFT JOIN chat_messages cm ON cs.session_id = cm.session_id
    WHERE u.email = user_email AND u.is_active = TRUE
    GROUP BY cs.session_id, cs.session_title, cs.created_at, cs.is_completed
    ORDER BY cs.created_at DESC;
END //

CREATE PROCEDURE GetChatMessages(IN session_id VARCHAR(255))
BEGIN
    SELECT 
        message_id,
        sender_type,
        message_text,
        remedy_text,
        suggestions,
        timestamp,
        is_system_message
    FROM chat_messages
    WHERE session_id = session_id
    ORDER BY timestamp ASC;
END //

DELIMITER ;
select* from users;