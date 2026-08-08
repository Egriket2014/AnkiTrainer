package com.ankitrainer.dto.deckconfig;

import com.ankitrainer.language.enums.Language;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeckConfigListResponseDto {
    private Long id;
    private String deckName;
    private Language language;
}
