package com.ankitrainer.service.impl;

import com.ankitrainer.service.AnkiConnectService;
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
}