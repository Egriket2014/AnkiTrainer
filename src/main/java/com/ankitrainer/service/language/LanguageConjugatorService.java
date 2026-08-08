package com.ankitrainer.service.language;

import com.ankitrainer.language.Conjugator;
import com.ankitrainer.language.enums.ConjugationType;
import com.ankitrainer.language.enums.Language;
import com.ankitrainer.language.enums.PartOfSpeech;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class LanguageConjugatorService {

    private static final Logger log = LoggerFactory.getLogger(LanguageConjugatorService.class);

    private final Map<Language, Map<PartOfSpeech, Conjugator>> conjugators;

    public LanguageConjugatorService(List<Conjugator> conjugatorList) {
        this.conjugators = new HashMap<>();
        for (Conjugator conjugator : conjugatorList) {
            Language language = conjugator.getLanguage();
            PartOfSpeech partOfSpeech = conjugator.getPartOfSpeech();

            Map<PartOfSpeech, Conjugator> languageMap = conjugators.computeIfAbsent(
                    language,
                    k -> new HashMap<>()
            );

            Conjugator existing = languageMap.put(partOfSpeech, conjugator);

            // Check duplicate part of speech for same language
            if (existing != null) {
                String errorMessage = String.format(
                        "Duplicate conjugator for language '%s' and part of speech '%s'. Existing: %s, New: %s.",
                        language.getKey(),
                        partOfSpeech.getKey(),
                        existing.getClass().getSimpleName(),
                        conjugator.getClass().getSimpleName()
                );
                log.error(errorMessage);
                throw new IllegalStateException(errorMessage);
            }
        }
    }

    public String conjugate(
            String word,
            Language language,
            PartOfSpeech partOfSpeech,
            ConjugationType conjugationType
    ) {
        Conjugator conjugator = getConjugator(language, partOfSpeech);
        return conjugator.conjugate(word, conjugationType);
    }

    public Set<ConjugationType> getSupportedConjugationTypes(Language language, PartOfSpeech partOfSpeech) {
        Conjugator conjugator = getConjugator(language, partOfSpeech);
        return conjugator.getSupportedConjugationTypes();
    }

    private Conjugator getConjugator(Language language, PartOfSpeech partOfSpeech) {
        Map<PartOfSpeech, Conjugator> languageMap = conjugators.get(language);
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
}
