package com.ankitrainer.ankiconnect.impl;

import com.ankitrainer.ankiconnect.AnkiConnectClient;
import com.ankitrainer.exception.AnkiConnectException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Component
public class AnkiConnectClientImpl implements AnkiConnectClient {

    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger log = LoggerFactory.getLogger(AnkiConnectClientImpl.class);

    @Value("${ankiconnect.url:http://localhost:8765}")
    private String ankiConnectUrl;
    @Value("${ankiconnect.version:6}")
    private Integer ankiConnectVersion;


    private JsonNode sendRequest(String action, Object params) {
        log.debug("Sending request: action={}, params={}", action, params);

        try {
            Map<String, Object> requestBody = Map.of(
                    "action", action,
                    "version", ankiConnectVersion,
                    "params", params != null ? params : Map.of()
            );

            String json = objectMapper.writeValueAsString(requestBody);
            log.trace("Request body: {}", json);

            HttpURLConnection conn = (HttpURLConnection) new URL(ankiConnectUrl).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestProperty("Connection", "close");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            log.debug("Response code: {}", responseCode);

            String responseBody;
            try (InputStream is = responseCode >= 400 ? conn.getErrorStream() : conn.getInputStream()) {
                if (is == null) {
                    throw new IOException("Response body is null (input stream is empty)");
                }
                responseBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }
            log.trace("Response body: {}", responseBody);

            JsonNode responseJson = objectMapper.readTree(responseBody);
            if (responseJson.has("error") && !responseJson.get("error").isNull()) {
                String msg = responseJson.get("error").asText();
                log.error("AnkiConnect returned an error: {}", msg);
                throw new AnkiConnectException("AnkiConnect error: " + msg);
            }

            log.info("Request completed successfully action={}", action);
            return responseJson;

        } catch (IOException e) {
            log.error("Failed to execute request to AnkiConnect: {}", e.getMessage(), e);
            throw new AnkiConnectException("Failed to execute request to AnkiConnect", e);
        }
    }

    @Override
    public JsonNode getDeckNames() {
        log.info("Fetching deck names from Anki...");
        JsonNode response = sendRequest("deckNames", null);
        log.trace("AnkiConnect 'deckNames' response JsonNode={}", response);

        return response;
    }

    @Override
    public JsonNode getModelNames() {
        log.info("Fetching all model names from Anki...");
        JsonNode response = sendRequest("modelNames", null);
        log.trace("AnkiConnect 'modelNames' response JsonNode={}", response);

        return response;
    }

    @Override
    public JsonNode getFieldNamesForModel(String modelName) {
        log.info("Fetching model field names for model '{}' from Anki...", modelName);
        JsonNode response = sendRequest("modelFieldNames", Map.of("modelName", modelName));
        log.trace("AnkiConnect 'modelFieldNames' response JsonNode={}", response);

        return response;
    }

    @Override
    public JsonNode getNotesIdsForDeck(String deckName) {
        log.info("Fetching notes IDs for deck '{}' from Anki...", deckName);
        JsonNode response = sendRequest("findNotes", Map.of("query", "deck:" + deckName));
        log.trace("AnkiConnect 'findNotes' response JsonNode={}", response);

        return response;
    }

    @Override
    public JsonNode getNotesInfoByIds(List<Long> noteIds) {
        log.info("Fetching notes from Anki...");
        JsonNode response = sendRequest("notesInfo", Map.of("notes", noteIds));
        log.trace("AnkiConnect 'notesInfo' response JsonNode={}", response);

        return response;
    }
}
