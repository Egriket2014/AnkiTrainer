package com.ankitrainer.queue;

import com.ankitrainer.dto.session.QueueStatsDto;
import com.ankitrainer.entity.CardSrsEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;

public class SessionQueue {

    private final PriorityQueue<CardSrsEntity> queue = new PriorityQueue<>(CardSrsEntity.BY_DUE);

    private int blue;
    private int red;
    private int green;

    public void addAll(Collection<CardSrsEntity> cards) {
        for (CardSrsEntity card : cards) {
            add(card);
        }
    }

    public void add(CardSrsEntity card) {
        queue.offer(card);
        switch (CardType.classify(card)) {
            case TYPE_1 -> blue++;
            case TYPE_2, TYPE_3, TYPE_4 -> red++;
            case TYPE_5 -> green++;
        }
    }

    public CardSrsEntity poll() {
        CardSrsEntity card = queue.poll();
        if (card != null) {
            switch (CardType.classify(card)) {
                case TYPE_1 -> blue--;
                case TYPE_2, TYPE_3, TYPE_4 -> red--;
                case TYPE_5 -> green--;
            }
        }
        return card;
    }

    public CardSrsEntity peek() {
        return queue.peek();
    }

    public List<CardSrsEntity> peekAll() {
        PriorityQueue<CardSrsEntity> copy = new PriorityQueue<>(queue);
        List<CardSrsEntity> result = new ArrayList<>(copy.size());
        while (!copy.isEmpty()) {
            result.add(copy.poll());
        }
        return result;
    }

    public int size() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public QueueStatsDto stats() {
        return QueueStatsDto.builder()
                .blue(blue)
                .red(red)
                .green(green)
                .build();
    }
}