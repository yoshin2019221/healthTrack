package com.healthtrack.controller;

import com.healthtrack.entity.MealEntry;
import com.healthtrack.entity.UserPreference;
import com.healthtrack.model.FoodNutrition;
import com.healthtrack.service.MealService;
import com.healthtrack.service.OpenAIService;
import com.healthtrack.service.FoodParser;
import com.healthtrack.service.CalorieCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiController {
    @Autowired
    private MealService mealService;

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private FoodParser foodParser;

    @Autowired
    private CalorieCalculationService calorieCalculationService;

    @GetMapping("/meals/today")
    public ResponseEntity<?> getTodayMeals() {
        return ResponseEntity.ok(mealService.getTodayMeals());
    }

    @GetMapping("/meals")
    public ResponseEntity<?> getAllMeals() {
        return ResponseEntity.ok(mealService.getAllMeals());
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getDailySummary() {
        Map<String, Double> summary = mealService.getDailySummary();
        UserPreference pref = mealService.getOrCreatePreference();

        Map<String, Object> response = new HashMap<>();
        response.put("calories", Math.round(summary.get("calories")));
        response.put("protein", Math.round(summary.get("protein") * 10.0) / 10.0);
        response.put("carbs", Math.round(summary.get("carbs") * 10.0) / 10.0);
        response.put("fat", Math.round(summary.get("fat") * 10.0) / 10.0);
        response.put("dailyGoal", pref.getDailyCalorieGoal());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/meals")
    public ResponseEntity<?> addMeal(@RequestBody Map<String, Object> request) {
        try {
            String foodName = (String) request.get("foodName");
            String portion = request.get("portion").toString();
            String unit = (String) request.get("unit");
            Double calories = Double.parseDouble(request.get("calories").toString());
            Double protein = Double.parseDouble(request.get("protein").toString());
            Double carbs = Double.parseDouble(request.get("carbs").toString());
            Double fat = Double.parseDouble(request.get("fat").toString());
            String notes = (String) request.getOrDefault("notes", "");

            MealEntry meal = mealService.addMeal(foodName, portion, unit, calories, protein, carbs, fat, notes);
            return ResponseEntity.ok(meal);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                put("error", "Failed to add meal: " + e.getMessage());
            }});
        }
    }

    @DeleteMapping("/meals/{id}")
    public ResponseEntity<?> deleteMeal(@PathVariable Long id) {
        try {
            mealService.deleteMeal(id);
            return ResponseEntity.ok(new HashMap<String, String>() {{
                put("message", "Meal deleted successfully");
            }});
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                put("error", "Failed to delete meal: " + e.getMessage());
            }});
        }
    }

    @PutMapping("/meals/{id}")
    public ResponseEntity<?> updateMeal(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            String foodName = (String) request.get("foodName");
            String portion = request.get("portion").toString();
            String unit = (String) request.get("unit");
            Double calories = Double.parseDouble(request.get("calories").toString());
            Double protein = Double.parseDouble(request.get("protein").toString());
            Double carbs = Double.parseDouble(request.get("carbs").toString());
            Double fat = Double.parseDouble(request.get("fat").toString());

            MealEntry meal = mealService.updateMeal(id, foodName, portion, unit, calories, protein, carbs, fat);
            return ResponseEntity.ok(meal);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                put("error", "Failed to update meal: " + e.getMessage());
            }});
        }
    }

    @GetMapping("/foods/search")
    public ResponseEntity<?> searchFoods(@RequestParam String q) {
        List<FoodNutrition> results = mealService.searchFoods(q);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/preference")
    public ResponseEntity<?> getPreference() {
        return ResponseEntity.ok(mealService.getOrCreatePreference());
    }

    @PostMapping("/preference")
    public ResponseEntity<?> updatePreference(@RequestBody Map<String, Object> request) {
        try {
            String preference = (String) request.get("preference");
            String dietStatus = (String) request.get("dietStatus");
            String homeIngredients = (String) request.get("homeIngredients");
            Integer dailyGoal = request.get("dailyGoal") != null ?
                    Integer.parseInt(request.get("dailyGoal").toString()) : null;

            UserPreference updated = mealService.updatePreference(preference, dietStatus, homeIngredients, dailyGoal);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                put("error", "Failed to update preference: " + e.getMessage());
            }});
        }
    }

    @GetMapping("/suggestions")
    public ResponseEntity<?> getSuggestions() {
        UserPreference pref = mealService.getOrCreatePreference();
        return ResponseEntity.ok(mealService.getSuggestions(pref));
    }

    @PostMapping("/ai/api-key")
    public ResponseEntity<?> setApiKey(@RequestBody Map<String, String> request) {
        try {
            String apiKey = request.get("apiKey");
            if (apiKey == null || apiKey.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                    put("error", "API key cannot be empty");
                }});
            }
            openAIService.setApiKey(apiKey);
            return ResponseEntity.ok(new HashMap<String, String>() {{
                put("message", "API key configured successfully");
                put("status", "connected");
            }});
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                put("error", "Failed to set API key: " + e.getMessage());
            }});
        }
    }

    @GetMapping("/ai/status")
    public ResponseEntity<?> getAIStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("connected", openAIService.isConfigured());
        status.put("status", openAIService.isConfigured() ? "connected" : "disconnected");
        return ResponseEntity.ok(status);
    }

    @PostMapping("/ai/suggestions")
    public ResponseEntity<?> getAISuggestions(@RequestBody Map<String, String> request) {
        try {
            String userInput = request.getOrDefault("input", "");
            UserPreference pref = mealService.getOrCreatePreference();
            String preferences = pref.getPreference();
            String ingredients = pref.getHomeIngredients();

            List<Map<String, Object>> suggestions = openAIService.getMealSuggestions(userInput, preferences, ingredients);
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                put("error", "Failed to get suggestions: " + e.getMessage());
            }});
        }
    }

    @PostMapping("/ai/parse-food")
    public ResponseEntity<?> parseFoodText(@RequestBody Map<String, String> request) {
        try {
            String foodText = request.get("text");
            if (!openAIService.isConfigured()) {
                return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                    put("error", "OpenAI API not configured");
                }});
            }
            Map<String, Object> parsed = openAIService.parseFoodText(foodText);
            if (parsed == null) {
                return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                    put("error", "Failed to parse food text");
                }});
            }
            return ResponseEntity.ok(parsed);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                put("error", "Failed to parse food: " + e.getMessage());
            }});
        }
    }

    @PostMapping("/ai/parse-multiple-foods")
    public ResponseEntity<?> parseMultipleFoods(@RequestBody Map<String, String> request) {
        try {
            String foodText = request.get("text");
            if (!openAIService.isConfigured()) {
                return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                    put("error", "OpenAI API not configured");
                }});
            }
            List<Map<String, Object>> parsed = openAIService.parseMultipleFoods(foodText);
            if (parsed == null || parsed.isEmpty()) {
                return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                    put("error", "Failed to parse foods");
                }});
            }
            return ResponseEntity.ok(parsed);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                put("error", "Failed to parse foods: " + e.getMessage());
            }});
        }
    }

    @PostMapping("/ai/workout-plan")
    public ResponseEntity<?> getWorkoutPlan(@RequestBody Map<String, Object> request) {
        try {
            if (!openAIService.isConfigured()) {
                return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                    put("error", "AI not configured. Please add your OpenAI API key in Settings.");
                }});
            }
            @SuppressWarnings("unchecked")
            Map<String, String> answers = (Map<String, String>) request.get("answers");
            @SuppressWarnings("unchecked")
            Map<String, Object> profile = (Map<String, Object>) request.get("profile");
            @SuppressWarnings("unchecked")
            Map<String, Object> calorieTarget = (Map<String, Object>) request.get("calorieTarget");

            Map<String, Object> plan = openAIService.getWorkoutPlan(answers, profile, calorieTarget);
            if (plan == null) {
                return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                    put("error", "Failed to generate workout plan. Please try again.");
                }});
            }
            return ResponseEntity.ok(plan);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                put("error", "Error: " + e.getMessage());
            }});
        }
    }

    @PostMapping("/ai/ingredient-suggestions")
    public ResponseEntity<?> getIngredientBasedSuggestions(@RequestBody Map<String, String> request) {
        try {
            String ingredients = request.getOrDefault("ingredients", "");
            UserPreference pref = mealService.getOrCreatePreference();

            List<Map<String, Object>> suggestions;
            if (openAIService.isConfigured() && !ingredients.isEmpty()) {
                suggestions = openAIService.getIngredientsBasedSuggestions(ingredients, pref.getPreference(), pref.getDietStatus());
            } else if (openAIService.isConfigured()) {
                suggestions = openAIService.getIngredientsBasedSuggestions("", pref.getPreference(), pref.getDietStatus());
            } else {
                suggestions = openAIService.getDefaultSuggestions(ingredients, pref.getPreference());
            }

            if (suggestions == null || suggestions.isEmpty()) {
                return ResponseEntity.ok(openAIService.getDefaultSuggestions(ingredients, pref.getPreference()));
            }
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            UserPreference pref = mealService.getOrCreatePreference();
            return ResponseEntity.ok(openAIService.getDefaultSuggestions(request.getOrDefault("ingredients", ""), pref.getPreference()));
        }
    }

    @PostMapping("/parse-foods")
    public ResponseEntity<?> parseFoods(@RequestBody Map<String, String> request) {
        try {
            String text = request.get("text");
            if (text == null || text.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                    put("error", "Text cannot be empty");
                }});
            }

            FoodParser.ParseResult result = foodParser.parseMultipleFoods(text);

            Map<String, Object> response = new HashMap<>();
            response.put("foods", result.foods);
            response.put("notFound", result.notFound);
            response.put("debugLog", result.debugLog);
            response.put("count", result.foods.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                put("error", "Parse error: " + e.getMessage());
            }});
        }
    }

    @PostMapping("/calculate-calories")
    public ResponseEntity<?> calculateCalories(@RequestBody Map<String, Object> request) {
        try {
            int age = Integer.parseInt(request.get("age").toString());
            double weight = Double.parseDouble(request.get("weight").toString());
            double height = Double.parseDouble(request.get("height").toString());
            String gender = (String) request.getOrDefault("gender", "male");
            String activityLevel = (String) request.getOrDefault("activityLevel", "moderate");
            String goal = (String) request.get("goal");

            if (goal == null || goal.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                    put("error", "Goal is required (lose, gain, or maintain)");
                }});
            }

            CalorieCalculationService.CalorieTarget target = calorieCalculationService.calculateCalorieTarget(
                age, weight, height, gender, activityLevel, goal
            );

            return ResponseEntity.ok(target);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                put("error", "Calculation error: " + e.getMessage());
            }});
        }
    }
}