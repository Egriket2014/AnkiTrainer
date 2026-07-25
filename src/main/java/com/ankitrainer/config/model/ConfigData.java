package com.ankitrainer.config.model;

import com.ankitrainer.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.ankitrainer.util.Constants.DEFAULT_SESSION_CARDS_LIMIT;

/**
 * Configuration object for the trainer.
 * Stored as JSON in trainer-config.json file.
 */
@Data
@NoArgsConstructor
public class ConfigData {
    
    private String deck = "";
    private String model = "";
    private String wordField = "";
    private String translationField = "";
    private String extraField = "";
    private Integer sessionCardsLimit = DEFAULT_SESSION_CARDS_LIMIT;
    private String language = Constants.DEFAULT_LANGUAGE;

    @JsonIgnore
    public boolean isComplete() {
        return !deck.isEmpty() &&
                !model.isEmpty() &&
                !wordField.isEmpty() &&
                !translationField.isEmpty();
    }
    
    @Override
    public String toString() {
        return "ConfigData{" +
                "deck='" + deck + '\'' +
                ", model='" + model + '\'' +
                ", wordField='" + wordField + '\'' +
                ", translationField='" + translationField + '\'' +
                ", extraField='" + extraField + '\'' +
                ", sessionCardsLimit='" + sessionCardsLimit + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}