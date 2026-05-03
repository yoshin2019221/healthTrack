package com.healthtrack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.healthtrack.model.FoodNutrition;

import java.util.*;
import java.util.regex.*;

@Service
public class FoodParser {
    @Autowired
    private FoodDatabase foodDatabase;

    // Word to number mappings
    private static final Map<String, Double> WORD_TO_NUM = Map.ofEntries(
        Map.entry("zero", 0.0), Map.entry("one", 1.0), Map.entry("two", 2.0),
        Map.entry("three", 3.0), Map.entry("four", 4.0), Map.entry("five", 5.0),
        Map.entry("six", 6.0), Map.entry("seven", 7.0), Map.entry("eight", 8.0),
        Map.entry("nine", 9.0), Map.entry("ten", 10.0), Map.entry("eleven", 11.0),
        Map.entry("twelve", 12.0), Map.entry("dozen", 12.0), Map.entry("half", 0.5),
        Map.entry("quarter", 0.25), Map.entry("a", 1.0), Map.entry("an", 1.0),
        Map.entry("couple", 2.0), Map.entry("few", 3.0), Map.entry("some", 1.0)
    );

    // Unit mappings
    private static final Map<String, String> UNIT_MAP = Map.ofEntries(
        Map.entry("g", "g"), Map.entry("gram", "g"), Map.entry("grams", "g"),
        Map.entry("gm", "g"), Map.entry("gms", "g"), Map.entry("kg", "kg"),
        Map.entry("kilogram", "kg"), Map.entry("kilograms", "kg"),
        Map.entry("ml", "ml"), Map.entry("milliliter", "ml"), Map.entry("milliliters", "ml"),
        Map.entry("millilitre", "ml"), Map.entry("l", "l"), Map.entry("liter", "l"),
        Map.entry("liters", "l"), Map.entry("litre", "l"), Map.entry("litres", "l"),
        Map.entry("oz", "oz"), Map.entry("ounce", "oz"), Map.entry("ounces", "oz"),
        Map.entry("lb", "lb"), Map.entry("pound", "lb"), Map.entry("pounds", "lb"),
        Map.entry("cup", "cup"), Map.entry("cups", "cup"), Map.entry("bowl", "bowl"),
        Map.entry("bowls", "bowl"), Map.entry("katori", "bowl"), Map.entry("piece", "piece"),
        Map.entry("pieces", "piece"), Map.entry("pc", "piece"), Map.entry("pcs", "piece"),
        Map.entry("no", "piece"), Map.entry("nos", "piece"), Map.entry("plate", "plate"),
        Map.entry("plates", "plate"), Map.entry("tbsp", "tbsp"), Map.entry("tablespoon", "tbsp"),
        Map.entry("tablespoons", "tbsp"), Map.entry("tbs", "tbsp"), Map.entry("tsp", "tsp"),
        Map.entry("teaspoon", "tsp"), Map.entry("teaspoons", "tsp"), Map.entry("glass", "glass"),
        Map.entry("glasses", "glass"), Map.entry("slice", "slice"), Map.entry("slices", "slice"),
        Map.entry("scoop", "scoop"), Map.entry("scoops", "scoop"), Map.entry("can", "can"),
        Map.entry("cans", "can"), Map.entry("bottle", "bottle"), Map.entry("bottles", "bottle"),
        Map.entry("serving", "serving"), Map.entry("servings", "serving"), Map.entry("portion", "serving"),
        Map.entry("portions", "serving"), Map.entry("handful", "handful"), Map.entry("handfuls", "handful")
    );

    // Stopwords to filter out
    private static final Set<String> STOPWORDS = Set.of(
        "i", "ive", "i've", "im", "i'm", "will", "have", "had", "has", "ate", "eat", "eating", "eaten",
        "took", "take", "taking", "drank", "drink", "drinking", "consumed", "consume", "the", "a", "an",
        "of", "for", "my", "me", "today", "yesterday", "breakfast", "lunch", "dinner", "snack", "brunch",
        "meal", "in", "at", "from", "just", "only", "also", "some", "bit", "little", "lot", "lots",
        "much", "many", "one", "piece", "pieces"
    );

