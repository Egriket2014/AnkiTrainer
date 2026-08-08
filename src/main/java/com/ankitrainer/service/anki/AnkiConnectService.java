package com.ankitrainer.service.anki;

import com.ankitrainer.entity.CardEntity;
import com.ankitrainer.entity.DeckConfigEntity;
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
     * @param modelName name of card type
     * @return a list of fields names
     * @throws AnkiConnectException if request to AnkiConnect fails
     */
    List<String> getModelFieldNames(String modelName);

    /**
     * Retrieves a list of cards (notes) from a specific deck, filtered by the note model,
     * supported part of speech for language and extracts values from the selected fields.
     *
     * @param deckConfig deck configuration(language, name, model and fields for select)
     * @return a list of CardEntity
     * @throws AnkiConnectException if request to AnkiConnect fails
     */
    List<CardEntity> getSupportedCardsForDeck(DeckConfigEntity deckConfig);
}
