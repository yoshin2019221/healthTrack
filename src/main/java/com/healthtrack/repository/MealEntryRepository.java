package com.healthtrack.repository;

import com.healthtrack.entity.MealEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MealEntryRepository extends JpaRepository<MealEntry, Long> {
    @Query(value = "SELECT * FROM meal_entries WHERE CAST(entry_date AS DATE) = ?1 ORDER BY entry_date DESC", nativeQuery = true)
    List<MealEntry> findByDate(LocalDate date);

    @Query("SELECT m FROM MealEntry m ORDER BY m.entryDate DESC")
    List<MealEntry> findAllOrdered();
}
