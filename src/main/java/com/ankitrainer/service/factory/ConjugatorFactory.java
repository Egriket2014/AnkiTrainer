package com.ankitrainer.service.factory;

import com.ankitrainer.language.Conjugator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class ConjugatorFactory {

    // Map: language -> (partOfSpeech -> Conjugator)
    private final Map<String, Map<String, Conjugator>> conjugators;

    public ConjugatorFactory(List<Conjugator> conjugatorList) {
        this.conjugators = new HashMap<>();
        for (Conjugator conjugator : conjugatorList) {
            String language = conjugator.getLanguage();
            String partOfSpeech = conjugator.getPartOfSpeech();

            conjugators.computeIfAbsent(language, k -> new HashMap<>()).put(partOfSpeech, conjugator);
        }
    }

    public Conjugator getConjugator(String language, String partOfSpeech) {
        Map<String, Conjugator> languageMap = conjugators.get(language);
        if (languageMap == null) {
            throw new IllegalArgumentException("No conjugators found for language: " + language);
        }

        Conjugator conjugator = languageMap.get(partOfSpeech);
        if (conjugator == null) {
            throw new IllegalArgumentException(
                    "No conjugator found for language: " + language + ", part of speech: " + partOfSpeech
            );
        }

        return conjugator;
    }

    public Set<String> getSupportedPartsOfSpeech(String language) {
        Map<String, Conjugator> languageMap = conjugators.get(language);
        if (languageMap == null) {
            return Set.of();
        }
        return languageMap.keySet();
    }

    public Set<String> getSupportedConjugationTypes(String language, String partOfSpeech) {
        Conjugator conjugator = getConjugator(language, partOfSpeech);
        return conjugator.getSupportedConjugationTypes();
    }
}
