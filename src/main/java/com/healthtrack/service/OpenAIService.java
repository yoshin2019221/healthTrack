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

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .build();
    private static final String OPENAI_API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String MODEL = "llama-3.3-70b-versatile";

    public void setApiKey(String key) {
        // strip any non-ASCII characters that would break the HTTP Authorization header
        this.apiKey = key == null ? "" : key.replaceAll("[^\\x20-\\x7E]", "").trim();
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

    public Map<String, Object> getCombinedSuggestions(String preferences, String dietStatus, String ingredients) {
        Map<String, Object> result = new HashMap<>();
        if (!isConfigured()) {
            result.put("ingredientBased", ingredients != null && !ingredients.isEmpty()
                    ? getDefaultSuggestions(ingredients, preferences)
                    : new ArrayList<>());
            result.put("general", getDefaultSuggestions(null, preferences));
            return result;
        }
        try {
            String ingredientPrompt = buildCombinedPrompt(preferences, dietStatus, ingredients);
            String response = callOpenAI(ingredientPrompt, 2000);
            return parseCombinedSuggestions(response, preferences, dietStatus, ingredients);
        } catch (IOException e) {
            System.err.println("Error getting combined suggestions: " + e.getMessage());
            result.put("ingredientBased", getDefaultSuggestions(ingredients, preferences));
            result.put("general", getDefaultSuggestions(null, preferences));
            return result;
        }
    }

    private String buildCombinedPrompt(String preferences, String dietStatus, String ingredients) {
        String prefText = "all foods (omnivore)";
        if ("vegetarian".equals(preferences)) prefText = "vegetarian (no meat, eggs/dairy ok)";
        else if ("vegan".equals(preferences)) prefText = "vegan (no animal products)";
        else if ("highprotein".equals(preferences)) prefText = "high protein (25g+ per meal)";
        else if ("lowcarb".equals(preferences)) prefText = "low carb (under 20g carbs per meal)";

        boolean hasIngredients = ingredients != null && !ingredients.trim().isEmpty();

        return "You are a professional Indian nutritionist. Suggest healthy Indian meals.\n\n" +
               "Dietary preference: " + prefText + "\n" +
               "Diet status: " + (dietStatus != null ? dietStatus : "ontrack") + "\n" +
               (hasIngredients ? "Home ingredients available: " + ingredients + "\n" : "") +
               "\nReturn ONLY valid JSON (no markdown) with this structure:\n" +
               "{\n" +
               (hasIngredients ?
               "  \"ingredientBased\": [\n" +
               "    {\"name\": \"Indian meal name\", \"calories\": 400, \"description\": \"brief why it fits + which ingredients used\"}\n" +
               "  ],\n" : "") +
               "  \"general\": [\n" +
               "    {\"name\": \"Indian meal name\", \"calories\": 400, \"description\": \"brief why it fits their diet goal\"}\n" +
               "  ]\n" +
               "}\n" +
               (hasIngredients ? "Provide 4 ingredient-based Indian meals and 5 general Indian meal suggestions.\n" : "Provide 6 general Indian meal suggestions.\n") +
               "All meals must be Indian cuisine. Follow dietary restrictions strictly.";
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseCombinedSuggestions(String response, String preferences, String dietStatus, String ingredients) {
        Map<String, Object> result = new HashMap<>();
        try {
            String clean = response.trim();
            if (clean.startsWith("```")) {
                clean = clean.replaceAll("^```[a-z]*\\n?", "").replaceAll("```$", "").trim();
            }
            Map<String, Object> parsed = new com.google.gson.Gson().fromJson(clean, Map.class);
            result.put("ingredientBased", parsed.getOrDefault("ingredientBased", new ArrayList<>()));
            result.put("general", parsed.getOrDefault("general", new ArrayList<>()));
        } catch (Exception e) {
            System.err.println("Failed to parse combined suggestions: " + e.getMessage());
            result.put("ingredientBased", getDefaultSuggestions(ingredients, preferences));
            result.put("general", getDefaultSuggestions(null, preferences));
        }
        return result;
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
        return buildIngredientsPrompt(ingredients, preferences, userInput);
    }

    private String callOpenAI(String prompt) throws IOException {
        return callOpenAI(prompt, 1000);
    }

    private String callOpenAI(String prompt, int maxTokens) throws IOException {
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", prompt);

        JsonArray messages = new JsonArray();
        messages.add(message);

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", MODEL);
        requestBody.add("messages", messages);
        requestBody.addProperty("temperature", 0.7);
        requestBody.addProperty("max_tokens", maxTokens);

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

    public Map<String, Object> getWorkoutPlan(Map<String, String> answers, Map<String, Object> profile, Map<String, Object> calorieTarget) {
        if (!isConfigured()) {
            return null;
        }
        try {
            String prompt = buildWorkoutPrompt(answers, profile, calorieTarget);
            String response = callOpenAI(prompt, 4000);
            return parseWorkoutPlan(response);
        } catch (IOException e) {
            System.err.println("Error getting workout plan: " + e.getMessage());
            return null;
        }
    }

    private String buildWorkoutPrompt(Map<String, String> answers, Map<String, Object> profile, Map<String, Object> calorieTarget) {
        String goal = calorieTarget != null && calorieTarget.get("goal") != null ? calorieTarget.get("goal").toString() : "maintain";
        String targetCal = calorieTarget != null && calorieTarget.get("targetCalories") != null ? calorieTarget.get("targetCalories").toString() : "2000";
        String age = profile != null && profile.get("age") != null ? profile.get("age").toString() : "unknown";
        String weight = profile != null && profile.get("weight") != null ? profile.get("weight").toString() : "unknown";
        String height = profile != null && profile.get("height") != null ? profile.get("height").toString() : "unknown";
        String gender = profile != null && profile.get("gender") != null ? profile.get("gender").toString() : "unknown";

        return "You are an expert personal trainer and fitness coach. Create a detailed personalized workout plan.\n\n" +
               "USER PROFILE:\n" +
               "- Age: " + age + ", Weight: " + weight + "kg, Height: " + height + "cm, Gender: " + gender + "\n" +
               "- Goal: " + goal + " (Calorie target: " + targetCal + " kcal/day)\n\n" +
               "ANSWERS FROM USER:\n" +
               "- Days per week: " + answers.getOrDefault("daysPerWeek", "4") + "\n" +
               "- Gym access: " + answers.getOrDefault("gymAccess", "gym") + "\n" +
               "- Experience level: " + answers.getOrDefault("experience", "intermediate") + "\n" +
               "- Injuries/limitations: " + answers.getOrDefault("injuries", "none") + "\n" +
               "- Session duration: " + answers.getOrDefault("sessionDuration", "45-60 minutes") + "\n" +
               "- Equipment: " + answers.getOrDefault("equipment", "full gym") + "\n" +
               "- Focus area: " + answers.getOrDefault("specificFocus", "overall balanced") + "\n" +
               "- Additional notes: " + answers.getOrDefault("additionalNotes", "none") + "\n\n" +
               "Return ONLY valid JSON (no markdown, no extra text) with this exact structure:\n" +
               "{\n" +
               "  \"goalTitle\": \"short motivational goal title\",\n" +
               "  \"summary\": \"2-3 sentence plan overview\",\n" +
               "  \"frequency\": \"e.g. 4 days/week\",\n" +
               "  \"sessionDuration\": \"e.g. 45-60 minutes\",\n" +
               "  \"trainingFocus\": \"e.g. Strength + Cardio\",\n" +
               "  \"weeklySchedule\": {\n" +
               "    \"Monday\": [\"Exercise 1 - sets x reps\", \"Exercise 2 - sets x reps\"],\n" +
               "    \"Tuesday\": [\"...\"],\n" +
               "    \"Wednesday\": [\"...\"],\n" +
               "    \"Thursday\": [\"...\"],\n" +
               "    \"Friday\": [\"...\"],\n" +
               "    \"Saturday\": [\"...\"],\n" +
               "    \"Sunday\": [\"...\"]\n" +
               "  },\n" +
               "  \"exercises\": [\n" +
               "    {\"name\": \"Exercise Name\", \"category\": \"strength|cardio|flexibility\", \"sets\": \"3 sets\", \"reps\": \"10-12 reps\", \"notes\": \"tip\"}\n" +
               "  ],\n" +
               "  \"nutritionTip\": \"one specific nutrition advice relevant to their goal\",\n" +
               "  \"coachMessage\": \"short motivating message to the user\"\n" +
               "}";
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseWorkoutPlan(String response) {
        try {
            String clean = response.trim();
            if (clean.startsWith("```")) {
                clean = clean.replaceAll("^```[a-z]*\\n?", "").replaceAll("```$", "").trim();
            }
            return new com.google.gson.Gson().fromJson(clean, Map.class);
        } catch (Exception e) {
            System.err.println("Failed to parse workout plan JSON: " + e.getMessage());
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
