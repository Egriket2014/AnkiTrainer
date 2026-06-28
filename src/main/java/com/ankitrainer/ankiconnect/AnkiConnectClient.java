package com.ankitrainer.ankiconnect;

import com.ankitrainer.exception.AnkiConnectException;

import java.util.List;

public interface AnkiConnectClient {

    /**
     * Retrieves the names of all decks available in the connected Anki instance.
     * <p>
     * This method calls the AnkiConnect action: <code>deckNames</code>.
     *
     * @return a list of deck names or empty list if no decks exist
     * @throws AnkiConnectException if the connection to Anki fails, the response
     *                              contains an error, the response cannot be parsed
     *                              or a low-level I/O error occurs (e.g., connection reset)
     */
    List<String> getDeckNames();

}