    public static class ParsedFood {
        public String name;
        public Double portion;
        public String unit;
        public FoodNutrition food;
        public Boolean portionMissing;

        public ParsedFood(String name, Double portion, String unit, FoodNutrition food, Boolean portionMissing) {
            this.name = name;
            this.portion = portion;
            this.unit = unit;
            this.food = food;
            this.portionMissing = portionMissing;
        }
    }

    public static class ParseResult {
        public List<ParsedFood> foods;
        public List<String> notFound;
        public List<String> debugLog;

        public ParseResult() {
            this.foods = new ArrayList<>();
            this.notFound = new ArrayList<>();
            this.debugLog = new ArrayList<>();
        }
    }

    public ParseResult parseMultipleFoods(String text) {
        ParseResult result = new ParseResult();
        List<String> fragments = splitFragments(text);

        for (String fragment : fragments) {
            if (fragment.length() < 2) continue;

            ExtractedData extracted = extractPortionAndFood(fragment);
            if (extracted.foodName == null || extracted.foodName.isEmpty()) {
                result.debugLog.add("Skipped (no food): \"" + fragment + "\"");
                continue;
            }

            result.debugLog.add("Fragment: \"" + fragment + "\" -> food=\"" + extracted.foodName +
                    "\" portion=" + extracted.portion + " unit=" + extracted.unit);

            FoodMatch match = smartFoodSearch(extracted.foodName);
            if (match != null) {
                ParsedFood pf = new ParsedFood(
                    match.food.getName(),
                    extracted.portion,
                    extracted.unit != null ? extracted.unit : match.food.getBaseUnit(),
                    match.food,
                    extracted.portion == null
                );
                result.foods.add(pf);
                result.debugLog.add("  ✅ Matched \"" + match.food.getName() + "\" on \"" + match.matchedOn + "\"");
            } else {
                result.notFound.add(extracted.foodName);
                result.debugLog.add("  ❌ Not found: \"" + extracted.foodName + "\"");
            }
        }

        return result;
    }

    private List<String> splitFragments(String text) {
        String t = text.toLowerCase().trim();
        // Remove leading prefixes
        t = t.replaceAll("^(i\\s+(?:have\\s+had|had|have|ate|am\\s+having|will\\s+have)\\s+)", "");
        t = t.replaceAll("^(for\\s+(?:breakfast|lunch|dinner|snack)[,:;\\s]+)", "");

        // Replace connectors with pipe delimiter
        t = t.replaceAll("\\s+(and|with|plus|then|also|along\\s+with|as\\s+well\\s+as)\\s+", "|");
        t = t.replaceAll("[,;+&]", "|");

        List<String> fragments = new ArrayList<>();
        for (String part : t.split("\\|")) {
            String trimmed = part.trim();
            if (trimmed.length() > 0) {
                fragments.add(trimmed);
            }
        }
        return fragments;
    }

    private static class ExtractedData {
        Double portion;
        String unit;
        String foodName;
    }

