package com.ankitrainer.ui;

import com.ankitrainer.model.CardDto;
import com.ankitrainer.service.AnkiConnectService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UiController {

    private static final Logger log = LoggerFactory.getLogger(UiController.class);

    @Autowired
    private AnkiConnectService service;

    @GetMapping("/")
    public String index() {
        return "redirect:/config";
    }

    @GetMapping("/config")
    public String configPage(Model model) {
        model.addAttribute("decks", service.getDecksNames());
        model.addAttribute("allModels", service.getModelNames());
        return "config"; // Это будет наша единственная страница
    }

    @GetMapping("/api/fields")
    @ResponseBody
    public List<String> getFields(@RequestParam String modelName) {
        return service.getModelFieldNames(modelName);
    }

    @GetMapping("/api/decks")
    @ResponseBody
    public List<String> getDecks() {
        return service.getDecksNames();
    }

    @GetMapping("/api/models")
    @ResponseBody
    public List<String> getModels() {
        return service.getModelNames();
    }

    @PostMapping("/api/cards")
    @ResponseBody
    public List<CardDto> getCards(
            @RequestParam String deckName,
            @RequestParam String modelName,
            @RequestParam String wordField,
            @RequestParam String translationField,
            @RequestParam(required = false) String extraField
    ) {
        return service.getCardsByModelAndFields(deckName, modelName, wordField, translationField, extraField);
    }
}
