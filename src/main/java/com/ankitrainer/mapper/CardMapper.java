package com.ankitrainer.mapper;

import com.ankitrainer.dto.card.CardIndexResponseDto;
import com.ankitrainer.dto.card.CardResponseDto;
import com.ankitrainer.entity.CardSrsEntity;
import com.ankitrainer.queue.CardType;
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

    @Mapping(target = "word", source = "card.word")
    @Mapping(target = "translation", source = "card.translation")
    @Mapping(target = "extra", source = "card.extra")
    @Mapping(target = "expectedAnswer", source = "answer")
    @Mapping(target = "stem", expression = "java(CardMapper.extractStem(entity.getCard().getWord(), entity.getAnswer()))")
    @Mapping(target = "conjugationLabel", expression = "java(entity.getConjugationType() != null ? entity.getConjugationType().getLabel() : null)")
    @Mapping(target = "due", expression = "java(entity.getSrsCard() != null && entity.getSrsCard().getDue() != null ? entity.getSrsCard().getDue().toString() : null)")
    @Mapping(target = "status", expression = "java(entity.getState() != null ? entity.getState().name() : null)")
    @Mapping(target = "color", expression = "java(CardMapper.classifyColor(entity))")
    CardIndexResponseDto toIndexDto(CardSrsEntity entity);

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

    static String classifyColor(CardSrsEntity entity) {
        return switch (CardType.classify(entity)) {
            case TYPE_1 -> "blue";
            case TYPE_2, TYPE_3, TYPE_4 -> "red";
            case TYPE_5 -> "green";
        };
    }
}