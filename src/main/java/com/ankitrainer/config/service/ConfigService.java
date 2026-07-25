package com.ankitrainer.config.service;

import com.ankitrainer.config.model.ConfigData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class ConfigService {

    private static final Logger log = LoggerFactory.getLogger(ConfigService.class);

    @Value("${trainer.config.file:./trainer-config.json}")
    private String configFilePath;

    @Autowired
    private ObjectMapper objectMapper;

    private ConfigData currentConfig;

    public boolean hasSavedConfig() {
        return new File(configFilePath).exists();
    }

    public ConfigData loadConfig() {
        log.info("CURRENTCONFIG={},", currentConfig);
        if (currentConfig != null) {
            return currentConfig;
        }

        File configFile = new File(configFilePath);
        if (!configFile.exists()) {
            log.warn("Config file not found {}", configFilePath);
            return null;
        }

        try {
            ConfigData config = objectMapper.readValue(configFile, ConfigData.class);
            log.info("Configuration loaded from {} {}", configFilePath, config);
            return config;
        } catch (IOException e) {
            log.error("Failed to load config: {}", e.getMessage());
            return null;
        }
    }

    public void saveConfig(ConfigData config) {
        try {
            File configFile = new File(configFilePath);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(configFile, config);
            currentConfig = config;
            log.info("Configuration saved to: {}", configFilePath);
        } catch (IOException e) {
            log.error("Failed to save config: {}", e.getMessage());
            throw new RuntimeException("Failed to save configuration", e);
        }
    }
}
