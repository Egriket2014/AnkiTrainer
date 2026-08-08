package com.ankitrainer.service;

import com.ankitrainer.entity.CardEntity;
import com.ankitrainer.entity.CardSrsEntity;
import com.ankitrainer.entity.DeckConfigEntity;
import com.ankitrainer.language.enums.ConjugationType;
import com.ankitrainer.repository.CardRepository;
import com.ankitrainer.repository.CardSrsRepository;
import com.ankitrainer.service.anki.AnkiConnectService;
import com.ankitrainer.service.language.LanguageService;
import io.github.openspacedrepetition.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CardService {

    private static final Logger log = LoggerFactory.getLogger(CardService.class);

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private CardSrsRepository cardSrsRepository;
    @Autowired
    private AnkiConnectService ankiConnectService;
    @Autowired
    private LanguageService languageService;

    @Transactional
    public void createCardsFromAnki(DeckConfigEntity deckConfig) {
        log.info("Creating cards for deck: {}", deckConfig.getDeckName());
        List<CardEntity> cardsFromAnki = ankiConnectService.getSupportedCardsForDeck(deckConfig);

        cardsFromAnki.forEach(card -> createCardWithConjugations(card, deckConfig));

        log.info("Finished creating cards for deck: {}", deckConfig.getDeckName());
    }

    @Transactional
    private void createCardWithConjugations(CardEntity card, DeckConfigEntity deckConfig) {
        if (cardRepository.existsByNoteId(card.getNoteId())) {
            log.warn("Card with noteId {} already exists, skipping creation", card.getNoteId());
            return;
        }

        card = cardRepository.saveAndFlush(card);
        log.debug("Created card {}. id={} noteId={}", card.getWord(), card.getId(), card.getNoteId());

        Set<ConjugationType> conjugationTypes = languageService.getSupportedConjugationTypes(
                deckConfig.getLanguage(),
                card.getPartOfSpeech()
        );

        List<CardSrsEntity> srsEntities = new ArrayList<>();
        for (ConjugationType conjugationType : conjugationTypes) {
            try {
                String conjugatedWord = languageService.conjugate(
                        card.getWord(),
                        deckConfig.getLanguage(),
                        card.getPartOfSpeech(),
                        conjugationType
                );

                if (conjugatedWord == null || conjugatedWord.isBlank()) {
                    log.warn("Failed to conjugate word '{}' for type: {}", card.getWord(), conjugationType.getKey());
                    continue;
                }

                CardSrsEntity srsEntity = CardSrsEntity.builder()
                        .card(card)
                        .conjugationType(conjugationType)
                        .answer(conjugatedWord)
                        .srsCard(Card.builder().build())
                        .build();

                srsEntities.add(srsEntity);
                log.debug("Created SRS for card: {} -> {}, conjugation: {}",
                        card.getWord(), srsEntity.getAnswer(), conjugationType.getKey());

            } catch (Exception e) {
                log.error("Failed to conjugate word '{}' for type: {}", card.getWord(), conjugationType.getKey(), e);
            }
        }

        if (!srsEntities.isEmpty()) {
            cardSrsRepository.saveAll(srsEntities);
            log.debug("Created {} conjugations for card: {}", srsEntities.size(), card.getWord());
        }
    }

    public List<CardSrsEntity> findSeenTodayNewCards(String deckName, ConjugationType conjugationType, LocalDate today) {
        return cardSrsRepository.findSeenTodayNewCards(
                deckName,
                conjugationType.getKey(),
                today.toString()
        );
    }

    public List<CardSrsEntity> findNewCardsForToday(String deckName, ConjugationType conjugationType, int limit) {
        return cardSrsRepository.findNewCardsForToday(
                deckName,
                conjugationType.getKey(),
                limit
        );
    }

    public List<CardSrsEntity> findSeenNotTodayNewCards(String deckName, ConjugationType conjugationType, LocalDate today) {
        return cardSrsRepository.findSeenNotTodayNewCards(
                deckName,
                conjugationType.getKey(),
                today.toString()
        );
    }

    public List<CardSrsEntity> findRelearningCards(String deckName, ConjugationType conjugationType) {
        return cardSrsRepository.findRelearningCards(
                deckName,
                conjugationType.getKey()
        );
    }

    public List<CardSrsEntity> findReviewCards(String deckName, ConjugationType conjugationType, int limit) {
        return cardSrsRepository.findReviewCards(
                deckName,
                conjugationType.getKey(),
                limit
        );
    }

    @Transactional
    public void saveCardSrs(CardSrsEntity entity) {
        cardSrsRepository.save(entity);
    }
}
