package com.ankitrainer.mapper;

import com.ankitrainer.dto.deckconfig.CreateDeckConfigRequestDto;
import com.ankitrainer.dto.deckconfig.DeckConfigResponseDto;
import com.ankitrainer.dto.deckconfig.DeckConfigListResponseDto;
import com.ankitrainer.dto.deckconfig.UpdateDeckConfigRequestDto;
import com.ankitrainer.entity.DeckConfigEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DeckConfigMapper {

    @Mapping(target = "lastSyncedAt", dateFormat = "yyyy-MM-dd HH:mm")
    DeckConfigResponseDto toDto(DeckConfigEntity entity);

    DeckConfigListResponseDto toListDto(DeckConfigEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastSyncedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    DeckConfigEntity createDtoToEntity(CreateDeckConfigRequestDto dto);

    @Mapping(target = "deckName", ignore = true)
    @Mapping(target = "modelName", ignore = true)
    @Mapping(target = "language", ignore = true)
    @Mapping(target = "wordField", ignore = true)
    @Mapping(target = "translationField", ignore = true)
    @Mapping(target = "extraField", ignore = true)
    @Mapping(target = "lastSyncedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    DeckConfigEntity updateDtoToEntity(UpdateDeckConfigRequestDto dto);

    void updateEntity(@MappingTarget DeckConfigEntity existing, DeckConfigEntity updateData);
}