package com.ankitrainer.ui;

import com.ankitrainer.config.model.ConfigData;
import com.ankitrainer.config.service.ConfigService;
import com.ankitrainer.service.anki.AnkiConnectService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/config")
@RequiredArgsConstructor
public class ConfigController {

    private static final Logger log = LoggerFactory.getLogger(ConfigController.class);

    private final ConfigService configService;
    private final AnkiConnectService ankiConnectService;

    @GetMapping("/current")
    public ConfigData getCurrentConfig() {
        ConfigData config = configService.loadConfig();
        return config != null ? config : new ConfigData();
    }

    @PostMapping("/save")
    public ConfigData saveConfig(@RequestBody ConfigData config) {
        log.info("Saving configuration: {}", config);
        configService.saveConfig(config);
        return config;
    }

    @GetMapping("/decks")
    public List<String> getDecks() {
        return ankiConnectService.getDecksNames();
    }

    @GetMapping("/models")
    public List<String> getModels() {
        return ankiConnectService.getModelNames();
    }

    @GetMapping("/fields")
    public List<String> getFields(@RequestParam String modelName) {
        return ankiConnectService.getModelFieldNames(modelName);
    }
}