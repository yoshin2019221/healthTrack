#!/bin/bash

# Test Parser Script - 100 diverse food logging sentences
# Tests all possible formats: portions, units, connectors, natural language

BASE_URL="http://localhost:8080/api/parse-foods"
RESULTS_FILE="parser_test_results.txt"
MISSING_FOODS_FILE="missing_foods.txt"

> "$RESULTS_FILE"
> "$MISSING_FOODS_FILE"

echo "🔍 Testing Food Parser with 100 diverse sentences..."
echo "=================================================="

# Test cases covering all scenarios
declare -a TEST_SENTENCES=(
    # Basic with portions (numbers)
    "2 rotis"
    "3 eggs"
    "200 grams chicken"
    "1.5 cups rice"
    "0.5 bowl dal"

    # Decimal portions
    "2.5 bowls milk"
    "3.25 cups curd"
    "0.75 plate food"

    # Word numbers
    "one apple"
    "two oranges"
    "three bananas"
    "five almonds"
    "dozen eggs"

    # Fractions
    "1/2 cup milk"
    "3/4 bowl rice"
    "1/4 kg chicken"
    "2/3 plate dal"

    # "One and a half" patterns
    "one and a half cup milk"
    "one and a half bowl curd"
    "two and a half rotis"

    # Compound units (number+unit)
    "200g chicken"
    "150ml milk"
    "300gm paneer"
    "2kg rice"
    "500ml water"

    # Unit variations
    "1 bowl curd"
    "2 pieces bread"
    "3 cups milk"
    "4 tbsp butter"
    "5 tsp sugar"
    "6 glasses water"
    "7 plates food"
    "1 handful almonds"
    "2 scoops ice cream"
    "1 slice cake"

    # Without portions (just food names)
    "apple"
    "banana"
    "chicken"
    "rice"
    "bread"
    "milk"
    "curd"
    "paneer"
    "dal"
    "chapati"

    # Multiple foods with commas
    "2 rotis, 1 bowl dal, 200g chicken"
    "3 eggs, 1 apple, 1 glass milk"
    "100g paneer, 2 cups rice, 1 tomato"

    # Multiple foods with 'and'
    "2 rotis and 1 bowl dal and 200g chicken curry"
    "1 apple and 1 banana and 1 orange"
    "3 eggs and 2 slices bread and 1 cup milk"

    # Multiple foods with 'with'
    "2 rotis with 1 bowl dal with 200g chicken"
    "1 bowl curd with 1 apple"
    "3 eggs with bread and butter"

    # Multiple foods with 'plus'
    "1 cup rice plus 200g chicken plus 1 bowl curd"
    "2 rotis plus 1 bowl dal plus spinach"

    # Multiple foods with mixed connectors
    "1 bowl rice, 200g chicken with curd and apple"
    "2 rotis + 1 bowl dal + some curd"
    "3 eggs, 1 apple & 1 glass milk"
    "breakfast: 1 bowl rice, 1 egg with bread"

    # Natural language with descriptors
    "grilled chicken breast"
    "boiled eggs"
    "fried rice"
    "steamed broccoli"
    "roasted almonds"
    "raw apple"
    "cooked dal"
    "fresh milk"

    # Adjective + food
    "spicy chicken curry"
    "sweet mango"
    "sour yogurt"
    "salty chips"
    "bitter karela"

    # Indian specific variations
    "1 katori dal"
    "2 pieces paratha"
    "half bowl sambar"
    "1 dosa"
    "idli"
    "1 cup chai"

    # Casual/conversational format
    "i had 2 rotis"
    "i ate 3 eggs"
    "i drank 1 glass milk"
    "had 1 apple"
    "ate some rice"
    "took 2 cups coffee"

    # Meal time prefixes
    "breakfast: 2 rotis and 1 cup milk"
    "lunch: 1 plate biryani"
    "dinner: 200g chicken with rice"
    "snack: handful almonds"

    # Complex sentences
    "I had 2 rotis with 1 bowl dal, 200g chicken curry plus some curd and 1 apple"
    "For breakfast: 3 eggs, 2 slices bread with butter, coffee and orange juice"
    "lunch: 1 cup rice + 250g paneer tikka + 1 bowl salad"
    "dinner with family: 1.5 cups biryani, 200g chicken, curd, and 2 glasses lassi"
    "snacking: handful almonds, 1 apple, 1 banana, 1 glass milk"

    # Range/approximate portions
    "about 200g chicken"
    "roughly 2 cups rice"
    "around 1 bowl dal"
    "few eggs"
    "couple rotis"
    "some milk"
    "lot of salad"
)

COUNT=0
FOUND=0
NOT_FOUND=0

# Run tests
for sentence in "${TEST_SENTENCES[@]}"; do
    COUNT=$((COUNT + 1))

    # Call the API
    RESPONSE=$(curl -s -X POST "$BASE_URL" \
        -H "Content-Type: application/json" \
        -d "{\"text\":\"$sentence\"}")

    # Extract results
    FOODS_COUNT=$(echo "$RESPONSE" | grep -o '"count":[0-9]*' | grep -o '[0-9]*')
    NOT_FOUND_LIST=$(echo "$RESPONSE" | grep -o '"notFound":\[[^]]*\]')
    DEBUG_LOG=$(echo "$RESPONSE" | grep -o '"debugLog":\[[^]]*\]')

    # Log results
    echo "Test $COUNT: \"$sentence\"" >> "$RESULTS_FILE"
    echo "  Found: $FOODS_COUNT foods" >> "$RESULTS_FILE"
    echo "  Response: $RESPONSE" >> "$RESULTS_FILE"
    echo "" >> "$RESULTS_FILE"

    # Track missing foods
    if echo "$NOT_FOUND_LIST" | grep -q '\w'; then
        MISSING=$(echo "$NOT_FOUND_LIST" | sed 's/.*"notFound":\[//; s/\].*//' | tr ',' '\n' | sed 's/"//g' | tr -d ' ')
        for food in $MISSING; do
            if [ ! -z "$food" ]; then
                echo "$food" >> "$MISSING_FOODS_FILE"
                NOT_FOUND=$((NOT_FOUND + 1))
            fi
        done
    else
        FOUND=$((FOUND + 1))
    fi

    # Progress indicator
    if [ $((COUNT % 10)) -eq 0 ]; then
        echo "✓ Completed $COUNT tests..."
    fi
done

echo ""
echo "=================================================="
echo "📊 Test Results Summary"
echo "=================================================="
echo "Total Tests: $COUNT"
echo "Successful (all foods found): $FOUND"
echo "With Missing Foods: $NOT_FOUND"
echo ""
echo "Results saved to: $RESULTS_FILE"
echo "Missing foods saved to: $MISSING_FOODS_FILE"
echo ""

# Deduplicate and show missing foods
echo "🔍 Unique Missing Foods:"
sort "$MISSING_FOODS_FILE" | uniq -c | sort -rn
