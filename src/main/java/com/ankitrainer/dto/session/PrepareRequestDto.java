package com.ankitrainer.dto.session;

import com.ankitrainer.language.enums.ConjugationType;
import com.ankitrainer.language.enums.PartOfSpeech;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepareRequestDto {
    private Long deckConfigId;
    private PartOfSpeech partOfSpeech;
    private Set<ConjugationType> conjugationTypes;
}
