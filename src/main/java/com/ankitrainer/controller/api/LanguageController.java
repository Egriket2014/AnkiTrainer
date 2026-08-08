package com.ankitrainer.controller.api;

import com.ankitrainer.dto.language.LanguageKeyLabelResponseDto;
import com.ankitrainer.dto.language.SupportedConjugationsRequestDto;
import com.ankitrainer.language.enums.ConjugationType;
import com.ankitrainer.language.enums.Language;
import com.ankitrainer.language.enums.PartOfSpeech;
import com.ankitrainer.mapper.LanguageMapper;
import com.ankitrainer.service.language.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/language")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;
    private final LanguageMapper languageMapper;

    @GetMapping("/supported-parts-of-speech")
    public List<LanguageKeyLabelResponseDto> getSupportedPartsOfSpeech(@RequestParam Language language) {
        Set<PartOfSpeech> partsOfSpeech = languageService.getSupportedPartsOfSpeech(language);
        return languageMapper.mapPartOfSpeechSet(partsOfSpeech);
    }

    @GetMapping("/supported-conjugations")
    public List<LanguageKeyLabelResponseDto> getSupportedConjugations(
            @ModelAttribute SupportedConjugationsRequestDto request
    ) {
        Set<ConjugationType> conjugationTypes = languageService.getSupportedConjugationTypes(
                request.getLanguage(),
                request.getPartOfSpeech()
        );
        return languageMapper.mapConjugationSet(conjugationTypes);
    }

    @GetMapping("/supported-languages")
    public List<LanguageKeyLabelResponseDto> getSupportedLanguages() {
        Set<Language> languages = languageService.getSupportedLanguages();
        return languageMapper.mapLanguageSet(languages);
    }
}