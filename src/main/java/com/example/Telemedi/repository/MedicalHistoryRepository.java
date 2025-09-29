package com.example.Telemedi.repository;

import com.example.Telemedi.entity.MedicalHistory;
import com.example.Telemedi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {

    List<MedicalHistory> findByUserOrderByConsultationDateDesc(User user);

    List<MedicalHistory> findByUserAndFollowUpRequiredOrderByFollowUpDateAsc(User user, Boolean followUpRequired);

    @Query("SELECT mh FROM MedicalHistory mh WHERE mh.user = :user AND mh.consultationDate >= :since ORDER BY mh.consultationDate DESC")
    List<MedicalHistory> findRecentHistoryByUser(@Param("user") User user, @Param("since") LocalDateTime since);

    @Query("SELECT COUNT(mh) FROM MedicalHistory mh WHERE mh.user = :user")
    long countHistoryByUser(@Param("user") User user);
}
