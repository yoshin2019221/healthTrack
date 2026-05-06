package com.healthtrack.service;

import com.healthtrack.entity.MealEntry;
import com.healthtrack.entity.UserPreference;
import com.healthtrack.model.FoodNutrition;
import com.healthtrack.repository.MealEntryRepository;
import com.healthtrack.repository.UserPreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class MealService {
    @Autowired private MealEntryRepository mealEntryRepository;
    @Autowired private UserPreferenceRepository userPreferenceRepository;
    @Autowired private FoodDatabase foodDatabase;

    public MealEntry addMeal(String foodName, String portion, String unit, Double calories,
                             Double protein, Double carbs, Double fat, String notes, Long userId) {
        MealEntry meal = new MealEntry();
        meal.setUserId(userId);
        meal.setFoodName(foodName);
        meal.setPortion(portion);
        meal.setUnit(unit);
        meal.setCalories(calories);
        meal.setProtein(protein);
        meal.setCarbs(carbs);
        meal.setFat(fat);
        meal.setNotes(notes);
        meal.setEntryDate(LocalDateTime.now());
        return mealEntryRepository.save(meal);
    }

    public List<MealEntry> getTodayMeals(Long userId) {
        return mealEntryRepository.findByDateAndUserId(LocalDate.now(), userId);
    }

    public List<MealEntry> getAllMeals(Long userId) {
        return mealEntryRepository.findAllByUserId(userId);
    }

    public void deleteMeal(Long id, Long userId) {
        mealEntryRepository.findByIdAndUserId(id, userId).ifPresent(mealEntryRepository::delete);
    }

    public MealEntry updateMeal(Long id, String foodName, String portion, String unit,
                                Double calories, Double protein, Double carbs, Double fat, Long userId) {
        return mealEntryRepository.findByIdAndUserId(id, userId).map(meal -> {
            meal.setFoodName(foodName);
            meal.setPortion(portion);
            meal.setUnit(unit);
            meal.setCalories(calories);
            meal.setProtein(protein);
            meal.setCarbs(carbs);
            meal.setFat(fat);
            return mealEntryRepository.save(meal);
        }).orElse(null);
    }

    public Map<String, Double> getDailySummary(Long userId) {
        List<MealEntry> meals = getTodayMeals(userId);
        Map<String, Double> summary = new HashMap<>();
        summary.put("calories", 0.0);
        summary.put("protein", 0.0);
        summary.put("carbs", 0.0);
        summary.put("fat", 0.0);
        for (MealEntry meal : meals) {
            summary.merge("calories", meal.getCalories() != null ? meal.getCalories() : 0, Double::sum);
            summary.merge("protein",  meal.getProtein()  != null ? meal.getProtein()  : 0, Double::sum);
            summary.merge("carbs",    meal.getCarbs()    != null ? meal.getCarbs()    : 0, Double::sum);
            summary.merge("fat",      meal.getFat()      != null ? meal.getFat()      : 0, Double::sum);
        }
        return summary;
    }

    public UserPreference getOrCreatePreference(Long userId) {
        return userPreferenceRepository.findByUserId(userId).orElseGet(() -> {
            UserPreference pref = new UserPreference();
            pref.setUserId(userId);
            return userPreferenceRepository.save(pref);
        });
    }

    public UserPreference updatePreference(String preference, String dietStatus,
                                           String homeIngredients, Integer dailyGoal, Long userId) {
        UserPreference pref = getOrCreatePreference(userId);
        if (preference != null) pref.setPreference(preference);
        if (dietStatus != null) pref.setDietStatus(dietStatus);
        if (homeIngredients != null) pref.setHomeIngredients(homeIngredients);
        if (dailyGoal != null) pref.setDailyCalorieGoal(dailyGoal);
        return userPreferenceRepository.save(pref);
    }

    public List<FoodNutrition> searchFoods(String query) {
        return foodDatabase.searchFoods(query);
    }

    public List<Map<String, Object>> getSuggestions(UserPreference pref) {
        List<Map<String, Object>> suggestions = new ArrayList<>();
        String mealTime = getCurrentMealTime();
        String preference = pref.getPreference() != null ? pref.getPreference() : "all";

        List<String> breakfastOptions = new ArrayList<>();
        List<String> lunchDinnerOptions = new ArrayList<>();
        List<String> snackOptions = new ArrayList<>();

        if ("vegan".equalsIgnoreCase(preference) || "vegetarian".equalsIgnoreCase(preference) || "all".equalsIgnoreCase(preference)) {
            breakfastOptions.addAll(Arrays.asList("Dosa with Sambar (200g dosa)", "Idli with Chutney (4-5 pieces)", "Paratha with Curd (2 pieces, 100g curd)", "Upma with Vegetables (1 cup)", "Poha with Peanuts (1 cup)"));
            lunchDinnerOptions.addAll(Arrays.asList("Dal Chawal - Lentils with Rice (150g dal, 140g rice)", "Roti with Mixed Vegetable Curry (2 rotis, 1 cup curry)", "Paneer Tikka with Brown Rice (150g paneer, 140g rice)", "Rajma Rice (150g rajma, 140g rice)", "Chana Masala with Rice (150g curry, 140g rice)"));
            snackOptions.addAll(Arrays.asList("Roasted Chana (30g)", "Buttermilk with Jeera (200ml)", "Mixed Fruit Bowl (150g)", "Sprout Salad (100g)"));
        }
        if ("all".equalsIgnoreCase(preference) || "highprotein".equalsIgnoreCase(preference)) {
            lunchDinnerOptions.addAll(Arrays.asList("Tandoori Chicken with Brown Rice (150g chicken, 140g rice)", "Fish Curry with Rice (150g fish, 140g rice)", "Egg Curry with Roti (2 eggs, 2 rotis)"));
            breakfastOptions.add("Scrambled Eggs with Toast (2 eggs, 1 slice)");
        }
        if ("lowcarb".equalsIgnoreCase(preference) || "all".equalsIgnoreCase(preference)) {
            lunchDinnerOptions.addAll(Arrays.asList("Tandoori Paneer with Salad (200g paneer)", "Grilled Fish with Vegetable Sabzi (150g fish, 150g sabzi)"));
            snackOptions.addAll(Arrays.asList("Cucumber and Carrot Sticks (150g)", "Mixed Nuts (30g)"));
        }

        List<String> selected = new ArrayList<>();
        if ("breakfast".equalsIgnoreCase(mealTime)) selected.addAll(breakfastOptions);
        else if ("lunch".equalsIgnoreCase(mealTime) || "dinner".equalsIgnoreCase(mealTime)) selected.addAll(lunchDinnerOptions);
        else if ("snack".equalsIgnoreCase(mealTime)) selected.addAll(snackOptions);
        else { selected.addAll(breakfastOptions); selected.addAll(lunchDinnerOptions); selected.addAll(snackOptions); }

        for (String s : selected) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", s);
            item.put("calories", estimateCalories(s));
            item.put("description", "Traditional Indian meal");
            suggestions.add(item);
        }
        return suggestions;
    }

    private String getCurrentMealTime() {
        int hour = LocalDateTime.now().getHour();
        if (hour >= 6 && hour < 11) return "breakfast";
        if (hour >= 11 && hour < 15) return "lunch";
        if (hour >= 15 && hour < 18) return "snack";
        if (hour >= 18 && hour < 22) return "dinner";
        return "snack";
    }

    private double estimateCalories(String food) {
        if (food.contains("Dosa")) return 250;
        if (food.contains("Idli")) return 150;
        if (food.contains("Paratha")) return 300;
        if (food.contains("Dal Chawal")) return 400;
        if (food.contains("Paneer")) return 380;
        if (food.contains("Rajma")) return 420;
        if (food.contains("Tandoori")) return 350;
        if (food.contains("Fish")) return 380;
        if (food.contains("Egg")) return 320;
        if (food.contains("Chana")) return 380;
        if (food.contains("Roasted Chana")) return 120;
        if (food.contains("Nuts")) return 180;
        return 250;
    }
}
