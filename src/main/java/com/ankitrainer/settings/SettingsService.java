package com.ankitrainer.settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class SettingsService {

    private static final Logger log = LoggerFactory.getLogger(SettingsService.class);

    private static final String SETTINGS_FILE = System.getProperty("user.dir") + "/settings.json";

    private final ObjectMapper objectMapper;
    private AppSettings settings;

    public SettingsService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void load() {
        Path path = Paths.get(SETTINGS_FILE);

        if (!Files.exists(path)) {
            this.settings = AppSettings.defaults();
            persist();
            log.info("Settings file not found, created {} with defaults", SETTINGS_FILE);
            return;
        }

        try {
            this.settings = objectMapper.readValue(path.toFile(), AppSettings.class);
            if (this.settings == null) {
                this.settings = AppSettings.defaults();
            }
        } catch (IOException e) {
            log.error("Failed to read settings from {}, using defaults", SETTINGS_FILE, e);
            this.settings = AppSettings.defaults();
        }
    }

    public AppSettings getSettings() {
        return settings;
    }

    public synchronized AppSettings saveSettings(AppSettings newSettings) {
        this.settings = newSettings;
        persist();
        return this.settings;
    }

    private void persist() {
        Path path = Paths.get(SETTINGS_FILE);
        try {
            Files.createDirectories(path.getParent());
            objectMapper.writeValue(path.toFile(), settings);
            log.info("Settings saved to {}", SETTINGS_FILE);
        } catch (IOException e) {
            log.error("Failed to save settings to {}", SETTINGS_FILE, e);
        }
    }
}