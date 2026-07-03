package com.ankitrainer.service;

import com.ankitrainer.exception.AnkiConnectException;
import com.ankitrainer.model.CardDto;

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
     * @param modelName name of card type
     * @return a list of fields names
     * @throws AnkiConnectException if request to AnkiConnect fails
     */
    List<String> getModelFieldNames(String modelName);

    /**
     * Retrieves a list of cards (notes) from a specific deck, filtered by the note model,
     * and extracts values from the selected fields.
     *
     * @param deckName              the name of the Anki deck
     * @param modelName             the name of the note model to filter by
     * @param wordFieldName         the name of the field that contains the word
     * @param translationFieldName  the name of the field that contains the translation
     * @param extraFieldName        the name of an optional additional field.
     * @return a list of CardDto
     * @throws AnkiConnectException if request to AnkiConnect fails
     */
    List<CardDto> getVerbsByModelAndFields(
            String deckName,
            String modelName,
            String wordFieldName,
            String translationFieldName,
            String extraFieldName
    );
}
