package com.ankitrainer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/menu")
    public String menuPage() {
        return "menu";
    }

    @GetMapping("/config")
    public String configPage() {
        return "config";
    }

    @GetMapping("/card")
    public String cardPage() {
        return "card";
    }
}