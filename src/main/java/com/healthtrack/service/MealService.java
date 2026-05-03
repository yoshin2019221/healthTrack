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
    @Autowired
    private MealEntryRepository mealEntryRepository;

    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    @Autowired
    private FoodDatabase foodDatabase;

    public MealEntry addMeal(String foodName, String portion, String unit, Double calories,
                            Double protein, Double carbs, Double fat, String notes) {
        MealEntry meal = new MealEntry();
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

    public List<MealEntry> getTodayMeals() {
        return mealEntryRepository.findByDate(LocalDate.now());
    }

    public List<MealEntry> getAllMeals() {
        return mealEntryRepository.findAllOrdered();
    }

    public void deleteMeal(Long id) {
        mealEntryRepository.deleteById(id);
    }

    public MealEntry updateMeal(Long id, String foodName, String portion, String unit,
                               Double calories, Double protein, Double carbs, Double fat) {
        Optional<MealEntry> mealOpt = mealEntryRepository.findById(id);
        if (mealOpt.isPresent()) {
            MealEntry meal = mealOpt.get();
            meal.setFoodName(foodName);
            meal.setPortion(portion);
            meal.setUnit(unit);
            meal.setCalories(calories);
            meal.setProtein(protein);
            meal.setCarbs(carbs);
            meal.setFat(fat);
            return mealEntryRepository.save(meal);
        }
        return null;
    }

    public Map<String, Double> getDailySummary() {
        List<MealEntry> meals = getTodayMeals();
        Map<String, Double> summary = new HashMap<>();
        summary.put("calories", 0.0);
        summary.put("protein", 0.0);
        summary.put("carbs", 0.0);
        summary.put("fat", 0.0);

        for (MealEntry meal : meals) {
            summary.put("calories", summary.get("calories") + (meal.getCalories() != null ? meal.getCalories() : 0));
            summary.put("protein", summary.get("protein") + (meal.getProtein() != null ? meal.getProtein() : 0));
            summary.put("carbs", summary.get("carbs") + (meal.getCarbs() != null ? meal.getCarbs() : 0));
            summary.put("fat", summary.get("fat") + (meal.getFat() != null ? meal.getFat() : 0));
        }

        return summary;
    }

    public UserPreference getOrCreatePreference() {
        List<UserPreference> prefs = userPreferenceRepository.findAll();
        if (prefs.isEmpty()) {
            return userPreferenceRepository.save(new UserPreference());
        }
        return prefs.get(0);
    }

    public UserPreference updatePreference(String preference, String dietStatus,
                                          String homeIngredients, Integer dailyGoal) {
        UserPreference pref = getOrCreatePreference();
        if (preference != null) pref.setPreference(preference);
        if (dietStatus != null) pref.setDietStatus(dietStatus);
        if (homeIngredients != null) pref.setHomeIngredients(homeIngredients);
        if (dailyGoal != null) pref.setDailyCalorieGoal(dailyGoal);
        return userPreferenceRepository.save(pref);
    }

    public List<Map<String, Object>> getSuggestions(UserPreference pref) {
        List<Map<String, Object>> suggestions = new ArrayList<>();

        String mealTime = getCurrentMealTime();
        String preference = pref.getPreference() != null ? pref.getPreference() : "all";

        List<String> breakfastOptions = new ArrayList<>();
        List<String> lunchDinnerOptions = new ArrayList<>();
        List<String> snackOptions = new ArrayList<>();

        if ("vegan".equalsIgnoreCase(preference) || "vegetarian".equalsIgnoreCase(preference) || "all".equalsIgnoreCase(preference)) {
            breakfastOptions.addAll(java.util.Arrays.asList(
                "Dosa with Sambar (200g dosa)",
                "Idli with Chutney (4-5 pieces)",
                "Paratha with Curd (2 pieces, 100g curd)",
                "Upma with Vegetables (1 cup)",
                "Poha with Peanuts (1 cup)",
                "Oats Khichdi (1 cup)"
            ));

            lunchDinnerOptions.addAll(java.util.Arrays.asList(
                "Dal Chawal - Lentils with Rice (150g dal, 140g rice)",
                "Roti with Mixed Vegetable Curry (2 rotis, 1 cup curry)",
                "Chole Bhature (1 bhature, 150g chole curry)",
                "Paneer Tikka with Brown Rice (150g paneer, 140g rice)",
                "Rajma Rice - Kidney Beans with Rice (150g rajma, 140g rice)",
                "Baingan Bharta with Roti (1 cup bharta, 2 rotis)",
                "Chana Masala with Rice (150g curry, 140g rice)"
            ));

            snackOptions.addAll(java.util.Arrays.asList(
                "Roasted Chana (30g)",
                "Buttermilk with Jeera (200ml)",
                "Mixed Fruit Bowl (150g)",
                "Sprout Salad (100g)",
                "Baked Samosa (1 piece, 50g)",
                "Greek Yogurt (100g)",
                "Whole Wheat Biscuits (2 pieces, 25g)"
            ));
        }

        if ("all".equalsIgnoreCase(preference) || "highprotein".equalsIgnoreCase(preference)) {
            lunchDinnerOptions.addAll(java.util.Arrays.asList(
                "Tandoori Chicken with Brown Rice (150g chicken, 140g rice)",
                "Fish Curry with Rice (150g fish, 140g rice)",
                "Egg Curry with Roti (2 eggs, 2 rotis, 1 cup curry)",
                "Mutton Keema with Rice (150g keema, 140g rice)"
            ));

            breakfastOptions.add("Scrambled Eggs with Toast (2 eggs, 1 slice)");
            snackOptions.add("Boiled Eggs (2 pieces, 100g)");
        }

        if ("lowcarb".equalsIgnoreCase(preference) || "all".equalsIgnoreCase(preference)) {
            lunchDinnerOptions.addAll(java.util.Arrays.asList(
                "Tandoori Paneer with Salad (200g paneer, 150g salad)",
                "Grilled Fish with Vegetable Sabzi (150g fish, 150g sabzi)",
                "Chicken Tikka with Cucumber Salad (150g chicken, 150g salad)"
            ));

            snackOptions.addAll(java.util.Arrays.asList(
                "Cucumber and Carrot Sticks (150g)",
                "Mixed Nuts (30g)",
                "String Cheese (50g)"
            ));
        }

        List<String> selectedSuggestions = new ArrayList<>();

        if ("breakfast".equalsIgnoreCase(mealTime)) {
            selectedSuggestions.addAll(breakfastOptions);
        } else if ("lunch".equalsIgnoreCase(mealTime)) {
            selectedSuggestions.addAll(lunchDinnerOptions);
        } else if ("dinner".equalsIgnoreCase(mealTime)) {
            selectedSuggestions.addAll(lunchDinnerOptions);
        } else if ("snack".equalsIgnoreCase(mealTime)) {
            selectedSuggestions.addAll(snackOptions);
        } else {
            selectedSuggestions.addAll(breakfastOptions);
            selectedSuggestions.addAll(lunchDinnerOptions);
            selectedSuggestions.addAll(snackOptions);
        }

        for (String suggestion : selectedSuggestions) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", suggestion);
            item.put("calories", estimateCaloriesForIndianFood(suggestion));
            item.put("description", getIndianFoodDescription(suggestion));
            suggestions.add(item);
        }

        return suggestions;
    }

    private String getCurrentMealTime() {
        int hour = java.time.LocalDateTime.now().getHour();
        if (hour >= 6 && hour < 11) return "breakfast";
        if (hour >= 11 && hour < 15) return "lunch";
        if (hour >= 15 && hour < 18) return "snack";
        if (hour >= 18 && hour < 22) return "dinner";
        return "snack";
    }

    private double estimateCaloriesForIndianFood(String food) {
        if (food.contains("Dosa")) return 250;
        if (food.contains("Idli")) return 150;
        if (food.contains("Paratha")) return 300;
        if (food.contains("Upma")) return 200;
        if (food.contains("Poha")) return 180;
        if (food.contains("Dal Chawal")) return 400;
        if (food.contains("Roti with")) return 350;
        if (food.contains("Chole Bhature")) return 450;
        if (food.contains("Paneer")) return 380;
        if (food.contains("Rajma")) return 420;
        if (food.contains("Tandoori")) return 350;
        if (food.contains("Fish Curry")) return 380;
        if (food.contains("Egg Curry")) return 320;
        if (food.contains("Chana Masala")) return 380;
        if (food.contains("Roasted Chana")) return 120;
        if (food.contains("Buttermilk")) return 100;
        if (food.contains("Fruit")) return 80;
        if (food.contains("Sprout")) return 80;
        if (food.contains("Samosa")) return 150;
        if (food.contains("Yogurt")) return 100;
        if (food.contains("Biscuits")) return 80;
        if (food.contains("Eggs")) return 140;
        if (food.contains("Cucumber")) return 50;
        if (food.contains("Nuts")) return 180;
        if (food.contains("Cheese")) return 200;
        return 250;
    }

    private String getIndianFoodDescription(String food) {
        if (food.contains("Dosa")) return "South Indian crispy rice and lentil crepe - great source of carbs and protein";
        if (food.contains("Idli")) return "Steamed rice and lentil cakes - light, digestible breakfast option";
        if (food.contains("Paratha")) return "Whole wheat flatbread - rich in fiber and satisfying";
        if (food.contains("Dal Chawal")) return "Classic combination of lentils and rice - complete protein";
        if (food.contains("Tandoori")) return "Grilled preparation - high in protein, low in fat";
        if (food.contains("Paneer")) return "Indian cottage cheese - excellent vegetarian protein source";
        if (food.contains("Fish")) return "Rich in omega-3 fatty acids and lean protein";
        if (food.contains("Roasted Chana")) return "Chickpeas - rich in fiber and plant-based protein";
        if (food.contains("Rajma")) return "Kidney beans with rice - complete protein and good for digestion";
        return "Traditional Indian meal - well-balanced nutrition";
    }

    public List<FoodNutrition> searchFoods(String query) {
        return foodDatabase.searchFoods(query);
    }
}
