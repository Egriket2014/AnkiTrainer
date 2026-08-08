package com.ankitrainer.controller.api;

import com.ankitrainer.service.anki.AnkiConnectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/anki-connect")
@RequiredArgsConstructor
public class AnkiConnectController {

    private final AnkiConnectService ankiConnectService;

    @GetMapping("/decks")
    public List<String> getDecks() {
        return ankiConnectService.getDecksNames();
    }

    @GetMapping("/models")
    public List<String> getModels() {
        return ankiConnectService.getModelNames();
    }

    @GetMapping("/fields")
    public List<String> getFields(@RequestParam String modelName) {
        return ankiConnectService.getModelFieldNames(modelName);
    }
}
