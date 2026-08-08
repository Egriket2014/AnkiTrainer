package com.ankitrainer.language.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum PartOfSpeech {

    VERB("verb", "Verbs");
    // ADJECTIVE("adjective", "Adjectives")

    private final String key;
    private final String label;

    private static final Map<String, PartOfSpeech> map =
            Arrays.stream(values()).collect(Collectors.toMap(PartOfSpeech::getKey, p -> p));

    PartOfSpeech(String key, String label) {
        this.key = key;
        this.label = label;
    }

    public static PartOfSpeech get(String key) {
        PartOfSpeech partOfSpeech = map.get(key);
        if (partOfSpeech == null) {
            throw new IllegalArgumentException("Unknown partOfSpeech key: " + key);
        }
        return partOfSpeech;
    }

    @JsonCreator
    public static PartOfSpeech fromKey(String key) {
        PartOfSpeech pos = map.get(key);
        if (pos == null) {
            throw new IllegalArgumentException("Unknown partOfSpeech key: " + key);
        }
        return pos;
    }

    @JsonValue
    public String getKey() {
        return key;
    }
}
