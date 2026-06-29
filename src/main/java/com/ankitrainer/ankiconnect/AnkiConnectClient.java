package com.ankitrainer.ankiconnect;

import com.ankitrainer.exception.AnkiConnectException;
import com.fasterxml.jackson.databind.JsonNode;

public interface AnkiConnectClient {

    /**
     * Retrieves the names of all decks available in the connected Anki instance.
     * <p>
     * This method calls the AnkiConnect action: <code>deckNames</code>.
     *
     * @return AnkiConnect response as {@link JsonNode}
     * @throws AnkiConnectException if the connection to Anki fails, the response
     *                              contains an error, the response cannot be parsed
     *                              or a low-level I/O error occurs (e.g., connection reset)
     */
    JsonNode getDeckNames();


    /**
     * Retrieves the names of all note models (card types) available in Anki.
     * <p>
     * This method calls the AnkiConnect action: <code>modelNames</code>.
     *
     * @return AnkiConnect response as {@link JsonNode}
     * @throws AnkiConnectException if the connection to Anki fails, the response
     *                              contains an error, the response cannot be parsed
     *                              or a low-level I/O error occurs (e.g., connection reset)
     */
    JsonNode getModelNames();


    /**
     * Retrieves note type info (name, field names) for a given note type ID.
     * <p>
     * This method calls the AnkiConnect action: <code>modelFieldNames</code>.
     *
     * @param modelName the name of the model
     * @return AnkiConnect response as {@link JsonNode}
     * @throws AnkiConnectException if the connection to Anki fails, the response
     *                              contains an error, the response cannot be parsed
     *                              or a low-level I/O error occurs (e.g., connection reset)
     */
    JsonNode getModelFieldNames(String modelName);

}
