package com.ankitrainer.language;

import com.ankitrainer.language.enums.Language;
import com.ankitrainer.language.enums.PartOfSpeech;

public interface PartOfSpeechAnalyzer {

    /**
     * Checks whether the given word belongs to this part of speech.
     *
     * @param word the word to analyze (may contain HTML tags)
     * @return {@code true} if the word belongs to this part of speech,
     *         {@code false} otherwise
     */
    boolean matches(String word);

    /**
     * Returns the identifier for this part of speech.
     *
     * @return the part of speech enum (e.g., {@code key="verb", label="Verbs"}, {@code key="adjective", label="Adjectives"})
     */
    PartOfSpeech getPartOfSpeech();

    /**
     * Returns the language supported by this analyzer.
     *
     * @return language enum (e.g., {@code key="japanese", label="Japanese"}, {@code key="english", label="English"})
     */
    Language getLanguage();
}
