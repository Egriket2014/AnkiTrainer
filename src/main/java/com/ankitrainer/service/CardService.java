package com.ankitrainer.service;

import com.ankitrainer.config.model.ConfigData;
import com.ankitrainer.config.service.ConfigService;
import com.ankitrainer.language.Conjugator;
import com.ankitrainer.model.CardDto;
import com.ankitrainer.service.anki.AnkiConnectService;
import com.ankitrainer.service.factory.ConjugatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {

    private static final Logger log = LoggerFactory.getLogger(CardService.class);

    @Autowired
    private ConjugatorFactory conjugatorFactory;
    @Autowired
    private ConfigService configService;
    @Autowired
    private AnkiConnectService ankiConnectService;

    // Session
    private String currentConjugationType;
    private String currentPartOfSpeech;
    private Conjugator currentConjugator;
    private String currentLanguage;
    private List<CardDto> currentCards = new ArrayList<>();
    private int currentIndex = 0;

    /**
     * Prepares a shuffled, limited list of cards.
     *
     * @param partOfSpeech           the part of speech (e.g., "verb", "adjective")
     * @param conjugationType        the conjugation type (e.g., "past", "te")
     * @throws IllegalStateException if configuration is incomplete
     * @throws RuntimeException      if any word cannot be conjugated
     */
    public void prepareCards(String partOfSpeech, String conjugationType) {
        log.info("Preparing cards for training (partOfSpeech: {}, conjugationType: {})...", partOfSpeech, conjugationType);

        ConfigData config = configService.loadConfig();
        if (config == null || !config.isComplete()) {
            throw new IllegalStateException("Configuration is incomplete. Please set up your settings.");
        }

        this.currentPartOfSpeech = partOfSpeech.trim().toLowerCase();
        this.currentConjugationType = conjugationType.trim().toLowerCase();
        this.currentLanguage = config.getLanguage();
        this.currentConjugator = conjugatorFactory.getConjugator(currentLanguage, partOfSpeech);

        List<CardDto> allWords = ankiConnectService.getVerbsByModelAndFields(
                config.getDeck(),
                config.getModel(),
                config.getWordField(),
                config.getTranslationField(),
                config.getExtraField()
        );

        if (allWords.isEmpty()) {
            log.warn("No words found in the selected deck. Config: {}", config);
            return;
        }

        Collections.shuffle(allWords);
        List<CardDto> selectedCards = allWords.stream()
                .limit(config.getSessionCardsLimit())
                .collect(Collectors.toList());

        // Cashing right answers
        for (CardDto card : selectedCards) {
            String expected = currentConjugator.conjugate(card.getWord(), conjugationType);
            if (expected == null) {
                throw new RuntimeException(
                        "Could not conjugate word '" + card.getWord() + "' " +
                                "for language " + currentLanguage + " and conjugation type " + currentConjugationType
                );
            }
            card.setExpectedAnswer(expected);
        }

        // Reset session
        this.currentCards.clear();
        this.currentCards.addAll(selectedCards);
        this.currentIndex = 0;

        log.info("Prepared {} {} cards for language: {}, conjugation: {}.",
                selectedCards.size(), partOfSpeech, currentLanguage, currentConjugationType);
    }

    /**
     * Returns the current card.
     *
     * @return null if session is complete
     */
    public CardDto getCurrentCard() {
        if (isComplete()) {
            return null;
        } else {
            return currentCards.get(currentIndex);
        }
    }

    /**
     * Checks if the training session is complete.
     *
     * @return true if session is complete
     */
    public boolean isComplete() {
        return currentCards.isEmpty() || currentIndex >= currentCards.size();
    }

    /**
     * Verifies the user's answer against the correct conjugation for current card.
     *
     * @param userAnswer   the user's input
     * @return true if the answer is correct, false otherwise
     */
    public boolean checkAnswer(String userAnswer) {
        CardDto card = getCurrentCard();
        if (card == null || userAnswer == null || currentConjugator == null || currentPartOfSpeech == null) {
            throw new IllegalStateException(
                    "Cannot check answer: missing card, answer, currentConjugator or currentPartOfSpeech"
            );
        }

        boolean isCorrect = card.getExpectedAnswer().equals(userAnswer.trim());
        log.debug("Check result. Word: {}, Expected: {}, User: {}, Result: {}",
                card.getWord(), card.getExpectedAnswer(), userAnswer.trim(), isCorrect);

        currentIndex++;

        return isCorrect;
    }

    /**
     * Saves the results of the current session.
     */
    public void saveResults() {
        log.info("Saving results for current session...");
        // TODO Update cards statistics in DB

        clearSession();
    }

    public void clearSession() {
        currentConjugationType = null;
        currentPartOfSpeech = null;
        currentLanguage = null;
        currentConjugator = null;
        currentCards.clear();
        currentIndex = 0;
    }
}
