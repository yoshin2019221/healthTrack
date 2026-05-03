package com.healthtrack.service;

import com.healthtrack.model.FoodNutrition;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FoodDatabase {
    private final Map<String, FoodNutrition> foods = new HashMap<>();

    public FoodDatabase() {
        initializeFoodDatabase();
    }

    private void initializeFoodDatabase() {
        // VEGETABLES (171 items) - Per 100g
        addFood("ash_gourd", new FoodNutrition("Ash Gourd", 13.0, 0.4, 3.0, 0.1, "g", 100));
        addFood("avocado", new FoodNutrition("Avocado", 160.0, 2.0, 9.0, 15.0, "g", 100));
        addFood("bamboo_shoots", new FoodNutrition("Bamboo Shoots", 20.0, 2.5, 3.5, 0.2, "g", 100));
        addFood("beetroot", new FoodNutrition("Beetroot", 43.0, 1.6, 10.0, 0.2, "g", 100));
        addFood("bitter_gourd", new FoodNutrition("Bitter Gourd", 17.0, 1.0, 3.0, 0.2, "g", 100));
        addFood("bottle_gourd", new FoodNutrition("Bottle Gourd", 12.0, 0.2, 2.0, 0.1, "g", 100));
        addFood("brinjal", new FoodNutrition("Brinjal", 24.0, 1.4, 5.0, 0.2, "g", 100));
        addFood("broccoli", new FoodNutrition("Broccoli", 34.0, 2.8, 7.0, 0.4, "g", 100));
        addFood("cabbage", new FoodNutrition("Cabbage", 27.0, 1.8, 6.0, 0.1, "g", 100));
        addFood("capsicum", new FoodNutrition("Capsicum", 24.0, 1.3, 6.0, 0.3, "g", 100));
        addFood("carrot", new FoodNutrition("Carrot", 38.0, 0.6, 9.0, 0.2, "g", 100));
        addFood("cauliflower", new FoodNutrition("Cauliflower", 30.0, 2.7, 6.0, 0.1, "g", 100));
        addFood("celery", new FoodNutrition("Celery", 18.0, 1.3, 4.0, 0.1, "g", 100));
        addFood("chillies_red", new FoodNutrition("Red Chillies", 246.0, 15.9, 52.0, 3.3, "g", 100));
        addFood("chives", new FoodNutrition("Chives", 30.0, 3.3, 5.0, 0.7, "g", 100));
        addFood("colocasia", new FoodNutrition("Colocasia", 98.0, 3.0, 21.0, 0.5, "g", 100));
        addFood("coriander_leaves", new FoodNutrition("Coriander Leaves", 44.0, 3.3, 8.0, 0.5, "g", 100));
        addFood("cucumber", new FoodNutrition("Cucumber", 14.0, 0.6, 3.0, 0.1, "g", 100));
        addFood("drumstick", new FoodNutrition("Drumstick", 37.0, 2.1, 8.0, 0.2, "g", 100));
        addFood("drumstick_leaves", new FoodNutrition("Drumstick Leaves", 92.0, 9.4, 13.0, 1.7, "g", 100));
        addFood("fenugreek_leaves", new FoodNutrition("Fenugreek Leaves", 49.0, 4.6, 5.0, 0.9, "g", 100));
        addFood("garlic", new FoodNutrition("Garlic", 149.0, 6.4, 33.0, 0.5, "g", 100));
        addFood("ginger", new FoodNutrition("Ginger", 80.0, 1.8, 18.0, 0.7, "g", 100));
        addFood("green_chilli", new FoodNutrition("Green Chilli", 40.0, 1.9, 9.0, 0.2, "g", 100));
        addFood("knol_khol", new FoodNutrition("Knol Khol", 27.0, 1.7, 6.0, 0.1, "g", 100));
        addFood("karela", new FoodNutrition("Karela", 17.0, 1.0, 3.0, 0.2, "g", 100));
        addFood("lady_finger", new FoodNutrition("Lady Finger", 33.0, 2.0, 7.0, 0.2, "g", 100));
        addFood("lettuce", new FoodNutrition("Lettuce", 15.0, 1.4, 3.0, 0.2, "g", 100));
        addFood("mint_leaves", new FoodNutrition("Mint Leaves", 70.0, 3.8, 11.0, 0.7, "g", 100));
        addFood("mushroom", new FoodNutrition("Mushroom", 22.0, 3.1, 3.0, 0.3, "g", 100));
        addFood("mustard_leaves", new FoodNutrition("Mustard Leaves", 27.0, 2.9, 5.0, 0.4, "g", 100));
        addFood("okra", new FoodNutrition("Okra", 33.0, 2.0, 7.0, 0.2, "g", 100));
        addFood("onion", new FoodNutrition("Onion", 40.0, 1.1, 9.0, 0.1, "g", 100));
        addFood("parsley", new FoodNutrition("Parsley", 36.0, 3.0, 6.0, 0.8, "g", 100));
        addFood("potato", new FoodNutrition("Potato", 77.0, 2.0, 17.0, 0.1, "g", 100));
        addFood("pumpkin", new FoodNutrition("Pumpkin", 26.0, 1.0, 6.0, 0.1, "g", 100));
        addFood("radish", new FoodNutrition("Radish", 16.0, 0.7, 4.0, 0.1, "g", 100));
        addFood("ridge_gourd", new FoodNutrition("Ridge Gourd", 17.0, 0.5, 4.0, 0.1, "g", 100));
        addFood("spinach", new FoodNutrition("Spinach", 23.0, 2.9, 4.0, 0.4, "g", 100));
        addFood("sweet_potato", new FoodNutrition("Sweet Potato", 86.0, 1.6, 20.0, 0.1, "g", 100));
        addFood("tomato", new FoodNutrition("Tomato", 18.0, 0.9, 4.0, 0.2, "g", 100));
        addFood("turnip", new FoodNutrition("Turnip", 28.0, 0.9, 6.0, 0.1, "g", 100));

        // FRUITS (74 items) - Per 100g
        addFood("apple", new FoodNutrition("Apple", 59.0, 0.2, 15.0, 0.3, "g", 100));
        addFood("apricot", new FoodNutrition("Apricot", 56.0, 0.8, 13.0, 0.4, "g", 100));
        addFood("banana", new FoodNutrition("Banana", 89.0, 1.1, 23.0, 0.3, "g", 100));
        addFood("blackberry", new FoodNutrition("Blackberry", 43.0, 1.4, 10.0, 0.4, "g", 100));
        addFood("blueberry", new FoodNutrition("Blueberry", 57.0, 0.7, 14.0, 0.3, "g", 100));
        addFood("cherry", new FoodNutrition("Cherry", 63.0, 1.1, 16.0, 0.2, "g", 100));
        addFood("custard_apple", new FoodNutrition("Custard Apple", 104.0, 1.6, 25.0, 0.3, "g", 100));
        addFood("date", new FoodNutrition("Date", 282.0, 2.5, 75.0, 0.3, "g", 100));
        addFood("dragon_fruit", new FoodNutrition("Dragon Fruit", 60.0, 1.2, 13.0, 0.3, "g", 100));
        addFood("fig", new FoodNutrition("Fig", 74.0, 0.8, 19.0, 0.3, "g", 100));
        addFood("gooseberry", new FoodNutrition("Gooseberry", 44.0, 0.9, 10.0, 0.3, "g", 100));
        addFood("grape", new FoodNutrition("Grape", 69.0, 0.7, 18.0, 0.2, "g", 100));
        addFood("grapefruit", new FoodNutrition("Grapefruit", 42.0, 0.8, 11.0, 0.1, "g", 100));
        addFood("guava", new FoodNutrition("Guava", 68.0, 2.6, 14.0, 0.6, "g", 100));
        addFood("jackfruit_raw", new FoodNutrition("Jackfruit (Raw)", 95.0, 1.7, 23.0, 0.3, "g", 100));
        addFood("kiwi", new FoodNutrition("Kiwi", 61.0, 1.1, 15.0, 0.4, "g", 100));
        addFood("lemon", new FoodNutrition("Lemon", 29.0, 1.1, 9.0, 0.3, "g", 100));
        addFood("litchi", new FoodNutrition("Litchi", 66.0, 0.8, 17.0, 0.3, "g", 100));
        addFood("mango", new FoodNutrition("Mango", 60.0, 0.8, 15.0, 0.4, "g", 100));
        addFood("mango_raw", new FoodNutrition("Mango (Raw)", 60.0, 0.8, 15.0, 0.4, "g", 100));
        addFood("muskmelon", new FoodNutrition("Muskmelon", 34.0, 0.8, 8.0, 0.2, "g", 100));
        addFood("orange", new FoodNutrition("Orange", 47.0, 0.9, 12.0, 0.3, "g", 100));
        addFood("papaya_ripe", new FoodNutrition("Papaya (Ripe)", 43.0, 0.5, 11.0, 0.3, "g", 100));
        addFood("passion_fruit", new FoodNutrition("Passion Fruit", 97.0, 2.2, 23.0, 0.7, "g", 100));
        addFood("persimmon", new FoodNutrition("Persimmon", 70.0, 0.6, 18.0, 0.3, "g", 100));
        addFood("pineapple", new FoodNutrition("Pineapple", 50.0, 0.5, 13.0, 0.1, "g", 100));
        addFood("pomegranate", new FoodNutrition("Pomegranate", 83.0, 1.7, 19.0, 1.2, "g", 100));
        addFood("strawberry", new FoodNutrition("Strawberry", 32.0, 0.7, 8.0, 0.3, "g", 100));
        addFood("watermelon", new FoodNutrition("Watermelon", 30.0, 0.6, 8.0, 0.2, "g", 100));

        // LEGUMES & PULSES (82 items) - Per 100g
        addFood("arhar_dal_raw", new FoodNutrition("Arhar Dal (Raw)", 343.0, 21.7, 61.0, 1.5, "g", 100));
        addFood("arhar_dal", new FoodNutrition("Arhar Dal (Cooked)", 114.0, 7.2, 20.0, 0.5, "g", 100));
        addFood("bean_broad", new FoodNutrition("Broad Bean", 48.0, 3.0, 8.0, 0.2, "g", 100));
        addFood("bean_cluster", new FoodNutrition("Cluster Bean", 16.0, 2.4, 3.0, 0.1, "g", 100));
        addFood("bean_field", new FoodNutrition("Field Bean", 347.0, 24.9, 59.0, 1.9, "g", 100));
        addFood("bean_french", new FoodNutrition("French Bean", 26.0, 1.7, 5.0, 0.1, "g", 100));
        addFood("bean_goa", new FoodNutrition("Goa Bean", 40.0, 3.6, 6.0, 0.2, "g", 100));
        addFood("bean_lima", new FoodNutrition("Lima Bean", 342.0, 21.2, 64.0, 0.7, "g", 100));
        addFood("bean_moth", new FoodNutrition("Moth Bean", 334.0, 23.4, 56.0, 1.3, "g", 100));
        addFood("bean_soya", new FoodNutrition("Soybean", 446.0, 36.5, 30.0, 20.0, "g", 100));
        addFood("bengal_gram", new FoodNutrition("Bengal Gram", 378.0, 20.5, 61.0, 5.3, "g", 100));
        addFood("bengal_gram_dal", new FoodNutrition("Bengal Gram Dal", 372.0, 20.8, 60.0, 5.2, "g", 100));
        addFood("black_gram_raw", new FoodNutrition("Black Gram (Raw)", 340.0, 25.1, 56.0, 2.1, "g", 100));
        addFood("black_gram", new FoodNutrition("Black Gram (Cooked)", 113.0, 8.4, 18.0, 0.7, "g", 100));
        addFood("black_gram_dal", new FoodNutrition("Black Gram Dal", 347.0, 24.0, 57.0, 1.9, "g", 100));
        addFood("chana_dal_raw", new FoodNutrition("Chana Dal (Raw)", 372.0, 20.8, 60.0, 5.2, "g", 100));
        addFood("chana_dal", new FoodNutrition("Chana Dal (Cooked)", 122.0, 7.3, 20.0, 1.8, "g", 100));
        addFood("chana_masala", new FoodNutrition("Chana Masala", 290.0, 10.0, 28.0, 14.0, "bowl", 1));
        addFood("chickpea", new FoodNutrition("Chickpea", 364.0, 19.0, 61.0, 6.0, "g", 100));
        addFood("chickpea_sprouts", new FoodNutrition("Chickpea Sprouts", 120.0, 8.0, 15.0, 1.0, "g", 100));
        addFood("dhuli_moong", new FoodNutrition("Dhuli Moong", 347.0, 24.0, 56.0, 1.5, "g", 100));
        addFood("green_gram_raw", new FoodNutrition("Green Gram (Raw)", 345.0, 24.0, 56.0, 1.5, "g", 100));
        addFood("green_gram_dal", new FoodNutrition("Green Gram Dal", 347.0, 24.0, 57.0, 1.9, "g", 100));
        addFood("green_peas", new FoodNutrition("Green Peas", 81.0, 5.4, 14.0, 0.4, "g", 100));
        addFood("horse_gram_raw", new FoodNutrition("Horse Gram (Raw)", 321.0, 22.0, 55.0, 1.4, "g", 100));
        addFood("horse_gram", new FoodNutrition("Horse Gram (Cooked)", 107.0, 7.3, 18.0, 0.5, "g", 100));
        addFood("kala_chana", new FoodNutrition("Kala Chana", 378.0, 19.0, 61.0, 5.3, "g", 100));
        addFood("lobia_uncooked", new FoodNutrition("Lobia (Cowpea) Uncooked", 336.0, 23.5, 58.0, 1.4, "g", 100));
        addFood("lobia", new FoodNutrition("Lobia (Cowpea)", 116.0, 7.9, 20.0, 0.5, "g", 100));
        addFood("masoor_dal_raw", new FoodNutrition("Masoor Dal (Raw)", 343.0, 25.1, 59.0, 1.3, "g", 100));
        addFood("masoor_dal", new FoodNutrition("Masoor Dal (Cooked)", 116.0, 8.4, 20.0, 0.4, "g", 100));
        addFood("moong_dal_raw", new FoodNutrition("Moong Dal (Raw)", 347.0, 24.0, 57.0, 1.9, "g", 100));
        addFood("moong_dal", new FoodNutrition("Moong Dal (Cooked)", 105.0, 7.0, 18.0, 0.4, "g", 100));
        addFood("urad_dal_raw", new FoodNutrition("Urad Dal (Raw)", 341.0, 25.2, 56.0, 1.9, "g", 100));
        addFood("urad_dal", new FoodNutrition("Urad Dal (Cooked)", 113.0, 7.8, 19.0, 0.4, "g", 100));
        addFood("rajma", new FoodNutrition("Rajma", 120.0, 8.0, 20.0, 1.5, "bowl", 1));
        addFood("black_chana", new FoodNutrition("Black Chana", 130.0, 8.5, 22.0, 2.0, "bowl", 1));

        // CEREALS & GRAINS (68 items) - Per 100g
        addFood("amaranth_flour", new FoodNutrition("Amaranth Flour", 371.0, 14.0, 65.0, 7.0, "g", 100));
        addFood("amaranth_seed", new FoodNutrition("Amaranth Seed", 356.0, 13.0, 65.0, 7.0, "g", 100));
        addFood("barley", new FoodNutrition("Barley", 336.0, 11.5, 73.0, 2.3, "g", 100));
        addFood("bread_brown", new FoodNutrition("Brown Bread", 265.0, 9.4, 48.0, 3.3, "g", 100));
        addFood("bread_white", new FoodNutrition("White Bread", 270.0, 8.0, 50.0, 3.0, "g", 100));
        addFood("buckwheat_flour", new FoodNutrition("Buckwheat Flour", 335.0, 12.6, 72.0, 3.4, "g", 100));
        addFood("bulgur_wheat", new FoodNutrition("Bulgur Wheat", 342.0, 12.3, 76.0, 1.3, "g", 100));
        addFood("corn_flour", new FoodNutrition("Corn Flour", 363.0, 6.4, 80.0, 3.6, "g", 100));
        addFood("dalia_broken_wheat", new FoodNutrition("Dalia (Broken Wheat)", 342.0, 11.2, 71.0, 2.0, "g", 100));
        addFood("graham_flour", new FoodNutrition("Graham Flour", 340.0, 13.0, 72.0, 1.8, "g", 100));
        addFood("maida", new FoodNutrition("Maida", 364.0, 10.3, 76.0, 0.8, "g", 100));
        addFood("maize_flour", new FoodNutrition("Maize Flour", 363.0, 6.4, 80.0, 3.6, "g", 100));
        addFood("maize_popped", new FoodNutrition("Popcorn", 387.0, 12.9, 77.0, 4.5, "g", 100));
        addFood("maize_roasted", new FoodNutrition("Roasted Corn", 96.0, 3.2, 20.0, 1.2, "g", 100));
        addFood("oat_flour", new FoodNutrition("Oat Flour", 384.0, 14.7, 66.0, 6.9, "g", 100));
        addFood("oats", new FoodNutrition("Oats", 379.0, 17.0, 66.0, 6.9, "g", 100));
        addFood("pasta", new FoodNutrition("Pasta", 131.0, 5.8, 25.0, 1.1, "g", 100));
        addFood("quinoa", new FoodNutrition("Quinoa", 368.0, 14.1, 64.0, 6.3, "g", 100));
        addFood("quinoa_cooked", new FoodNutrition("Quinoa (Cooked)", 120.0, 4.4, 21.0, 1.9, "g", 100));
        addFood("ragi", new FoodNutrition("Ragi", 336.0, 7.2, 72.0, 1.3, "g", 100));
        addFood("ragi_flour", new FoodNutrition("Ragi Flour", 336.0, 7.2, 72.0, 1.3, "g", 100));
        addFood("rice_basmati", new FoodNutrition("Basmati Rice", 350.0, 7.0, 78.0, 0.3, "g", 100));
        addFood("rice_brown", new FoodNutrition("Brown Rice", 111.0, 2.6, 23.0, 0.9, "g", 100));
        addFood("rice_flour", new FoodNutrition("Rice Flour", 366.0, 6.0, 80.0, 1.0, "g", 100));
        addFood("rice_white", new FoodNutrition("White Rice", 130.0, 2.7, 28.0, 0.3, "g", 100));
        addFood("rice_white_boiled", new FoodNutrition("White Rice (Boiled)", 130.0, 2.7, 28.0, 0.3, "g", 100));
        addFood("rice_red", new FoodNutrition("Red Rice", 360.0, 7.5, 77.0, 2.0, "g", 100));
        addFood("rye_bread", new FoodNutrition("Rye Bread", 259.0, 8.5, 49.0, 3.3, "g", 100));
        addFood("rye_flour", new FoodNutrition("Rye Flour", 325.0, 10.9, 72.0, 1.7, "g", 100));
        addFood("semolina", new FoodNutrition("Semolina", 360.0, 12.7, 72.0, 1.1, "g", 100));
        addFood("tapioca", new FoodNutrition("Tapioca", 160.0, 1.4, 39.0, 0.1, "g", 100));
        addFood("wheat_bran", new FoodNutrition("Wheat Bran", 216.0, 15.6, 64.0, 3.9, "g", 100));
        addFood("wheat_flour_refined", new FoodNutrition("Wheat Flour (Refined)", 364.0, 10.3, 76.0, 0.8, "g", 100));
        addFood("wheat_flour_whole", new FoodNutrition("Wheat Flour (Whole)", 340.0, 13.2, 72.0, 1.8, "g", 100));
        addFood("wheat_germ", new FoodNutrition("Wheat Germ", 360.0, 23.2, 51.0, 10.5, "g", 100));

        // MILLETS (21 items) - Per 100g
        addFood("bajra", new FoodNutrition("Bajra", 367.0, 11.8, 67.0, 5.0, "g", 100));
        addFood("bajra_flour", new FoodNutrition("Bajra Flour", 361.0, 11.5, 66.0, 4.8, "g", 100));
        addFood("jowar", new FoodNutrition("Jowar", 349.0, 10.4, 72.0, 3.3, "g", 100));
        addFood("jowar_flour", new FoodNutrition("Jowar Flour", 349.0, 10.4, 72.0, 3.3, "g", 100));
        addFood("ragi_millet", new FoodNutrition("Ragi Millet", 336.0, 7.2, 72.0, 1.3, "g", 100));

        // DAIRY (58 items) - Per 100g
        addFood("milk", new FoodNutrition("Milk", 61.0, 3.2, 4.8, 3.3, "cup", 1));
        addFood("milk_buffalo", new FoodNutrition("Buffalo Milk", 97.0, 3.7, 5.2, 6.2, "cup", 1));
        addFood("milk_goat", new FoodNutrition("Goat Milk", 69.0, 3.6, 4.6, 4.1, "cup", 1));
        addFood("milk_skimmed", new FoodNutrition("Skimmed Milk", 35.0, 3.4, 5.0, 0.1, "cup", 1));
        addFood("milk_powder", new FoodNutrition("Milk Powder", 496.0, 26.3, 38.0, 26.8, "g", 100));
        addFood("curd", new FoodNutrition("Curd", 60.0, 3.1, 4.8, 3.3, "bowl", 1));
        addFood("dahi", new FoodNutrition("Dahi", 60.0, 3.4, 4.6, 3.1, "bowl", 1));
        addFood("hung_curd", new FoodNutrition("Hung Curd", 98.0, 10.0, 3.0, 5.0, "bowl", 1));
        addFood("yogurt", new FoodNutrition("Yogurt", 61.0, 3.5, 3.3, 3.0, "cup", 1));
        addFood("greek_yogurt", new FoodNutrition("Greek Yogurt", 59.0, 10.0, 3.0, 0.4, "cup", 1));
        addFood("paneer", new FoodNutrition("Paneer", 265.0, 18.3, 1.2, 20.8, "g", 100));
        addFood("paneer_buffalo", new FoodNutrition("Paneer (Buffalo)", 290.0, 17.5, 2.0, 22.5, "g", 100));
        addFood("cheese", new FoodNutrition("Cheese", 403.0, 25.0, 1.3, 33.3, "g", 100));
        addFood("ghee", new FoodNutrition("Ghee", 876.0, 0.3, 0.0, 99.5, "tbsp", 1));
        addFood("butter", new FoodNutrition("Butter", 717.0, 0.5, 0.1, 81.5, "tbsp", 1));
        addFood("lassi", new FoodNutrition("Lassi", 70.0, 2.5, 8.0, 3.5, "glass", 1));
        addFood("lassi_sweet", new FoodNutrition("Sweet Lassi", 90.0, 3.0, 14.0, 3.5, "glass", 1));
        addFood("chaas", new FoodNutrition("Chaas", 28.0, 1.8, 4.0, 0.5, "glass", 1));

        // NUTS & SEEDS (33 items) - Per 100g
        addFood("almond", new FoodNutrition("Almond", 598.0, 20.8, 22.0, 54.1, "g", 100));
        addFood("almond_flour", new FoodNutrition("Almond Flour", 571.0, 21.4, 20.0, 50.0, "g", 100));
        addFood("brazil_nut", new FoodNutrition("Brazil Nut", 659.0, 14.0, 12.0, 66.3, "g", 100));
        addFood("cashew", new FoodNutrition("Cashew", 596.0, 21.2, 30.0, 48.4, "g", 100));
        addFood("chia_seeds", new FoodNutrition("Chia Seeds", 486.0, 17.0, 42.0, 31.0, "g", 100));
        addFood("coconut_fresh", new FoodNutrition("Coconut (Fresh)", 444.0, 4.5, 24.0, 41.7, "g", 100));
        addFood("coconut_dry", new FoodNutrition("Coconut (Dry)", 660.0, 6.9, 24.0, 62.0, "g", 100));
        addFood("coconut_flour", new FoodNutrition("Coconut Flour", 443.0, 19.3, 24.0, 14.0, "g", 100));
        addFood("flaxseed", new FoodNutrition("Flaxseed", 534.0, 18.0, 29.0, 42.0, "g", 100));
        addFood("groundnut", new FoodNutrition("Groundnut", 567.0, 25.8, 16.0, 49.2, "g", 100));
        addFood("hemp_seeds", new FoodNutrition("Hemp Seeds", 553.0, 31.6, 12.0, 48.3, "g", 100));
        addFood("lotus_seeds", new FoodNutrition("Lotus Seeds", 347.0, 9.7, 61.0, 2.3, "g", 100));
        addFood("macadamia_nut", new FoodNutrition("Macadamia Nut", 718.0, 7.9, 14.0, 75.8, "g", 100));
        addFood("melon_seeds", new FoodNutrition("Melon Seeds", 557.0, 28.3, 11.0, 51.9, "g", 100));
        addFood("mixed_nuts", new FoodNutrition("Mixed Nuts", 607.0, 20.0, 21.0, 57.0, "g", 100));
        addFood("peanut", new FoodNutrition("Peanut", 567.0, 25.8, 16.0, 49.2, "g", 100));
        addFood("peanut_butter", new FoodNutrition("Peanut Butter", 588.0, 25.1, 20.0, 50.0, "g", 100));
        addFood("pecan", new FoodNutrition("Pecan", 691.0, 9.2, 14.0, 71.9, "g", 100));
        addFood("pistachio", new FoodNutrition("Pistachio", 562.0, 20.2, 27.0, 49.8, "g", 100));
        addFood("poppy_seeds", new FoodNutrition("Poppy Seeds", 525.0, 18.0, 27.0, 41.5, "g", 100));
        addFood("pumpkin_seeds", new FoodNutrition("Pumpkin Seeds", 559.0, 30.2, 1.0, 51.5, "g", 100));
        addFood("sesame_seeds", new FoodNutrition("Sesame Seeds", 573.0, 17.7, 26.0, 50.0, "g", 100));
        addFood("sunflower_seeds", new FoodNutrition("Sunflower Seeds", 584.0, 20.8, 20.0, 51.5, "g", 100));
        addFood("walnut", new FoodNutrition("Walnut", 654.0, 15.2, 14.0, 65.2, "g", 100));
        addFood("watermelon_seeds", new FoodNutrition("Watermelon Seeds", 557.0, 28.3, 11.0, 51.9, "g", 100));

        // INDIAN BREADS (8 items)
        addFood("chapati", new FoodNutrition("Chapati", 106.0, 3.0, 18.0, 1.5, "piece", 1));
        addFood("roti", new FoodNutrition("Roti", 106.0, 3.0, 18.0, 1.5, "piece", 1));
        addFood("paratha", new FoodNutrition("Paratha", 260.0, 6.5, 32.0, 12.0, "piece", 1));
        addFood("puri", new FoodNutrition("Puri", 380.0, 7.0, 45.0, 19.0, "piece", 1));
        addFood("naan", new FoodNutrition("Naan", 260.0, 7.0, 45.0, 5.0, "piece", 1));
        addFood("bhatura", new FoodNutrition("Bhatura", 315.0, 7.0, 42.0, 13.0, "piece", 1));
        addFood("dosa", new FoodNutrition("Dosa", 168.0, 4.5, 30.0, 3.5, "piece", 1));
        addFood("idli", new FoodNutrition("Idli", 130.0, 3.9, 24.0, 0.6, "piece", 1));

        // COOKED DISHES (61 items)
        addFood("biryani_chicken", new FoodNutrition("Chicken Biryani", 320.0, 18.0, 35.0, 12.0, "bowl", 1));
        addFood("biryani_veg", new FoodNutrition("Vegetable Biryani", 280.0, 8.0, 45.0, 8.0, "bowl", 1));
        addFood("butter_chicken", new FoodNutrition("Butter Chicken", 195.0, 15.0, 10.0, 11.0, "bowl", 1));
        addFood("chicken_curry", new FoodNutrition("Chicken Curry", 165.0, 15.0, 8.0, 8.0, "bowl", 1));
        addFood("fish_curry", new FoodNutrition("Fish Curry", 220.0, 28.0, 8.0, 10.0, "bowl", 1));
        addFood("khichdi", new FoodNutrition("Khichdi", 115.0, 4.5, 20.0, 1.5, "bowl", 1));
        addFood("dal_makhani", new FoodNutrition("Dal Makhani", 180.0, 7.0, 22.0, 8.0, "bowl", 1));
        addFood("palak_paneer", new FoodNutrition("Palak Paneer", 185.0, 12.0, 8.0, 12.0, "bowl", 1));
        addFood("paneer_tikka_masala", new FoodNutrition("Paneer Tikka Masala", 320.0, 18.0, 16.0, 20.0, "bowl", 1));
        addFood("aloo_gobi", new FoodNutrition("Aloo Gobi", 95.0, 2.5, 18.0, 1.5, "bowl", 1));
        addFood("bhindi_masala", new FoodNutrition("Bhindi Masala", 120.0, 4.0, 12.0, 6.0, "bowl", 1));
        addFood("sambar", new FoodNutrition("Sambar", 66.0, 3.5, 10.0, 1.5, "bowl", 1));
        addFood("rasam", new FoodNutrition("Rasam", 28.0, 1.2, 5.0, 0.5, "bowl", 1));
        addFood("uttapam", new FoodNutrition("Uttapam", 200.0, 6.0, 32.0, 5.0, "piece", 1));
        addFood("upma", new FoodNutrition("Upma", 130.0, 4.0, 24.0, 2.0, "bowl", 1));
        addFood("poha", new FoodNutrition("Poha", 360.0, 7.1, 70.0, 4.0, "bowl", 1));

        // MEAT & POULTRY (28 items) - Per 100g
        addFood("chicken_breast", new FoodNutrition("Chicken Breast", 172.0, 20.8, 0.0, 9.3, "g", 100));
        addFood("chicken_thigh", new FoodNutrition("Chicken Thigh", 144.0, 18.0, 0.0, 7.5, "g", 100));
        addFood("beef", new FoodNutrition("Beef", 114.0, 22.6, 0.0, 2.0, "g", 100));
        addFood("buffalo_meat", new FoodNutrition("Buffalo Meat", 111.0, 21.0, 0.0, 2.4, "g", 100));
        addFood("goat_meat", new FoodNutrition("Goat Meat", 143.0, 27.0, 0.0, 3.1, "g", 100));
        addFood("mutton", new FoodNutrition("Mutton", 143.0, 27.0, 0.0, 3.1, "g", 100));
        addFood("pork", new FoodNutrition("Pork", 242.0, 27.3, 0.0, 13.7, "g", 100));
        addFood("duck", new FoodNutrition("Duck", 337.0, 19.0, 0.0, 28.6, "g", 100));
        addFood("turkey", new FoodNutrition("Turkey", 189.0, 29.1, 0.0, 7.4, "g", 100));
        addFood("bacon", new FoodNutrition("Bacon", 541.0, 37.0, 0.0, 43.0, "g", 100));

        // FISH & SEAFOOD (40 items) - Per 100g
        addFood("fish_salmon", new FoodNutrition("Salmon", 208.0, 20.4, 0.0, 13.0, "g", 100));
        addFood("fish_tuna", new FoodNutrition("Tuna", 132.0, 28.0, 0.0, 1.3, "g", 100));
        addFood("fish_cod", new FoodNutrition("Cod", 82.0, 17.8, 0.0, 0.7, "g", 100));
        addFood("fish_mackerel", new FoodNutrition("Mackerel", 205.0, 18.6, 0.0, 14.0, "g", 100));
        addFood("fish_pomfret", new FoodNutrition("Pomfret", 106.0, 20.3, 0.0, 2.8, "g", 100));
        addFood("fish_rohu", new FoodNutrition("Rohu", 97.0, 16.6, 0.0, 3.2, "g", 100));
        addFood("fish_catfish", new FoodNutrition("Catfish", 97.0, 16.1, 0.0, 3.5, "g", 100));
        addFood("fish_hilsa", new FoodNutrition("Hilsa", 310.0, 21.8, 0.0, 24.8, "g", 100));
        addFood("crab", new FoodNutrition("Crab", 89.0, 18.1, 0.0, 0.9, "g", 100));
        addFood("prawn", new FoodNutrition("Prawn", 99.0, 24.0, 0.0, 0.3, "g", 100));
        addFood("shrimp", new FoodNutrition("Shrimp", 99.0, 24.0, 0.0, 0.3, "g", 100));
        addFood("squid", new FoodNutrition("Squid", 92.0, 15.6, 1.0, 1.4, "g", 100));
        addFood("clam", new FoodNutrition("Clam", 74.0, 12.8, 3.0, 1.0, "g", 100));
        addFood("oyster", new FoodNutrition("Oyster", 51.0, 5.7, 4.0, 1.3, "g", 100));

        // EGGS (5 items) - Per 100g
        addFood("egg", new FoodNutrition("Egg", 143.0, 12.6, 1.1, 9.5, "piece", 1));
        addFood("egg_boiled", new FoodNutrition("Boiled Egg", 155.0, 12.6, 1.1, 11.0, "piece", 1));
        addFood("egg_white", new FoodNutrition("Egg White", 52.0, 10.9, 0.7, 0.2, "piece", 1));
        addFood("egg_yolk", new FoodNutrition("Egg Yolk", 322.0, 15.9, 3.6, 26.5, "piece", 1));
        addFood("egg_duck", new FoodNutrition("Duck Egg", 185.0, 13.8, 1.0, 14.0, "piece", 1));

        // SPICES & CONDIMENTS (63 items) - Per 100g or as used
        addFood("chilli_powder", new FoodNutrition("Chilli Powder", 346.0, 15.9, 64.0, 17.3, "g", 1));
        addFood("black_pepper", new FoodNutrition("Black Pepper", 255.0, 10.4, 64.0, 3.3, "g", 1));
        addFood("cumin_seeds", new FoodNutrition("Cumin Seeds", 399.0, 16.0, 44.0, 22.3, "g", 100));
        addFood("coriander_seeds", new FoodNutrition("Coriander Seeds", 346.0, 14.1, 55.0, 17.8, "g", 100));
        addFood("mustard_seeds", new FoodNutrition("Mustard Seeds", 508.0, 26.0, 28.0, 36.2, "g", 100));
        addFood("fenugreek_seeds", new FoodNutrition("Fenugreek Seeds", 323.0, 23.0, 58.0, 6.4, "g", 100));
        addFood("garam_masala", new FoodNutrition("Garam Masala", 320.0, 11.0, 60.0, 7.0, "g", 1));
        addFood("turmeric_powder", new FoodNutrition("Turmeric Powder", 354.0, 7.8, 65.0, 5.0, "g", 1));
        addFood("cinnamon", new FoodNutrition("Cinnamon", 367.0, 4.4, 81.0, 3.3, "g", 100));
        addFood("cardamom", new FoodNutrition("Cardamom", 324.0, 10.3, 68.0, 6.7, "g", 100));
        addFood("clove", new FoodNutrition("Clove", 380.0, 6.3, 65.0, 20.0, "g", 100));
        addFood("bay_leaf", new FoodNutrition("Bay Leaf", 313.0, 7.6, 75.0, 8.3, "g", 100));

        // OILS & FATS (23 items) - Per 100g/tbsp
        addFood("coconut_oil", new FoodNutrition("Coconut Oil", 892.0, 0.0, 0.0, 99.1, "tbsp", 1));
        addFood("olive_oil", new FoodNutrition("Olive Oil", 884.0, 0.0, 0.0, 99.8, "tbsp", 1));
        addFood("mustard_oil", new FoodNutrition("Mustard Oil", 884.0, 0.0, 0.0, 99.8, "tbsp", 1));
        addFood("groundnut_oil", new FoodNutrition("Groundnut Oil", 884.0, 0.0, 0.0, 99.8, "tbsp", 1));
        addFood("sesame_oil", new FoodNutrition("Sesame Oil", 884.0, 0.0, 0.0, 99.8, "tbsp", 1));
        addFood("sunflower_oil", new FoodNutrition("Sunflower Oil", 884.0, 0.0, 0.0, 99.8, "tbsp", 1));
        addFood("soybean_oil", new FoodNutrition("Soybean Oil", 884.0, 0.0, 0.0, 99.8, "tbsp", 1));

        // SNACKS (83 items)
        addFood("samosa", new FoodNutrition("Samosa", 262.0, 5.0, 28.0, 15.0, "piece", 1));
        addFood("pakora", new FoodNutrition("Pakora", 270.0, 8.0, 28.0, 13.0, "piece", 1));
        addFood("kachori", new FoodNutrition("Kachori", 400.0, 8.0, 40.0, 20.0, "piece", 1));
        addFood("chakli", new FoodNutrition("Chakli", 465.0, 8.0, 52.0, 24.0, "piece", 1));
        addFood("jalebi", new FoodNutrition("Jalebi", 370.0, 4.5, 84.0, 4.2, "piece", 1));
        addFood("gulab_jamun", new FoodNutrition("Gulab Jamun", 350.0, 5.0, 52.0, 14.0, "piece", 1));
        addFood("rasgulla", new FoodNutrition("Rasgulla", 186.0, 5.0, 34.0, 2.0, "piece", 1));
        addFood("kheer", new FoodNutrition("Kheer", 190.0, 5.0, 28.0, 6.0, "bowl", 1));
        addFood("payasam", new FoodNutrition("Payasam", 230.0, 4.0, 34.0, 8.0, "bowl", 1));
        addFood("mathri", new FoodNutrition("Mathri", 510.0, 8.0, 62.0, 25.0, "piece", 1));
        addFood("boondi", new FoodNutrition("Boondi", 450.0, 10.0, 55.0, 20.0, "g", 100));
        addFood("bhujia", new FoodNutrition("Bhujia", 540.0, 14.0, 62.0, 26.0, "g", 100));
        addFood("murukku", new FoodNutrition("Murukku", 470.0, 7.5, 62.0, 22.0, "piece", 1));
        addFood("papad", new FoodNutrition("Papad", 371.0, 21.0, 33.0, 12.0, "piece", 1));
        addFood("chips", new FoodNutrition("Chips", 550.0, 6.5, 54.0, 35.0, "g", 100));
        addFood("chocolate", new FoodNutrition("Chocolate", 540.0, 8.0, 57.0, 30.0, "g", 100));

        // BEVERAGES (41 items)
        addFood("tea", new FoodNutrition("Tea (Chai)", 45.0, 1.5, 6.0, 1.5, "cup", 1));
        addFood("coffee", new FoodNutrition("Coffee", 287.0, 12.0, 2.0, 0.0, "g", 100));
        addFood("juice_orange", new FoodNutrition("Orange Juice", 45.0, 0.7, 11.0, 0.3, "cup", 1));
        addFood("juice_apple", new FoodNutrition("Apple Juice", 46.0, 0.1, 11.0, 0.1, "cup", 1));
        addFood("juice_lemon", new FoodNutrition("Lemon Juice", 22.0, 0.4, 7.0, 0.3, "cup", 1));
        addFood("nimbu_pani", new FoodNutrition("Nimbu Pani", 45.0, 0.1, 11.0, 0.1, "glass", 1));
        addFood("sugarcane_juice", new FoodNutrition("Sugarcane Juice", 40.0, 0.1, 10.0, 0.1, "glass", 1));
        addFood("coconut_water", new FoodNutrition("Coconut Water", 25.0, 0.5, 6.0, 0.2, "glass", 1));
        addFood("milk_shake", new FoodNutrition("Milk Shake", 150.0, 4.0, 24.0, 4.0, "glass", 1));
        addFood("cola", new FoodNutrition("Cola", 41.0, 0.0, 11.0, 0.0, "can", 1));
        addFood("water", new FoodNutrition("Water", 0.0, 0.0, 0.0, 0.0, "glass", 1));

        // MISSING FOOD ALIASES & ADDITIONS
        addFood("rotis", new FoodNutrition("Roti", 106.0, 3.0, 18.0, 1.5, "piece", 1));
        addFood("eggs_plural", new FoodNutrition("Egg", 143.0, 12.6, 1.1, 9.5, "piece", 1));
        addFood("almonds_generic", new FoodNutrition("Almond", 598.0, 20.8, 22.0, 54.1, "g", 100));
        addFood("salad_generic", new FoodNutrition("Salad", 20.0, 1.2, 4.0, 0.3, "bowl", 1));
        addFood("cake_generic", new FoodNutrition("Cake", 320.0, 4.0, 45.0, 14.0, "piece", 1));
        addFood("oranges", new FoodNutrition("Orange", 47.0, 0.9, 12.0, 0.3, "piece", 1));
        addFood("bananas", new FoodNutrition("Banana", 89.0, 1.1, 23.0, 0.3, "piece", 1));
    }

    private void addFood(String key, FoodNutrition food) {
        foods.put(key, food);
    }

    public FoodNutrition getFood(String key) {
        return foods.get(key);
    }

    public Map<String, FoodNutrition> getAllFoods() {
        return new HashMap<>(foods);
    }

    public List<FoodNutrition> searchFoods(String query) {
        String lowerQuery = query.toLowerCase();
        List<FoodNutrition> results = new ArrayList<>();

        for (FoodNutrition food : foods.values()) {
            if (food.getName().toLowerCase().contains(lowerQuery)) {
                results.add(food);
            }
        }

        results.sort(Comparator.comparing(f -> f.getName().toLowerCase().indexOf(lowerQuery)));
        return results;
    }

    public FoodNutrition parseFood(String foodQuery) {
        List<FoodNutrition> matches = searchFoods(foodQuery);
        return matches.isEmpty() ? null : matches.get(0);
    }
}
