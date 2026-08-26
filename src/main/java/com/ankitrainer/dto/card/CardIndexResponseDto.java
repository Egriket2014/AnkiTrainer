package com.ankitrainer.dto.card;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardIndexResponseDto {

    private String word;
    private String translation;
    private String extra;
    private String expectedAnswer;
    private String stem;
    private String conjugationLabel;

    private String due;
    private String status;
    private String color;
}