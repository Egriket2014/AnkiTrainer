package com.ankitrainer.ui;

import com.ankitrainer.config.ConfigData;
import com.ankitrainer.model.CardDto;
import com.ankitrainer.service.AnkiConnectService;
import com.ankitrainer.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UiController {

    private static final Logger log = LoggerFactory.getLogger(UiController.class);

    @Autowired
    private AnkiConnectService service;

    @Autowired
    private ConfigService configService;

    @GetMapping("/")
    public String index() {
        if (configService.hasSavedConfig()) {
            return "redirect:/menu";
        } else {
            return "redirect:/config";
        }
    }

    @GetMapping("/menu")
    public String menuPage() {
        return "menu";
    }

    @GetMapping("/config")
    public String configPage(Model model) {
        model.addAttribute("decks", service.getDecksNames());
        model.addAttribute("allModels", service.getModelNames());

        ConfigData existingConfig = configService.loadConfig();
        if (existingConfig != null) {
            log.info("Config added to model: {}", existingConfig);
            model.addAttribute("config", existingConfig);
        } else {
            model.addAttribute("config", new ConfigData());
        }

        return "config";
    }

    @GetMapping("/trainer")
    public String trainerPage(Model model) {
        ConfigData config = configService.loadConfig();
        if (config == null || !config.isComplete()) {
            return "redirect:/config";
        }

        model.addAttribute("config", config);
        return "trainer";
    }

    @PostMapping("/api/config/save")
    @ResponseBody
    public ConfigData saveConfig(@RequestBody ConfigData config) {
        log.info("Saving configuration: {}", config);
        configService.saveConfig(config);
        return config;
    }

    @PostMapping("/api/config/reset")
    @ResponseBody
    public void resetConfig() {
        log.info("Resetting configuration");
        configService.saveConfig(null);
    }

    @GetMapping("/api/decks")
    @ResponseBody
    public List<String> getDecks() {
        return service.getDecksNames();
    }

    @GetMapping("/api/models")
    @ResponseBody
    public List<String> getModels() {
        return service.getModelNames();
    }

    @GetMapping("/api/fields")
    @ResponseBody
    public List<String> getFields(@RequestParam String modelName) {
        return service.getModelFieldNames(modelName);
    }

    @PostMapping("/api/cards/verbs")
    @ResponseBody
    public List<CardDto> getVerbs(
            @RequestParam String deckName,
            @RequestParam String modelName,
            @RequestParam String wordField,
            @RequestParam String translationField,
            @RequestParam(required = false) String extraField
    ) {
        return service.getVerbsByModelAndFields(deckName, modelName, wordField, translationField, extraField);
    }
}
