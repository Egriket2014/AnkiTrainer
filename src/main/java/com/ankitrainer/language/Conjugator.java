package com.ankitrainer.language;

import java.util.Set;

public interface Conjugator {

    /**
     * Conjugates a word into the specified form.
     *
     * @param word             the word in dictionary form
     * @param conjugationType  the type of conjugation (e.g., "past", "te", "polite")
     * @return the conjugated form, or null if conjugation fails
     */
    String conjugate(String word, String conjugationType);

    /**
     * Returns the language supported by this conjugator.
     *
     * @return language code (e.g., "japanese", "english")
     */
    String getLanguage();

    /**
     * Returns the part of speech supported by this conjugator.
     *
     * @return part of speech (e.g., "verb", "adjective")
     */
    String getPartOfSpeech();

    /**
     * Returns the supported conjugation types.
     *
     * @return supported types as set of strings (e.g., "past", "te", "polite")
     */
    Set<String> getSupportedConjugationTypes();
}
