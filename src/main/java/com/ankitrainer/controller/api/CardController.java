package com.ankitrainer.controller.api;

import com.ankitrainer.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/card")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping("/prepare")
    public Map<String, Object> prepareCards(@RequestBody Map<String, String> request) {
        String partOfSpeech = request.get("partOfSpeech");
        String conjugationType = request.get("conjugationType");

        try {
            cardService.prepareCards(partOfSpeech, conjugationType);
            return Map.of("success", true);
        } catch (Exception e) {
            return Map.of(
                    "success", false,
                    "error", e.getMessage()
            );
        }
    }

    @GetMapping("/current")
    public Map<String, Object> getCurrentCard() {
        if (cardService.isComplete()) {
            return Map.of("complete", true);
        }

        return Map.of(
                "complete", false,
                "card", cardService.getCurrentCard()
        );
    }

    @PostMapping("/check")
    public Map<String, Object> checkAnswer(@RequestParam String answer) {
        return Map.of("correct", cardService.checkAnswer(answer));
    }

    @PostMapping("/save")
    public Map<String, Object> saveResults() {
        cardService.saveResults();
        return Map.of("success", true);
    }
}