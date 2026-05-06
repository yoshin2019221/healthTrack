package com.healthtrack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/")
    public String index() { return "index"; }

    @GetMapping("/onboarding")
    public String onboarding() { return "onboarding"; }

    @GetMapping("/auth")
    public String auth() { return "auth"; }
}
