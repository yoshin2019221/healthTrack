package com.healthtrack.model;

public class FoodNutrition {
    private String name;
    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fat;
    private String baseUnit;
    private Integer baseAmount;

    public FoodNutrition() {}

    public FoodNutrition(String name, Double calories, Double protein, Double carbs, Double fat, String baseUnit, Integer baseAmount) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.baseUnit = baseUnit;
        this.baseAmount = baseAmount;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getCalories() { return calories; }
    public void setCalories(Double calories) { this.calories = calories; }
    public Double getProtein() { return protein; }
    public void setProtein(Double protein) { this.protein = protein; }
    public Double getCarbs() { return carbs; }
    public void setCarbs(Double carbs) { this.carbs = carbs; }
    public Double getFat() { return fat; }
    public void setFat(Double fat) { this.fat = fat; }
    public String getBaseUnit() { return baseUnit; }
    public void setBaseUnit(String baseUnit) { this.baseUnit = baseUnit; }
    public Integer getBaseAmount() { return baseAmount; }
    public void setBaseAmount(Integer baseAmount) { this.baseAmount = baseAmount; }
}
