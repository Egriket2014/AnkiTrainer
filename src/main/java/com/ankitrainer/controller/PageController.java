package com.ankitrainer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

    @GetMapping("/menu")
    public String menuPage() {
        return "menu";
    }

    @GetMapping("/settings")
    public String settingsPage() {
        return "settings";
    }

    @GetMapping("/config/new")
    public String newConfigPage() {
        return "config-new";
    }

    @GetMapping("/config/{id}")
    public String editConfigPage(@PathVariable Long id) {
        return "config-edit";
    }

    @GetMapping("/select-training")
    public String selectTrainingPage() {
        return "select-training";
    }

    @GetMapping("/session")
    public String sessionPage() {
        return "session";
    }
}