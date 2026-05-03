package com.healthtrack.service;

import org.springframework.stereotype.Service;

@Service
public class CalorieCalculationService {

    // Calculate Basal Metabolic Rate (BMR) using Mifflin-St Jeor equation (most accurate)
    public double calculateBMR(int age, double weightKg, double heightCm, String gender) {
        if ("male".equalsIgnoreCase(gender)) {
            return (10 * weightKg) + (6.25 * heightCm) - (5 * age) + 5;
        } else {
            return (10 * weightKg) + (6.25 * heightCm) - (5 * age) - 161;
        }
    }

    // Calculate Daily Caloric Expenditure using activity factor
    public double calculateDailyCalories(double bmr, String activityLevel) {
        double activityFactor = 1.55;

        switch (activityLevel.toLowerCase()) {
            case "sedentary":
                activityFactor = 1.2;
                break;
            case "light":
                activityFactor = 1.375;
                break;
            case "moderate":
                activityFactor = 1.55;
                break;
            case "very":
                activityFactor = 1.725;
                break;
            case "extreme":
                activityFactor = 1.9;
                break;
        }

        return bmr * activityFactor;
    }

    // Calculate target calories based on goal
    public CalorieTarget calculateCalorieTarget(int age, double weightKg, double heightCm, String gender,
                                               String activityLevel, String goal) {
        double bmr = calculateBMR(age, weightKg, heightCm, gender);
        double dailyCalories = calculateDailyCalories(bmr, activityLevel);

        double targetCalories;
        String description;

        if ("lose".equalsIgnoreCase(goal)) {
            targetCalories = dailyCalories - 500;
            description = "Calorie deficit for weight loss";
        } else if ("gain".equalsIgnoreCase(goal)) {
            targetCalories = dailyCalories + 500;
            description = "Calorie surplus for weight gain";
        } else {
            targetCalories = dailyCalories;
            description = "Maintenance calories";
        }

        return new CalorieTarget(
            Math.round(bmr),
            Math.round(dailyCalories),
            Math.round(targetCalories),
            description,
            goal
        );
    }

    public static class CalorieTarget {
        public long basalMetabolicRate;
        public long dailyCalories;
        public long targetCalories;
        public String description;
        public String goal;

        public CalorieTarget(long bmr, long daily, long target, String desc, String goal) {
            this.basalMetabolicRate = bmr;
            this.dailyCalories = daily;
            this.targetCalories = target;
            this.description = desc;
            this.goal = goal;
        }

        public long getBasalMetabolicRate() {
            return basalMetabolicRate;
        }

        public long getDailyCalories() {
            return dailyCalories;
        }

        public long getTargetCalories() {
            return targetCalories;
        }

        public String getDescription() {
            return description;
        }

        public String getGoal() {
            return goal;
        }
    }
}
