package com.ankitrainer.service.language;

import com.ankitrainer.language.enums.ConjugationType;
import com.ankitrainer.language.enums.Language;
import com.ankitrainer.language.enums.PartOfSpeech;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class LanguageService {

    @Autowired
    private LanguagePartOfSpeechAnalyzerService languagePartOfSpeechAnalyzerService;
    @Autowired
    private LanguageConjugatorService languageConjugatorService;

    // Language
    public Set<Language> getSupportedLanguages() {
        return Set.of(Language.values());
    }

    // Part of Speech
    public PartOfSpeech detectPartOfSpeech(String word, Language language) {
        return languagePartOfSpeechAnalyzerService.detectPartOfSpeech(word, language);
    }

    public Set<PartOfSpeech> getSupportedPartsOfSpeech(Language language) {
        return languagePartOfSpeechAnalyzerService.getSupportedPartsOfSpeech(language);
    }

    // Conjugation
    public String conjugate(
            String word,
            Language language,
            PartOfSpeech partOfSpeech,
            ConjugationType conjugationType
    ) {
        return languageConjugatorService.conjugate(word, language, partOfSpeech, conjugationType);
    }

    public Set<ConjugationType> getSupportedConjugationTypes(Language language, PartOfSpeech partOfSpeech) {
        return languageConjugatorService.getSupportedConjugationTypes(language, partOfSpeech);
    }
}
