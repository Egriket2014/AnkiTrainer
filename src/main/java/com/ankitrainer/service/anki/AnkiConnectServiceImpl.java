package com.ankitrainer.service.anki;

import com.ankitrainer.ankiconnect.AnkiConnectClient;
import com.ankitrainer.language.LanguageAnalyzer;
import com.ankitrainer.model.CardDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnkiConnectServiceImpl implements AnkiConnectService {

    @Autowired
    private AnkiConnectClient client;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LanguageAnalyzer languageAnalyzer;

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
    public List<CardDto> getVerbsByModelAndFields(
            String deckName,
            String modelName,
            String wordFieldName,
            String translationFieldName,
            String extraFieldName
    ) {
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

        return noteList.stream()
                .filter(noteNode -> modelName.equals(noteNode.get("modelName").asText()))
                .filter(noteNode -> {
                    String word = extractFieldValue(noteNode.get("fields"), wordFieldName);
                    return languageAnalyzer.isVerb(word);
                })
                .map(noteNode -> {
                    JsonNode fieldsNode = noteNode.get("fields");

                    String word = extractFieldValue(fieldsNode, wordFieldName);
                    log.debug("Mapping word {}", word);
                    String translation = extractFieldValue(fieldsNode, translationFieldName);
                    String extra = extraFieldName != null ? extractFieldValue(fieldsNode, extraFieldName) : "";

                    return CardDto.builder()
                            .noteId(noteNode.get("noteId").asLong())
                            .word(word)
                            .translation(translation)
                            .extra(extra)
                            .modelName(noteNode.get("modelName").asText())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private String extractFieldValue(JsonNode fieldsNode, String fieldName) {
        if (fieldsNode.has(fieldName) && fieldsNode.get(fieldName).has("value")) {
            return fieldsNode.get(fieldName).get("value").asText();
        } else {
            return  "";
        }
    }
}
