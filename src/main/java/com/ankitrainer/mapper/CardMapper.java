package com.ankitrainer.mapper;

import com.ankitrainer.dto.card.CardResponseDto;
import com.ankitrainer.entity.CardSrsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CardMapper {

    @Mapping(target = "word", source = "card.word")
    @Mapping(target = "translation", source = "card.translation")
    @Mapping(target = "extra", source = "card.extra")
    @Mapping(target = "expectedAnswer", source = "answer")
    @Mapping(target = "stem", expression = "java(CardMapper.extractStem(entity.getCard().getWord(), entity.getAnswer()))")
    @Mapping(target = "conjugationLabel", expression = "java(entity.getConjugationType() != null ? entity.getConjugationType().getLabel() : null)")
    CardResponseDto toDto(CardSrsEntity entity);

    static String extractStem(String word, String answer) {
        if (word == null || answer == null || word.isEmpty() || answer.isEmpty()) {
            return null;
        }

        int prefixLength = 0;
        int maxLength = Math.min(word.length(), answer.length());
        while (prefixLength < maxLength
                && word.charAt(prefixLength) == answer.charAt(prefixLength)
        ) {
            prefixLength++;
        }

        return word.substring(0, prefixLength);
    }
}