    private ExtractedData extractPortionAndFood(String fragment) {
        ExtractedData result = new ExtractedData();

        String[] tokens = fragment.toLowerCase().split("\\s+");
        List<String> cleanTokens = new ArrayList<>();
        for (String t : tokens) {
            if (t.length() > 0 && !STOPWORDS.contains(t)) {
                cleanTokens.add(t);
            }
        }

        List<String> foodTokens = new ArrayList<>();
        result.portion = null;
        result.unit = null;

        for (int i = 0; i < cleanTokens.size(); i++) {
            String tok = cleanTokens.get(i);

            // Compound number+unit: "200g", "150ml"
            Pattern compoundPattern = Pattern.compile("^(\\d+(?:\\.\\d+)?)(g|gm|gms|kg|ml|l|oz|lb)$");
            Matcher compoundMatcher = compoundPattern.matcher(tok);
            if (compoundMatcher.matches()) {
                if (result.portion == null) {
                    result.portion = Double.parseDouble(compoundMatcher.group(1));
                }
                if (result.unit == null) {
                    String unitKey = compoundMatcher.group(2).toLowerCase();
                    result.unit = UNIT_MAP.get(unitKey);
                }
                continue;
            }

            // Fraction: "1/2", "3/4"
            if (tok.matches("^\\d+/\\d+$")) {
                Double value = parseFraction(tok);
                if (value != null && result.portion == null) {
                    result.portion = value;
                }
                continue;
            }

            // Number
            Double num = parseNumber(tok);
            if (num != null) {
                // Check for "one and a half" pattern
                if (i + 2 < cleanTokens.size() && "and".equals(cleanTokens.get(i + 1))) {
                    String nextTok = cleanTokens.get(i + 2);
                    if ("half".equals(nextTok)) {
                        if (result.portion == null) result.portion = num + 0.5;
                        i += 2;
                        continue;
                    } else if ("a".equals(nextTok) && i + 3 < cleanTokens.size() && "half".equals(cleanTokens.get(i + 3))) {
                        if (result.portion == null) result.portion = num + 0.5;
                        i += 3;
                        continue;
                    }
                }
                if (result.portion == null) result.portion = num;
                continue;
            }

            // Unit word
            if (isUnit(tok)) {
                if (result.unit == null) {
                    result.unit = UNIT_MAP.get(tok);
                }
                continue;
            }

            // Food name token
            foodTokens.add(tok);
        }

        result.foodName = String.join(" ", foodTokens);
        return result;
    }

    private Double parseNumber(String token) {
        if (token == null) return null;

        // Decimal or integer
        if (token.matches("^\\d+(\\.\\d+)?$")) {
            return Double.parseDouble(token);
        }

        // Word number
        if (WORD_TO_NUM.containsKey(token)) {
            return WORD_TO_NUM.get(token);
        }

        return null;
    }

    private Double parseFraction(String token) {
        Pattern fractionPattern = Pattern.compile("^(\\d+)/(\\d+)$");
        Matcher matcher = fractionPattern.matcher(token);
        if (matcher.matches()) {
            int numerator = Integer.parseInt(matcher.group(1));
            int denominator = Integer.parseInt(matcher.group(2));
            if (denominator != 0) {
                return (double) numerator / denominator;
            }
        }
        return null;
    }

    private boolean isUnit(String word) {
        return UNIT_MAP.containsKey(word.toLowerCase());
    }

    private static class FoodMatch {
        FoodNutrition food;
        String matchedOn;

        FoodMatch(FoodNutrition food, String matchedOn) {
            this.food = food;
            this.matchedOn = matchedOn;
        }
    }

    private FoodMatch smartFoodSearch(String foodName) {
        if (foodName == null || foodName.isEmpty()) return null;

        String[] words = foodName.split("\\s+");
        List<String> tries = new ArrayList<>();

        // TIER 1: Exact and very close matches (highest priority)
        // Try exact match first
        tries.add(foodName);

        // Try with adjectives removed (but keep word order)
        Set<String> adjectives = Set.of("raw", "boiled", "fried", "grilled", "steamed", "roasted", "baked", "cooked", "fresh", "dry", "dried", "spicy", "sweet", "salty", "sour", "bitter");
        List<String> nonAdj = new ArrayList<>();
        for (String w : words) {
            if (!adjectives.contains(w)) {
                nonAdj.add(w);
            }
        }
        if (nonAdj.size() > 0 && nonAdj.size() < words.length) {
            tries.add(String.join(" ", nonAdj));
        }

        // Singular/plural of FULL phrase only (not individual words)
        if (foodName.endsWith("s") && !foodName.endsWith("ss")) {
            tries.add(foodName.substring(0, foodName.length() - 1));
        }

        // TIER 2: Multi-word combinations (if multiple words)
        if (words.length >= 2) {
            // Last two words: "chicken curry" from "spicy chicken curry"
            tries.add(String.join(" ", Arrays.copyOfRange(words, words.length - 2, words.length)));
            // First two words: "chicken tikka" from "chicken tikka masala"
            tries.add(String.join(" ", Arrays.copyOfRange(words, 0, Math.min(2, words.length))));
        }

        // TIER 3: Single word fallback ONLY (avoid matching "dal" to "dalia")
        // Only try single words if multi-word search fails
        if (words.length >= 1) {
            String lastWord = words[words.length - 1];
            tries.add(lastWord);
            // Singular/plural of last word only
            if (lastWord.endsWith("s") && !lastWord.endsWith("ss")) {
                tries.add(lastWord.substring(0, lastWord.length() - 1));
            }
        }

        // TIER 4: First word only as last resort
        if (words.length >= 2) {
            tries.add(words[0]);
        }

        // Search with exact matching - only return if we have a good match
        for (String query : tries) {
            if (query == null || query.length() < 2) continue;

            List<FoodNutrition> matches = foodDatabase.searchFoods(query);
            if (!matches.isEmpty()) {
                FoodNutrition bestMatch = findBestMatch(matches, query);
                if (bestMatch != null) {
                    return new FoodMatch(bestMatch, query);
                }
            }
        }

        return null;
    }

