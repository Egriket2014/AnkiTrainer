package com.ankitrainer.service.impl;

import com.ankitrainer.service.AnkiConnectService;
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
        int a = 2;
    }
}