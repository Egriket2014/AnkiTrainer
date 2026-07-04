package com.ankitrainer.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Configuration object for the trainer.
 * Stored as JSON in trainer-config.json file.
 */
@Data
@NoArgsConstructor
public class ConfigData {
    
    private String defaultDeck = "";
    private String defaultModel = "";
    private String wordField = "";
    private String translationField = "";
    private String extraField = "";

    @JsonIgnore
    public boolean isComplete() {
        return !defaultDeck.isEmpty() &&
                !defaultModel.isEmpty() &&
                !wordField.isEmpty() &&
                !translationField.isEmpty();
    }
    
    @Override
    public String toString() {
        return "ConfigData{" +
                "deck='" + defaultDeck + '\'' +
                ", model='" + defaultModel + '\'' +
                ", wordField='" + wordField + '\'' +
                ", translationField='" + translationField + '\'' +
                ", extraField='" + extraField + '\'' +
                '}';
    }
}