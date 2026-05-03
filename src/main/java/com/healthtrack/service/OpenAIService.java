package com.healthtrack.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class OpenAIService {
    @Value("${openai.api.key:}")
    private String apiKey;

    private final OkHttpClient httpClient = new OkHttpClient();
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public void setApiKey(String key) {
        this.apiKey = key;
    }

    public boolean isConfigured() {
        return apiKey != null && !apiKey.isEmpty() && !apiKey.equals("demo");
    }

    public List<Map<String, Object>> getMealSuggestions(String userInput, String preferences, String ingredients) {
        if (!isConfigured()) {
            return getDefaultSuggestions();
        }

        try {
            String prompt = buildSuggestionPrompt(userInput, preferences, ingredients);
            String response = callOpenAI(prompt);
            return parseSuggestions(response);
        } catch (IOException e) {
            System.err.println("Error calling OpenAI: " + e.getMessage());
            return getDefaultSuggestions();
        }
    }

    public List<Map<String, Object>> parseMultipleFoods(String foodText) {
        if (!isConfigured()) {
            return null;
        }

        try {
            String prompt = "Extract all food items from this sentence and return as JSON array. " +
                    "For each food, extract: foodName, portion (if mentioned), unit (g/ml/piece/cup/tbsp, if mentioned), " +
                    "estimated calories, protein(g), carbs(g), fat(g). " +
                    "If portion is not mentioned, set portion to null. " +
                    "Return ONLY valid JSON array with objects containing: foodName, portion, unit, calories, protein, carbs, fat. " +
                    "Example: [{\"foodName\":\"Chicken\",\"portion\":null,\"unit\":null,\"calories\":165,...}] " +
                    "Food entry: " + foodText;
            String response = callOpenAI(prompt);
            return parseMultipleFoodsResponse(response);
        } catch (IOException e) {
            System.err.println("Error parsing food text: " + e.getMessage());
            return null;
        }
    }

    public Map<String, Object> parseFoodText(String foodText) {
        if (!isConfigured()) {
            return null;
        }

        try {
            String prompt = "Parse this food entry and extract: food name, portion, unit, estimated calories, protein(g), carbs(g), fat(g). " +
                    "Return as JSON with keys: foodName, portion, unit, calories, protein, carbs, fat. " +
                    "If portion not mentioned, set portion to null. " +
                    "Food entry: " + foodText;
            String response = callOpenAI(prompt);
            return parseJsonResponse(response);
        } catch (IOException e) {
            System.err.println("Error parsing food text: " + e.getMessage());
            return null;
        }
    }

    private String buildSuggestionPrompt(String userInput, String preferences, String ingredients) {
        return "Based on user input '" + userInput + "', dietary preference '" + preferences +
               "' and available ingredients '" + ingredients +
               "', suggest 5 healthy meal combinations. For each suggestion, provide: " +
               "meal name, estimated calories, brief description. Return as JSON array with objects containing: " +
               "name, calories, description";
    }

    private String callOpenAI(String prompt) throws IOException {
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", prompt);

        JsonArray messages = new JsonArray();
        messages.add(message);

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "gpt-3.5-turbo");
        requestBody.add("messages", messages);
        requestBody.addProperty("temperature", 0.7);
        requestBody.addProperty("max_tokens", 1000);

        RequestBody body = RequestBody.create(
                requestBody.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(OPENAI_API_URL)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("OpenAI API error: " + response.code() + " " + response.message());
            }

            String responseBody = response.body().string();
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
            return jsonResponse.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();
        }
    }

    private List<Map<String, Object>> parseSuggestions(String response) {
        List<Map<String, Object>> suggestions = new ArrayList<>();
        try {
            JsonArray array = JsonParser.parseString(response).getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                JsonObject obj = array.get(i).getAsJsonObject();
                Map<String, Object> suggestion = new HashMap<>();
                suggestion.put("name", obj.get("name").getAsString());
                suggestion.put("calories", obj.get("calories").getAsDouble());
                suggestion.put("description", obj.get("description").getAsString());
                suggestions.add(suggestion);
            }
        } catch (Exception e) {
            return getDefaultSuggestions();
        }
        return suggestions;
    }

    private Map<String, Object> parseJsonResponse(String response) {
        try {
            return new com.google.gson.Gson().fromJson(response, Map.class);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Map<String, Object>> getIngredientsBasedSuggestions(String ingredients, String preferences, String dietStatus) {
        if (!isConfigured()) {
            return getDefaultSuggestions(ingredients, preferences);
        }

        try {
            String prompt = buildIngredientsPrompt(ingredients, preferences, dietStatus);
            String response = callOpenAI(prompt);
            return parseSuggestions(response);
        } catch (IOException e) {
            System.err.println("Error getting ingredient-based suggestions: " + e.getMessage());
            return getDefaultSuggestions(ingredients, preferences);
        }
    }

    private String buildIngredientsPrompt(String ingredients, String preferences, String dietStatus) {
        String preferenceText = "all foods (omnivore)";
        String preferenceEmoji = "🍽️";

        if ("vegetarian".equals(preferences)) {
            preferenceText = "vegetarian (no meat, but eggs/dairy allowed)";
            preferenceEmoji = "🥬";
        } else if ("vegan".equals(preferences)) {
            preferenceText = "vegan (no animal products at all)";
            preferenceEmoji = "🌱";
        } else if ("highprotein".equals(preferences)) {
            preferenceText = "high protein for muscle building and fitness";
            preferenceEmoji = "💪";
        } else if ("lowcarb".equals(preferences)) {
            preferenceText = "low carb for weight management and blood sugar control";
            preferenceEmoji = "📉";
        }

        String ingredientInfo = ingredients.isEmpty() ?
            "No specific ingredients provided - suggest healthy general meals" :
            "Using these ingredients: " + ingredients;

        String vegetarianRule = "";
        if ("vegetarian".equals(preferences)) {
            vegetarianRule = "\n⚠️ CRITICAL: NO MEAT OR FISH! Only suggest vegetarian meals. No chicken, beef, fish, lamb, etc. Use eggs, dairy, legumes, vegetables.";
        } else if ("vegan".equals(preferences)) {
            vegetarianRule = "\n⚠️ CRITICAL: NO ANIMAL PRODUCTS AT ALL! Only suggest vegan meals. No meat, fish, eggs, dairy, honey. Use plants, legumes, nuts, seeds only.";
        } else if ("highprotein".equals(preferences)) {
            vegetarianRule = "\n⚠️ CRITICAL: MUST BE HIGH PROTEIN! Every meal must have 25+ grams of protein. Focus on protein sources like chicken, fish, eggs, paneer, lentils, chickpeas.";
        } else if ("lowcarb".equals(preferences)) {
            vegetarianRule = "\n⚠️ CRITICAL: MUST BE LOW CARB! Avoid rice, bread, pasta, potatoes. Focus on vegetables, meat, fish, eggs. Keep carbs under 20g per meal.";
        }

        String prompt = "You are a strict nutritionist and chef. STRICT RULES - you MUST follow these or your response is WRONG:\n\n" +
                        "🎯 Dietary Preference: " + preferenceEmoji + " " + preferenceText + vegetarianRule + "\n" +
                        "📊 Diet Status: " + dietStatus + "\n" +
                        "🥘 " + ingredientInfo + "\n\n" +
                        "SUGGEST 5 MEALS. For EACH meal:\n" +
                        "1. Name\n" +
                        "2. Calories\n" +
                        "3. Why it matches the preference and diet status\n\n" +
                        "MANDATORY RULES:\n" +
                        "- IF VEGETARIAN: ZERO meat, poultry, or fish. Only egg, dairy, vegetables, legumes, nuts\n" +
                        "- IF VEGAN: ZERO animal products. Only plants, legumes, nuts, seeds, oils\n" +
                        "- IF HIGH PROTEIN: Every meal 25+ grams protein\n" +
                        "- IF LOW CARB: Every meal under 20g carbs\n" +
                        "- ALL: Use provided ingredients if given\n" +
                        "- ALL: Relevant to diet status (help undereating users eat more, help junk food users eat clean, etc.)\n\n" +
                        "Return ONLY valid JSON: [{\"name\":\"meal\",\"calories\":400,\"description\":\"why\"}]\n" +
                        "NO OTHER TEXT. ONLY JSON.";
        return prompt;
    }

    private List<Map<String, Object>> parseMultipleFoodsResponse(String response) {
        List<Map<String, Object>> foods = new ArrayList<>();
        try {
            JsonArray array = JsonParser.parseString(response).getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                JsonObject obj = array.get(i).getAsJsonObject();
                Map<String, Object> food = new HashMap<>();
                food.put("foodName", obj.has("foodName") ? obj.get("foodName").getAsString() : "Unknown");
                food.put("portion", obj.has("portion") && !obj.get("portion").isJsonNull() ? obj.get("portion") : null);
                food.put("unit", obj.has("unit") && !obj.get("unit").isJsonNull() ? obj.get("unit").getAsString() : null);
                food.put("calories", obj.has("calories") ? obj.get("calories").getAsDouble() : 100.0);
                food.put("protein", obj.has("protein") ? obj.get("protein").getAsDouble() : 10.0);
                food.put("carbs", obj.has("carbs") ? obj.get("carbs").getAsDouble() : 15.0);
                food.put("fat", obj.has("fat") ? obj.get("fat").getAsDouble() : 5.0);
                foods.add(food);
            }
        } catch (Exception e) {
            System.err.println("Error parsing multiple foods: " + e.getMessage());
        }
        return foods;
    }

    public List<Map<String, Object>> getDefaultSuggestions(String ingredients, String preferences) {
        List<Map<String, Object>> suggestions = new ArrayList<>();
        preferences = preferences != null ? preferences : "all";

        String[][] allMeals = null;
        String[][] vegetarianMeals = {
            {"Paneer Tikka with Rice & Vegetables", "450", "High protein vegetarian meal with paneer for muscle building"},
            {"Palak Paneer with Chapati", "400", "Creamy spinach-paneer curry with Indian bread - traditional vegetarian favorite"},
            {"Chana Masala with Brown Rice", "420", "Spiced chickpea curry - nutrient-dense plant-based complete protein"},
            {"Dal Makhani with Roti", "380", "Creamy lentil curry with whole wheat bread - classic Indian vegetarian meal"},
            {"Vegetable Biryani with Raita", "430", "Aromatic rice dish with vegetables and yogurt - satisfying vegetarian feast"}
        };

        String[][] veganMeals = {
            {"Chana Masala with Brown Rice", "380", "Spiced chickpea curry - 100% plant-based complete protein"},
            {"Dal with Roti & Aloo Gobi", "400", "Lentil curry with flatbread and potato-cauliflower - traditional vegan meal"},
            {"Vegetable Biryani", "350", "Aromatic rice with vegetables - naturally vegan Indian specialty"},
            {"Rajma with Brown Rice", "370", "Kidney bean curry - hearty vegan plant-based protein source"},
            {"Tofu & Vegetable Stir Fry with Rice", "360", "Protein-rich tofu with fresh vegetables - modern vegan option"}
        };

        String[][] highProteinMeals = {
            {"Grilled Chicken Breast with Brown Rice & Broccoli", "450", "28g protein - High protein for muscle building and fitness"},
            {"Tandoori Chicken with Basmati Rice", "480", "32g protein - Indian spiced grilled chicken - perfect for muscle gains"},
            {"Paneer Tikka Masala with Rice", "470", "30g protein - High-protein vegetarian option with creamy sauce"},
            {"Tuna & Brown Rice Power Bowl", "420", "35g protein - Lean fish protein for muscle recovery and growth"},
            {"Grilled Fish with Dal & Rice", "430", "33g protein - Lean protein fish with lentils - double protein boost"}
        };

        String[][] lowCarbMeals = {
            {"Tandoori Chicken with Green Salad", "320", "16g carbs - Low-carb Indian spiced chicken - weight management favorite"},
            {"Grilled Fish with Aloo Gobi (Potato-less version)", "280", "12g carbs - Low-carb fish with roasted vegetables"},
            {"Paneer & Spinach Curry (No Rice)", "290", "14g carbs - Low-carb paneer with leafy greens"},
            {"Grilled Chicken with Cucumber & Tomato Salad", "300", "11g carbs - Simple low-carb protein meal"},
            {"Tandoori Paneer with Broccoli", "310", "13g carbs - Vegetarian low-carb high-protein Indian option"}
        };

        String[][] generalMeals = {
            {"Balanced Plate: Grilled Chicken with Brown Rice & Broccoli", "450", "High protein, balanced carbs and veggies for complete nutrition"},
            {"Healthy Salmon with Roasted Vegetables", "480", "Rich in omega-3s, excellent for heart and brain health"},
            {"Lentil & Vegetable Soup with Whole Grain Bread", "320", "Plant-based protein, fiber-rich, easy to digest"},
            {"Mediterranean Greek Salad with Grilled Fish", "380", "Loaded with antioxidants, healthy fats, and lean protein"},
            {"Chicken & Vegetable Stir Fry with Rice", "400", "Complete balanced meal with protein, carbs, and vegetables"}
        };

        if ("vegetarian".equals(preferences)) {
            allMeals = vegetarianMeals;
        } else if ("vegan".equals(preferences)) {
            allMeals = veganMeals;
        } else if ("highprotein".equals(preferences)) {
            allMeals = highProteinMeals;
        } else if ("lowcarb".equals(preferences)) {
            allMeals = lowCarbMeals;
        } else {
            allMeals = generalMeals;
        }

        for (String[] meal : allMeals) {
            Map<String, Object> suggestion = new HashMap<>();
            suggestion.put("name", meal[0]);
            suggestion.put("calories", Double.parseDouble(meal[1]));
            suggestion.put("description", meal[2]);
            suggestions.add(suggestion);
        }

        return suggestions;
    }

    private List<Map<String, Object>> getDefaultSuggestions() {
        return getDefaultSuggestions(null, "all");
    }
}
