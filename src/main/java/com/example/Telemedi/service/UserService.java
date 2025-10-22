package com.example.Telemedi.service;

import com.example.Telemedi.entity.User;
import com.example.Telemedi.entity.UserPreferences;
import com.example.Telemedi.repository.UserRepository;
import com.example.Telemedi.repository.UserPreferencesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPreferencesRepository userPreferencesRepository;

    // FIXED: Use static final with consistent strength
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    public User registerUser(String email, String password, String fullName) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setFullName(fullName);
        user.setIsActive(true);

        User savedUser = userRepository.save(user);

        // Create default preferences
        UserPreferences preferences = new UserPreferences(savedUser);
        userPreferencesRepository.save(preferences);

        return savedUser;
    }

    public Optional<User> authenticateUser(String email, String password) {
        Optional<User> userOpt = userRepository.findActiveUserByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPasswordHash())) {
                // Update last login
                user.setLastLogin(LocalDateTime.now());
                userRepository.save(user);
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findActiveUserByEmail(email);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deactivateUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setIsActive(false);
            userRepository.save(user);
        }
    }

    public boolean changePassword(String email, String oldPassword, String newPassword) {
        Optional<User> userOpt = userRepository.findActiveUserByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
                user.setPasswordHash(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return true;
            }
        }

        return false;
    }

    public long getTotalActiveUsers() {
        return userRepository.countActiveUsers();
    }
}
