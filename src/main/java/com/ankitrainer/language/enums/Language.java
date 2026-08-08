package com.ankitrainer.language.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum Language {

    JAPANESE("japanese", "日本語 (Japanese)");
    // ENGLISH("english", "English");

    private final String key;
    private final String label;

    private static final Map<String, Language> map =
            Arrays.stream(values()).collect(Collectors.toMap(Language::getKey, l -> l));

    Language(String key, String label) {
        this.key = key;
        this.label = label;
    }

    public static Language get(String key) {
        Language language = map.get(key);
        if (language == null) {
            throw new IllegalArgumentException("Unknown language key: " + key);
        }
        return language;
    }

    @JsonCreator
    public static Language fromKey(String key) {
        Language language = map.get(key);
        if (language == null) {
            throw new IllegalArgumentException("Unknown language key: " + key);
        }
        return language;
    }

    @JsonValue
    public String getKey() {
        return key;
    }
}