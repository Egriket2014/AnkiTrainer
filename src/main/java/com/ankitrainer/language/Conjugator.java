package com.ankitrainer.language;

import com.ankitrainer.language.enums.ConjugationType;
import com.ankitrainer.language.enums.Language;
import com.ankitrainer.language.enums.PartOfSpeech;

import java.util.Set;

public interface Conjugator {

    /**
     * Conjugates a word into the specified form.
     *
     * @param word             the word in dictionary form
     * @param conjugationType  the type of conjugation (e.g., "past", "te", "polite")
     * @return the conjugated form, or null if conjugation fails
     */
    String conjugate(String word, ConjugationType conjugationType);

    /**
     * Returns the language supported by this conjugator.
     *
     * @return language code (e.g., "japanese", "english")
     */
    Language getLanguage();

    /**
     * Returns the part of speech supported by this conjugator.
     *
     * @return part of speech (e.g., "verb", "adjective")
     */
    PartOfSpeech getPartOfSpeech();

    /**
     * Returns the supported conjugation types.
     *
     * @return supported types as set of strings (e.g., "past", "te", "polite")
     */
    Set<ConjugationType> getSupportedConjugationTypes();
}