    /* OLD LOGIC (commented out):
    // Find best match - prefer exact/prefix matches over substring matches
    private FoodNutrition findBestMatch(List<FoodNutrition> matches, String query) {
        String lowerQuery = query.toLowerCase();

        // Priority 1: Exact match (case-insensitive)
        for (FoodNutrition food : matches) {
            if (food.getName().toLowerCase().equals(lowerQuery)) {
                return food;
            }
        }

        // Priority 2: Starts with query (e.g., "rice" matches "Rice Flour" but prefer "Rice")
        for (FoodNutrition food : matches) {
            if (food.getName().toLowerCase().startsWith(lowerQuery)) {
                return food;
            }
        }

        // Priority 3: Contains as whole word (e.g., "chicken" in "Chicken Curry")
        for (FoodNutrition food : matches) {
            String[] foodWords = food.getName().toLowerCase().split("\s+");
            for (String word : foodWords) {
                if (word.equals(lowerQuery)) {
                    return food;
                }
            }
        }

        // Priority 4: Just return first match (this is the fallback)
        return matches.get(0);
    }
    */

    // NEW LOGIC: Find best match with default food preferences
    private FoodNutrition findBestMatch(List<FoodNutrition> matches, String query) {
        String lowerQuery = query.toLowerCase();

        // Priority 1: Exact match (case-insensitive)
        for (FoodNutrition food : matches) {
            if (food.getName().toLowerCase().equals(lowerQuery)) {
                return food;
            }
        }

        // Priority 2: Starts with query, prefer defaults (e.g., "rice" → "White Rice", "dal" → "Arhar Dal")
        FoodNutrition bestStartsWith = null;
        for (FoodNutrition food : matches) {
            if (food.getName().toLowerCase().startsWith(lowerQuery)) {
                if (shouldPreferDefault(food, lowerQuery)) {
                    return food;
                }
                if (bestStartsWith == null) {
                    bestStartsWith = food;
                }
            }
        }
        if (bestStartsWith != null) {
            return bestStartsWith;
        }

        // Priority 3: Contains as whole word (e.g., "chicken" in "Chicken Curry")
        for (FoodNutrition food : matches) {
            String[] foodWords = food.getName().toLowerCase().split("\s+");
            for (String word : foodWords) {
                if (word.equals(lowerQuery)) {
                    return food;
                }
            }
        }

        // Priority 4: Just return first match (this is the fallback)
        return matches.get(0);
    }

    private boolean shouldPreferDefault(FoodNutrition food, String query) {
        String foodName = food.getName().toLowerCase();
        
        // Default to White Rice when searching for "rice"
        if ("rice".equals(query) && foodName.equals("white rice")) {
            return true;
        }
        
        // Default to Dal (Arhar) when searching for "dal"
        if ("dal".equals(query) && foodName.equals("arhar dal (cooked)")) {
            return true;
        }
        
        // More defaults can be added here
        return false;
    }
}
