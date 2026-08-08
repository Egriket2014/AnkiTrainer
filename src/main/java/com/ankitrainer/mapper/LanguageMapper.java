package com.ankitrainer.mapper;

import com.ankitrainer.dto.language.LanguageKeyLabelResponseDto;
import com.ankitrainer.language.enums.ConjugationType;
import com.ankitrainer.language.enums.Language;
import com.ankitrainer.language.enums.PartOfSpeech;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface LanguageMapper {

    @Mapping(target = "key", source = "key")
    @Mapping(target = "label", source = "label")
    LanguageKeyLabelResponseDto partOfSpeechToDto(PartOfSpeech partOfSpeech);

    @Mapping(target = "key", source = "key")
    @Mapping(target = "label", source = "label")
    LanguageKeyLabelResponseDto conjugationToDto(ConjugationType conjugationType);

    @Mapping(target = "key", source = "key")
    @Mapping(target = "label", source = "label")
    LanguageKeyLabelResponseDto languageToDto(Language language);

    default List<LanguageKeyLabelResponseDto> mapPartOfSpeechSet(Set<PartOfSpeech> partsOfSpeech) {
        if (partsOfSpeech == null || partsOfSpeech.isEmpty()) {
            return List.of();
        }
        return partsOfSpeech.stream()
                .map(this::partOfSpeechToDto)
                .collect(Collectors.toList());
    }

    default List<LanguageKeyLabelResponseDto> mapConjugationSet(Set<ConjugationType> conjugationTypes) {
        if (conjugationTypes == null || conjugationTypes.isEmpty()) {
            return List.of();
        }
        return conjugationTypes.stream()
                .map(this::conjugationToDto)
                .collect(Collectors.toList());
    }

    default List<LanguageKeyLabelResponseDto> mapLanguageSet(Set<Language> languages) {
        if (languages == null || languages.isEmpty()) {
            return List.of();
        }
        return languages.stream()
                .map(this::languageToDto)
                .collect(Collectors.toList());
    }
}