package com.ankitrainer.service.language;

import com.ankitrainer.language.PartOfSpeechAnalyzer;
import com.ankitrainer.language.enums.Language;
import com.ankitrainer.language.enums.PartOfSpeech;
import com.ankitrainer.util.HtmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class LanguagePartOfSpeechAnalyzerService {

    private static final Logger log = LoggerFactory.getLogger(LanguagePartOfSpeechAnalyzerService.class);

    private final Map<Language, Map<PartOfSpeech, PartOfSpeechAnalyzer>> analyzerMap;

    public LanguagePartOfSpeechAnalyzerService(List<PartOfSpeechAnalyzer> analyzers) {
        this.analyzerMap = new HashMap<>();
        for (PartOfSpeechAnalyzer analyzer : analyzers) {
            Language language = analyzer.getLanguage();
            PartOfSpeech partOfSpeech = analyzer.getPartOfSpeech();

            Map<PartOfSpeech, PartOfSpeechAnalyzer> languageMap = analyzerMap.computeIfAbsent(
                    language,
                    k -> new HashMap<>()
            );

            PartOfSpeechAnalyzer existing = languageMap.put(partOfSpeech, analyzer);

            // Check duplicate part of speech for same language
            if (existing != null) {
                String errorMessage = String.format(
                        "Duplicate analyzer for language '%s' and part of speech '%s'. Existing: %s, New: %s.",
                        language.getKey(),
                        partOfSpeech.getKey(),
                        existing.getClass().getSimpleName(),
                        analyzer.getClass().getSimpleName()
                );
                log.error(errorMessage);
                throw new IllegalStateException(errorMessage);
            }
        }
    }

    public PartOfSpeech detectPartOfSpeech(String word, Language language) {
        String cleanedWord = HtmlUtils.cleanHtml(word);
        if (cleanedWord.isEmpty() || language == null) {
            return null;
        }

        List<PartOfSpeechAnalyzer> languageAnalyzers = getAnalyzers(language);
        for (PartOfSpeechAnalyzer analyzer : languageAnalyzers) {
            if (analyzer.matches(cleanedWord)) {
                log.debug("Word '{}' detected as '{}' by {}",
                        cleanedWord, analyzer.getPartOfSpeech(), analyzer.getClass().getSimpleName());
                return analyzer.getPartOfSpeech();
            }
        }

        log.debug("Word '{}' did not match any part of speech for language: {}", cleanedWord, language);
        return null;
    }

    public Set<PartOfSpeech> getSupportedPartsOfSpeech(Language language) {
        Map<PartOfSpeech, PartOfSpeechAnalyzer> languageMap = analyzerMap.get(language);
        if (languageMap == null) {
            return Set.of();
        }
        return languageMap.keySet();
    }

    private List<PartOfSpeechAnalyzer> getAnalyzers(Language language) {
        Map<PartOfSpeech, PartOfSpeechAnalyzer> languageMap = analyzerMap.get(language);
        if (languageMap == null) {
            return List.of();
        }
        return languageMap.values().stream().toList();
    }
}
