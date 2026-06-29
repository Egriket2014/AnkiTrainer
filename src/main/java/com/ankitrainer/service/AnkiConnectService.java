package com.ankitrainer.service;

import com.ankitrainer.exception.AnkiConnectException;

import java.util.List;

public interface AnkiConnectService {

    /**
     * Retrieves the names of all decks available in the connected Anki instance.
     *
     * @return a list of deck names
     * @throws AnkiConnectException if request to AnkiConnect fails
     */
    List<String> getDecksNames();

    /**
     * Retrieves the names of all note models (card types) available in the connected Anki instance.
     *
     * @return a list of all model names
     * @throws AnkiConnectException if request to AnkiConnect fails
     */
    List<String> getModelNames();

    /**
     * Retrieves the names of all fields names for specified model.
     *
     * @return a list of fields names
     * @throws AnkiConnectException if request to AnkiConnect fails
     */
    List<String> getModelFieldNames(String modelName);
}
