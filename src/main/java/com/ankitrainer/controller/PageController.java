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

    @GetMapping("/config/new")
    public String newConfigPage() {
        return "config-new";
    }

    @GetMapping("/config/{id}")
    public String editConfigPage(@PathVariable Long id) {
        return "config-edit";
    }

    @GetMapping("/select-part")
    public String selectPartPage() {
        return "select-part";
    }

    @GetMapping("/select-conjugation")
    public String selectConjugationPage() {
        return "select-conjugation";
    }

    @GetMapping("/session")
    public String sessionPage() {
        return "session";
    }
}