package com.ankitrainer.service;

import com.ankitrainer.dto.session.QueueStatsDto;
import com.ankitrainer.entity.CardSrsEntity;
import com.ankitrainer.entity.DeckConfigEntity;
import com.ankitrainer.language.enums.ConjugationType;
import com.ankitrainer.language.enums.PartOfSpeech;
import com.ankitrainer.queue.SessionQueue;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    private SessionQueue currentQueue;
    private DeckConfigEntity currentDeckConfig;
    private Set<ConjugationType> currentConjugationTypes;
    private PartOfSpeech partOfSpeech;

    public void prepareCards(Long deckConfigId, PartOfSpeech partOfSpeech, Set<ConjugationType> conjugationTypes) {
        log.info("Preparing cards for deck: {}, partOfSpeech: {}, conjugations: {}",
                deckConfigId, partOfSpeech.getKey(), conjugationTypes);

        this.currentDeckConfig = deckConfigService.getDeckConfigById(deckConfigId);
        this.currentConjugationTypes = conjugationTypes;
        this.partOfSpeech = partOfSpeech;

        this.currentQueue = loadQueue();
    }

    private SessionQueue loadQueue() {
        log.info("Filling queue for deck: {}, partOfSpeech: {}, {} conjugation types",
                currentDeckConfig.getDeckName(), partOfSpeech, currentConjugationTypes.size());

        SessionQueue queue = new SessionQueue();
        for (ConjugationType conjugationType : currentConjugationTypes) {
            List<CardSrsEntity> cards = loadCards(conjugationType);
            log.debug("Loaded {} cards for conjugation: {}", cards.size(), conjugationType.getKey());
            queue.addAll(cards);
        }

        log.info("Total queue size: {}", queue.size());
        return queue;
    }

    /*
        ТИП 1. Новая карточка - state=LEARNING, lastReview=null
        ТИП 2. Новая, но которую уже видели сегодня - state=LEARNING, lastReview=today
        ТИП 3. Новая, но которую уже видели не сегодня - state=LEARNING, lastReview<today
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
    private List<CardSrsEntity> loadCards(ConjugationType conjugationType) {
        List<CardSrsEntity> cards = new ArrayList<>();

        String deckName = currentDeckConfig.getDeckName();
        int newLimit = currentDeckConfig.getNewLimit();
        int reviewLimit = currentDeckConfig.getReviewLimit();

        // 1
        var type2 = cardService.findSeenTodayNewCards(deckName, conjugationType, LocalDate.now());
        cards.addAll(type2);
        log.debug("Loaded {} active new cards (type 2)", type2.size());

        // 2
        int remainingNewLimit = Math.max(0, newLimit - type2.size());
        if (remainingNewLimit > 0) {
            var type1 = cardService.findNewCardsForToday(deckName, conjugationType, remainingNewLimit);
            cards.addAll(type1);
            log.debug("Loaded {} fresh new cards (type 1)", type1.size());
        }

        // 3
        var type3 = cardService.findSeenNotTodayNewCards(deckName, conjugationType, LocalDate.now());
        cards.addAll(type3);
        log.debug("Loaded {} old new cards (type 3)", type3.size());

        // 4
        var type4 = cardService.findRelearningCards(deckName, conjugationType);
        cards.addAll(type4);
        log.debug("Loaded {} relearning cards (type 4)", type4.size());

        // 5
        var type5 = cardService.findReviewCards(deckName, conjugationType, reviewLimit);
        cards.addAll(type5);
        log.debug("Loaded {} review cards (type 5)", type5.size());

        return cards;
    }

    public CardSrsEntity getCurrentCard() {
        if (isComplete()) {
            return null;
        }
        return currentQueue.peek();
    }

    public boolean isComplete() {
        return currentQueue == null || currentQueue.isEmpty();
    }

    public QueueStatsDto getQueueStats() {
        if (currentQueue == null) {
            return QueueStatsDto.builder()
                    .blue(0)
                    .red(0)
                    .green(0)
                    .build();
        }
        return currentQueue.stats();
    }

    public List<CardSrsEntity> getQueueCards() {
        if (currentQueue == null) {
            return List.of();
        }
        return currentQueue.peekAll();
    }

    @Transactional
    public boolean checkAnswer(String userAnswer) {
        if (isComplete()) {
            throw new IllegalStateException("Session is complete");
        }

        CardSrsEntity card = currentQueue.poll();
        if (card == null) {
            throw new IllegalStateException("Queue is empty but session is not complete");
        }

        boolean isCorrect = card.getAnswer().equals(userAnswer.trim());
        Rating rating = isCorrect ? Rating.GOOD : Rating.AGAIN;

        log.debug("Checking answer for card {}: user='{}', expected='{}', correct={}",
                card.getCard().getWord(), userAnswer, card.getAnswer(), isCorrect);

        log.debug("OLD SRS {}", card.getSrsCard().toJson());

        CardAndReviewLog review = scheduler.reviewCard(card.getSrsCard(), rating);
        card.updateSrs(review.card());

        log.debug("NEW SRS {}", card.getSrsCard().toJson());

        cardService.saveCardSrs(card);

        if (card.getState() == State.REVIEW) {
            log.debug("Card {} completed and moved to REVIEW, removing from queue", card.getAnswer());
        } else {
            currentQueue.add(card);
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
        currentQueue = null;
        currentDeckConfig = null;
        currentConjugationTypes = null;
    }
}
