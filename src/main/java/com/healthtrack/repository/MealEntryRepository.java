package com.healthtrack.repository;

import com.healthtrack.entity.MealEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MealEntryRepository extends JpaRepository<MealEntry, Long> {
    @Query(value = "SELECT * FROM meal_entries WHERE CAST(entry_date AS DATE) = ?1 AND user_id = ?2 ORDER BY entry_date DESC", nativeQuery = true)
    List<MealEntry> findByDateAndUserId(LocalDate date, Long userId);

    @Query("SELECT m FROM MealEntry m WHERE m.userId = ?1 ORDER BY m.entryDate DESC")
    List<MealEntry> findAllByUserId(Long userId);

    Optional<MealEntry> findByIdAndUserId(Long id, Long userId);
}
