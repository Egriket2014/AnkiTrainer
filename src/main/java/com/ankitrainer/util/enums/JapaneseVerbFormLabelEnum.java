package com.ankitrainer.util.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum JapaneseVerbFormLabelEnum {

    TE("te", "て-form"),
    POLITE("polite", "ます-form (polite)"),
    NEGATIVE("negative", "ない-form (negative)"),
    NEGATIVE_POLITE("negative_polite", "ません-form (negative polite)"),
    PAST("past", "た-form (past)"),
    PAST_POLITE("past_polite", "ました-form (past polite)"),
    PAST_NEGATIVE("past_negative", "なかった-form (past negative)"),
    PAST_NEGATIVE_POLITE("past_negative_polite", "ませんでした-form (past negative polite)");

    private static final Map<String, JapaneseVerbFormLabelEnum> map =
            Arrays.stream(values()).collect(Collectors.toMap(JapaneseVerbFormLabelEnum::getKey, p -> p));

    private final String key;
    private final String label;

    JapaneseVerbFormLabelEnum(String key, String label) {
        this.key = key;
        this.label = label;
    }

    public static String getLabel(String key) {
        return map.get(key).getLabel();
    }

    public static Set<String> getKeys() {
        return map.keySet();
    }
}
