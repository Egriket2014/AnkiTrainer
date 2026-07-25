package com.ankitrainer.util.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum PartOfSpeechLabelEnum {

    VERB("verb", "Verbs");
    // ADJECTIVE("adjective", "Adjectives")

    private final String key;
    private final String label;

    private static final Map<String, PartOfSpeechLabelEnum> map =
            Arrays.stream(values()).collect(Collectors.toMap(PartOfSpeechLabelEnum::getKey, p -> p));

    PartOfSpeechLabelEnum(String key, String label) {
        this.key = key;
        this.label = label;
    }

    public static String getLabel(String key) {
        return map.get(key).getLabel();
    }
}
