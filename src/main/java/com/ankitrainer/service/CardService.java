package com.ankitrainer.service;

import com.ankitrainer.config.model.ConfigData;
import com.ankitrainer.config.service.ConfigService;
import com.ankitrainer.language.Conjugator;
import com.ankitrainer.model.CardDto;
import com.ankitrainer.service.anki.AnkiConnectService;
import com.ankitrainer.service.factory.ConjugatorFactory;
import io.github.openspacedrepetition.Card;
import io.github.openspacedrepetition.CardAndReviewLog;
import io.github.openspacedrepetition.Rating;
import io.github.openspacedrepetition.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
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
    @Autowired
    private Scheduler scheduler;

    // Session
    private String currentConjugationType;
    private String currentPartOfSpeech;
    private Conjugator currentConjugator;
    private String currentLanguage;
    private PriorityQueue<CardDto> currentCards = new PriorityQueue<>(CardDto.BY_DUE);

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

        // Cashing right answers + creating srs card
        for (CardDto card : selectedCards) {
            String expected = currentConjugator.conjugate(card.getWord(), conjugationType);
            if (expected == null) {
                throw new RuntimeException(
                        "Could not conjugate word '" + card.getWord() + "' " +
                                "for language " + currentLanguage + " and conjugation type " + currentConjugationType
                );
            }
            card.setExpectedAnswer(expected);
            card.setSrsCard(Card.builder().build());
        }

        // Reset session
        this.currentCards.clear();
        this.currentCards.addAll(selectedCards);

        log.info("Prepared {} {} cards for language: {}, conjugation: {}.",
                selectedCards.size(), partOfSpeech, currentLanguage, currentConjugationType);
    }

    /**
     * Returns the current card.
     *
     * @return null if session is complete
     */
    public CardDto getCurrentCard() {
        log.debug("Current cards queue size {}", currentCards.size());
        if (isComplete()) {
            return null;
        } else {
            return currentCards.peek();
        }
    }

    /**
     * Checks if the training session is complete.
     *
     * @return true if session is complete
     */
    public boolean isComplete() {
        return currentCards.isEmpty();
    }

    /**
     * Verifies the user's answer against the correct conjugation for current card.
     *
     * @param userAnswer   the user's input
     * @return true if the answer is correct, false otherwise
     */
    public boolean checkAnswer(String userAnswer) {
        CardDto card = currentCards.poll();
        if (card == null
            || userAnswer == null
            || currentConjugator == null
            || currentPartOfSpeech == null
            || card.getSrsCard() == null
        ) {
            throw new IllegalStateException(
                    "Cannot check answer: missing card, srsCard, answer, currentConjugator or currentPartOfSpeech"
            );
        }

        boolean isCorrect = card.getExpectedAnswer().equals(userAnswer.trim());

        // SRS update
        CardAndReviewLog review = scheduler.reviewCard(card.getSrsCard(), isCorrect ? Rating.GOOD : Rating.AGAIN);
        card.setSrsCard(review.card());
        log.debug("SRS updated. Word: {}, State: {}, Due: {}",
                card.getWord(), card.getSrsCard().getState(), card.getSrsCard().getDue());

        if (isCorrect) {
            log.debug("Correct! Word: {}, Expected: {}, UserAnswer: {}. Card removed from queue.",
                    card.getWord(), card.getExpectedAnswer(), userAnswer);
        } else {
            currentCards.offer(card);
            log.debug("Incorrect! Word: {}, Expected: {}, UserAnswer: {}. Returned to queue.",
                    card.getWord(), card.getExpectedAnswer(), userAnswer);
        }

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
    }
}
