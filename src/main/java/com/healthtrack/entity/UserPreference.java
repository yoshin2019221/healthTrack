package com.healthtrack.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_preferences")
public class UserPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String preference;
    private String dietStatus;
    private String homeIngredients;
    private Integer dailyCalorieGoal;

    public UserPreference() {
        this.preference = "all";
        this.dietStatus = "ontrack";
        this.homeIngredients = "";
        this.dailyCalorieGoal = 2000;
    }

    public UserPreference(String preference, String dietStatus, String homeIngredients, Integer dailyCalorieGoal) {
        this.preference = preference;
        this.dietStatus = dietStatus;
        this.homeIngredients = homeIngredients;
        this.dailyCalorieGoal = dailyCalorieGoal;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPreference() { return preference; }
    public void setPreference(String preference) { this.preference = preference; }
    public String getDietStatus() { return dietStatus; }
    public void setDietStatus(String dietStatus) { this.dietStatus = dietStatus; }
    public String getHomeIngredients() { return homeIngredients; }
    public void setHomeIngredients(String homeIngredients) { this.homeIngredients = homeIngredients; }
    public Integer getDailyCalorieGoal() { return dailyCalorieGoal; }
    public void setDailyCalorieGoal(Integer dailyCalorieGoal) { this.dailyCalorieGoal = dailyCalorieGoal; }
}
