package com.ankitrainer.service.impl;

import com.ankitrainer.ankiconnect.AnkiConnectClient;
import com.ankitrainer.service.AnkiConnectService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnkiConnectServiceImpl implements AnkiConnectService {

    @Autowired
    private AnkiConnectClient client;

    @Autowired
    private ObjectMapper objectMapper;

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
        JsonNode ankiResponse = client.getModelFieldNames(modelName);

        List<String> fieldNames = objectMapper.convertValue(
                ankiResponse.get("result"),
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)
        );

        log.info("Found {} fields for model '{}'", fieldNames.size(), modelName);
        log.debug("Field names for model '{}': {}", modelName, fieldNames);

        return fieldNames;
    }
}
