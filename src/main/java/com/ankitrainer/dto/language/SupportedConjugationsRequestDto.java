package com.ankitrainer.dto.language;

import com.ankitrainer.language.enums.Language;
import com.ankitrainer.language.enums.PartOfSpeech;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportedConjugationsRequestDto {
    private Language language;
    private PartOfSpeech partOfSpeech;
}
