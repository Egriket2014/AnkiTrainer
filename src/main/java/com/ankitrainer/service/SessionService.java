package com.ankitrainer.service;

import com.ankitrainer.entity.CardSrsEntity;
import com.ankitrainer.entity.DeckConfigEntity;
import com.ankitrainer.language.enums.ConjugationType;
import com.ankitrainer.language.enums.PartOfSpeech;
import io.github.openspacedrepetition.CardAndReviewLog;
import io.github.openspacedrepetition.Rating;
import io.github.openspacedrepetition.Scheduler;
import io.github.openspacedrepetition.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.PriorityQueue;

@Service
public class SessionService {

    private static final Logger log = LoggerFactory.getLogger(SessionService.class);

    @Autowired
    private CardService cardService;
    @Autowired
    private DeckConfigService deckConfigService;
    @Autowired
    private Scheduler scheduler;

    // Session
    private PriorityQueue<CardSrsEntity> currentCards;
    private DeckConfigEntity currentDeckConfig;
    private ConjugationType currentConjugationType;
    private PartOfSpeech partOfSpeech;

    public void prepareCards(Long deckConfigId, PartOfSpeech partOfSpeech, ConjugationType conjugationType) {
        log.info("Preparing cards for deck: {}, partOfSpeech: {}, conjugation: {}",
                deckConfigId, partOfSpeech.getKey(), conjugationType.getKey());

        this.currentDeckConfig = deckConfigService.getDeckConfigById(deckConfigId);
        this.currentConjugationType = conjugationType;
        this.partOfSpeech = partOfSpeech;

        this.currentCards = loadQueue();
        log.info("Prepared {} cards for session", currentCards.size());
    }


    /*
        ТИП 1. Новая карточка - state=LEARNING, step=0
        ТИП 2. Новая, но которую уже видели сегодня - state=LEARNING, step!=0, lastReview=today
        ТИП 3. Новая, но которую уже видели не сегодня - state=LEARNING, step!=0, lastReview<today
        ТИП 4. Не новая, но которую уже увидели и ответили неправильно - state=RELEARNING
        ТИП 5. На повторение - state=REVIEW, due < now()

        1) Сначала запрашиваем все карточки типа 2. Если их больше new_limit, значит это не первый
           подход пользователя за сегодня и новых карточек типа 1 мы ему не даем (переходим сразу к пункту 3).
        2) Запрашиваем карточки типа 1 с лимитом new_limit - кол-во карт из пункта 1.
           Это наши новые карточки на сегодня.
        3) Запрашиваем все карточки типа 3.
        4) Запрашиваем все карточки типа 4.
        5) Запрашиваем все карточки типа 5 с лимитом < review_limit
     */
    private PriorityQueue<CardSrsEntity> loadQueue() {
        PriorityQueue<CardSrsEntity> queue = new PriorityQueue<>(CardSrsEntity.BY_DUE);

        String deckName = currentDeckConfig.getDeckName();
        ConjugationType conjugationType = currentConjugationType;
        int newLimit = currentDeckConfig.getNewLimit();
        int reviewLimit = currentDeckConfig.getReviewLimit();
        log.info("Filling queue for deckName: {}, partOfSpeech: {}, conjugationType: {}",
                deckName, partOfSpeech, conjugationType);

        // 1
        var type2 = cardService.findSeenTodayNewCards(deckName, conjugationType, LocalDate.now());
        queue.addAll(type2);
        log.debug("Loaded {} active new cards (type 2)", type2.size());

        // 2
        int remainingNewLimit = Math.max(0, newLimit - type2.size());
        if (remainingNewLimit > 0) {
            var type1 = cardService.findNewCardsForToday(deckName, conjugationType, remainingNewLimit);
            queue.addAll(type1);
            log.debug("Loaded {} fresh new cards (type 1)", type1.size());
        }

        // 3
        var type3 = cardService.findSeenNotTodayNewCards(deckName, conjugationType, LocalDate.now());
        queue.addAll(type3);
        log.debug("Loaded {} old new cards (type 3)", type3.size());

        // 4
        var type4 = cardService.findRelearningCards(deckName, conjugationType);
        queue.addAll(type4);
        log.debug("Loaded {} relearning cards (type 4)", type4.size());

        // 5
        var type5 = cardService.findReviewCards(deckName, conjugationType, reviewLimit);
        queue.addAll(type5);
        log.debug("Loaded {} review cards (type 5)", type5.size());

        log.info("Total queue size: {}", queue.size());
        return queue;
    }

    public CardSrsEntity getCurrentCard() {
        if (isComplete()) {
            return null;
        }
        return currentCards.peek();
    }

    public boolean isComplete() {
        return currentCards == null || currentCards.isEmpty();
    }

    @Transactional
    public boolean checkAnswer(String userAnswer) {
        if (isComplete()) {
            throw new IllegalStateException("Session is complete");
        }

        CardSrsEntity card = currentCards.poll();
        if (card == null) {
            throw new IllegalStateException("Queue is empty but session is not complete");
        }

        boolean isCorrect = card.getAnswer().equals(userAnswer.trim());
        Rating rating = isCorrect ? Rating.GOOD : Rating.AGAIN;

        log.debug("Checking answer for card {}: user='{}', expected='{}', correct={}",
                card.getCard().getWord(), userAnswer, card.getAnswer(), isCorrect);

        CardAndReviewLog review = scheduler.reviewCard(card.getSrsCard(), rating);
        card.setSrsCard(review.card());
        cardService.saveCardSrs(card);

        if (card.getState() == State.REVIEW) {
            log.debug("Card {} completed and moved to REVIEW, removing from queue", card.getAnswer());
        } else {
            currentCards.offer(card);
            log.debug("Card {} returned to queue with new due: {}, state: {}",
                    card.getAnswer(), review.card().getDue(), card.getState());
        }

        return isCorrect;
    }

    public void saveResults() {
        log.info("Saving session results...");
        clearSession();
    }

    private void clearSession() {
        if (currentCards != null) {
            currentCards.clear();
        }
        currentCards = null;
        currentDeckConfig = null;
        currentConjugationType = null;
    }
}
