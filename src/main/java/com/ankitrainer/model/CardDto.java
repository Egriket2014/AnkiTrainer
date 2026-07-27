package com.ankitrainer.model;

import io.github.openspacedrepetition.Card;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDto implements Comparable<CardDto> {

    private Long noteId;
    private String word;
    private String translation;
    private String extra;
    private String modelName;
    private String expectedAnswer;

    private Card srsCard;

    public static final Comparator<CardDto> BY_DUE =
            Comparator.comparing(c -> c.getSrsCard().getDue());

    @Override
    public int compareTo(CardDto o) {
        return BY_DUE.compare(this, o);
    }
}
