package com.healthtrack.controller;

import com.healthtrack.entity.UserPreference;
import com.healthtrack.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class WebController {
    @Autowired
    private MealService mealService;

    @GetMapping("/")
    public String index(Model model) {
        UserPreference pref = mealService.getOrCreatePreference();
        Map<String, Double> summary = mealService.getDailySummary();

        model.addAttribute("meals", mealService.getTodayMeals());
        model.addAttribute("summary", summary);
        model.addAttribute("preference", pref);
        model.addAttribute("suggestions", mealService.getSuggestions(pref));

        return "index";
    }

    @GetMapping("/onboarding")
    public String onboarding() {
        return "onboarding";
    }
}
