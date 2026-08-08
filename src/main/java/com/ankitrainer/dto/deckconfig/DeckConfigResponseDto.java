package com.ankitrainer.dto.deckconfig;

import com.ankitrainer.language.enums.Language;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeckConfigResponseDto {
    private Long id;
    private String deckName;
    private String modelName;
    private Language language;
    private String wordField;
    private String translationField;
    private String extraField;
    private Integer reviewLimit;
    private Integer newLimit;
    private String lastSyncedAt;
}