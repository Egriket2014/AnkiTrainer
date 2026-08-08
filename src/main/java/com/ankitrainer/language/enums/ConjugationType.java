package com.ankitrainer.language.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum ConjugationType {

    JP_VERB_TE("jp_verb_te", "て-form"),
    JP_VERB_POLITE("jp_verb_polite", "ます-form (polite)"),
    JP_VERB_NEGATIVE("jp_verb_negative", "ない-form (negative)"),
    JP_VERB_NEGATIVE_POLITE("jp_verb_negative_polite", "ません-form (negative polite)"),
    JP_VERB_PAST("jp_verb_past", "た-form (past)"),
    JP_VERB_PAST_POLITE("jp_verb_past_polite", "ました-form (past polite)"),
    JP_VERB_PAST_NEGATIVE("jp_verb_past_negative", "なかった-form (past negative)"),
    JP_VERB_PAST_NEGATIVE_POLITE("jp_verb_past_negative_polite", "ませんでした-form (past negative polite)");

    private static final Map<String, ConjugationType> map =
            Arrays.stream(values()).collect(Collectors.toMap(ConjugationType::getKey, p -> p));

    private final String key;
    private final String label;

    ConjugationType(String key, String label) {
        this.key = key;
        this.label = label;
    }

    public static ConjugationType get(String key) {
        ConjugationType conjugationType = map.get(key);
        if (conjugationType == null) {
            throw new IllegalArgumentException("Unknown conjugationType key: " + key);
        }
        return conjugationType;
    }

    @JsonCreator
    public static ConjugationType fromKey(String key) {
        ConjugationType type = map.get(key);
        if (type == null) {
            throw new IllegalArgumentException("Unknown conjugationType key: " + key);
        }
        return type;
    }

    @JsonValue
    public String getKey() {
        return key;
    }
}
