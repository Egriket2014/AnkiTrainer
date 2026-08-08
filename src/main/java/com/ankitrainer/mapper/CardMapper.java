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
    CardResponseDto toDto(CardSrsEntity entity);
}
