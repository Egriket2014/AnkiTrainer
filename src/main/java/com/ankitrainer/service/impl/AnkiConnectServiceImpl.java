package com.ankitrainer.service.impl;

import com.ankitrainer.ankiconnect.AnkiConnectClient;
import com.ankitrainer.service.AnkiConnectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnkiConnectServiceImpl implements AnkiConnectService {

    private final AnkiConnectClient client;

    @Override
    public List<String> getDecksNames() {
        return client.getDeckNames();
    }
}
