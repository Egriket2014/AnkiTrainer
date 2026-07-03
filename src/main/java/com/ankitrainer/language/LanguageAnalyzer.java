package com.ankitrainer.language;

public interface LanguageAnalyzer {

    /**
     * Determines whether the given word is a verb in its dictionary form.
     * @param word the word to analyze (may contain HTML tags)
     * @return {@code true} if the word is a verb in dictionary form, {@code false} otherwise
     */
    boolean isVerb(String word);

    /**
     * Returns the language code supported by this analyzer.
     * @return a language code (e.g., "japanese", "english", "russian")
     */
    String getLanguage();
}
