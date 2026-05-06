package com.healthtrack.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "meal_entries")
public class MealEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    private String foodName;
    private String portion;
    private String unit;
    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fat;
    private LocalDateTime entryDate;
    private String notes;

    public MealEntry() {}

    public MealEntry(String foodName, String portion, String unit, Double calories,
                     Double protein, Double carbs, Double fat, LocalDateTime entryDate, String notes) {
        this.foodName = foodName;
        this.portion = portion;
        this.unit = unit;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.entryDate = entryDate;
        this.notes = notes;
    }

    @PrePersist
    protected void onCreate() {
        entryDate = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }
    public String getPortion() { return portion; }
    public void setPortion(String portion) { this.portion = portion; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public Double getCalories() { return calories; }
    public void setCalories(Double calories) { this.calories = calories; }
    public Double getProtein() { return protein; }
    public void setProtein(Double protein) { this.protein = protein; }
    public Double getCarbs() { return carbs; }
    public void setCarbs(Double carbs) { this.carbs = carbs; }
    public Double getFat() { return fat; }
    public void setFat(Double fat) { this.fat = fat; }
    public LocalDateTime getEntryDate() { return entryDate; }
    public void setEntryDate(LocalDateTime entryDate) { this.entryDate = entryDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
