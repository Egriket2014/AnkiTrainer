package com.ankitrainer.service.impl;

import com.ankitrainer.model.CardDto;
import com.ankitrainer.service.anki.AnkiConnectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class AnkiConnectServiceImplTest {

    @Autowired
    private AnkiConnectService ankiConnectService;

    @Test
    void getDecksNames() {
        List<String> names = ankiConnectService.getDecksNames();
        Assertions.assertFalse(names.isEmpty());
    }

    @Test
    void getModelNames() {
        List<String> names = ankiConnectService.getModelNames();
        Assertions.assertFalse(names.isEmpty());
    }

    @Test
    void getModelFieldNames() {
        List<String> names = ankiConnectService.getModelFieldNames("Japanese Sentence Card");
        Assertions.assertFalse(names.isEmpty());
    }

    @Test
    void getVerbsByModelAndFields() {
        List<CardDto> list = ankiConnectService.getVerbsByModelAndFields(
                "ЯПОНСКИЙ",
                "Japanese Sentence Card (с обратной карточкой)",
                "Word",
                "Meaning",
                "Reading"
        );
        Assertions.assertFalse(list.isEmpty());
    }
}