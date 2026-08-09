package com.ankitrainer.queue;

import com.ankitrainer.entity.CardSrsEntity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public enum CardType {

    TYPE_1, TYPE_2, TYPE_3, TYPE_4, TYPE_5;

    public static CardType classify(CardSrsEntity card) {
        return switch (card.getState()) {
            case LEARNING -> {
                Instant lastReview = card.getSrsCard().getLastReview();
                if (lastReview == null) {
                    yield TYPE_1;
                }
                boolean seenToday = lastReview
                        .atZone(ZoneId.systemDefault()).toLocalDate().isEqual(LocalDate.now());
                yield seenToday ? TYPE_2 : TYPE_3;
            }
            case RELEARNING -> TYPE_4;
            case REVIEW -> TYPE_5;
            default -> throw new IllegalStateException("Unclassifiable card state for card id: " + card.getId());
        };
    }
}