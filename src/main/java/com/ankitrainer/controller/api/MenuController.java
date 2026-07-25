package com.ankitrainer.controller.api;

import com.ankitrainer.config.model.ConfigData;
import com.ankitrainer.config.service.ConfigService;
import com.ankitrainer.service.factory.ConjugatorFactory;
import com.ankitrainer.util.enums.JapaneseVerbFormLabelEnum;
import com.ankitrainer.util.enums.PartOfSpeechLabelEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final ConfigService configService;
    private final ConjugatorFactory conjugatorFactory;

    @GetMapping("/parts-of-speech")
    public List<Map<String, String>> getPartsOfSpeech() {
        ConfigData config = configService.loadConfig();
        String language = config.getLanguage();
        Set<String> partsOfSpeech = conjugatorFactory.getSupportedPartsOfSpeech(language);

        return partsOfSpeech.stream()
                .map(key -> Map.of(
                        "key", key,
                        "label", PartOfSpeechLabelEnum.getLabel(key)
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/conjugation-types")
    public List<Map<String, String>> getConjugationTypes(@RequestParam String partOfSpeech) {
        ConfigData config = configService.loadConfig();
        String language = config.getLanguage();
        Set<String> conjugationTypes = conjugatorFactory.getSupportedConjugationTypes(language, partOfSpeech);

        return conjugationTypes.stream()
                .map(key -> Map.of(
                        "key", key,
                        "label", JapaneseVerbFormLabelEnum.getLabel(key)
                ))
                .collect(Collectors.toList());
    }
}