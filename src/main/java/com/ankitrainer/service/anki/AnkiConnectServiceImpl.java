package com.ankitrainer.service.anki;

import com.ankitrainer.ankiconnect.AnkiConnectClient;
import com.ankitrainer.entity.CardEntity;
import com.ankitrainer.entity.DeckConfigEntity;
import com.ankitrainer.language.enums.Language;
import com.ankitrainer.language.enums.PartOfSpeech;
import com.ankitrainer.service.language.LanguageService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AnkiConnectServiceImpl implements AnkiConnectService {

    @Autowired
    private AnkiConnectClient client;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LanguageService languageService;

    private static final Logger log = LoggerFactory.getLogger(AnkiConnectServiceImpl.class);

    @Override
    public List<String> getDecksNames() {
        JsonNode ankiResponse = client.getDeckNames();

        List<String> decksNames = objectMapper.convertValue(
                ankiResponse.get("result"),
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)
        );

        log.info("Found {} decks", decksNames.size());
        log.debug("Deck list: {}", decksNames);

        return decksNames;
    }

    @Override
    public List<String> getModelNames() {
        JsonNode ankiResponse = client.getModelNames();

        List<String> modelNames = objectMapper.convertValue(
                ankiResponse.get("result"),
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)
        );

        log.info("Found {} models", modelNames.size());
        log.debug("Models list: {}", modelNames);

        return modelNames;
    }

    @Override
    public List<String> getModelFieldNames(String modelName) {
        JsonNode ankiResponse = client.getFieldNamesForModel(modelName);

        List<String> fieldNames = objectMapper.convertValue(
                ankiResponse.get("result"),
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)
        );

        log.info("Found {} fields for model '{}'", fieldNames.size(), modelName);
        log.debug("Field names for model '{}': {}", modelName, fieldNames);

        return fieldNames;
    }

    @Override
    public List<CardEntity> getSupportedCardsForDeck(DeckConfigEntity deckConfig) {
        String deckName = deckConfig.getDeckName();
        String modelName = deckConfig.getModelName();
        String wordFieldName = deckConfig.getWordField();
        String translationFieldName = deckConfig.getTranslationField();
        String extraFieldName = deckConfig.getExtraField();
        Language language = deckConfig.getLanguage();

        JsonNode ankiResponseAllNotesIds = client.getNotesIdsForDeck(deckName);
        List<Long> allNoteIds = objectMapper.convertValue(
                ankiResponseAllNotesIds.get("result"),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Long.class)
        );

        if (allNoteIds.isEmpty()) {
            log.info("No notes found in deck '{}'", deckName);
            return List.of();
        }
        log.debug("Found {} total notes in deck", allNoteIds.size());

        JsonNode ankiResponseAllNotesInfo = client.getNotesInfoByIds(allNoteIds);
        List<JsonNode> noteList = objectMapper.convertValue(
                ankiResponseAllNotesInfo.get("result"),
                objectMapper.getTypeFactory().constructCollectionType(List.class, JsonNode.class)
        );

        Set<PartOfSpeech> supportedPartsOfSpeech = languageService.getSupportedPartsOfSpeech(language);
        List<CardEntity> result = new ArrayList<>();

        for (JsonNode noteNode : noteList) {
            if (!modelName.equals(noteNode.get("modelName").asText())) {
                continue;
            }

            JsonNode fieldsNode = noteNode.get("fields");
            String word = extractFieldValue(fieldsNode, wordFieldName);

            if (word == null || word.isBlank()) {
                continue;
            }

            PartOfSpeech partOfSpeech = languageService.detectPartOfSpeech(word, language);

            if (partOfSpeech == null || !supportedPartsOfSpeech.contains(partOfSpeech)) {
                continue;
            }

            String translation = extractFieldValue(fieldsNode, translationFieldName);
            String extra = extraFieldName != null && !extraFieldName.isBlank()
                    ? extractFieldValue(fieldsNode, extraFieldName)
                    : null;
            Long noteId = noteNode.get("noteId").asLong();

            CardEntity card = CardEntity.builder()
                    .noteId(noteId)
                    .word(word)
                    .translation(translation)
                    .extra(extra)
                    .partOfSpeech(partOfSpeech)
                    .deckName(deckName)
                    .build();

            result.add(card);
        }

        log.info("Found {} supported cards in deck '{}'", result.size(), deckName);
        return result;
    }

    private String extractFieldValue(JsonNode fieldsNode, String fieldName) {
        if (fieldsNode.has(fieldName) && fieldsNode.get(fieldName).has("value")) {
            String value = fieldsNode.get(fieldName).get("value").asText();
            return value != null ? value.trim() : null;
        }
        return null;
    }
}
