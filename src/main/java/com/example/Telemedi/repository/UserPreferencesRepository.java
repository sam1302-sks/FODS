package com.example.Telemedi.repository;

import com.example.Telemedi.entity.User;
import com.example.Telemedi.entity.UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Long> {

    Optional<UserPreferences> findByUser(User user);

    void deleteByUser(User user);
}
