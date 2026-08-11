package com.ankitrainer.controller.api;

import com.ankitrainer.dto.session.QueueStatsDto;
import com.ankitrainer.language.enums.ConjugationType;
import com.ankitrainer.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping("/queue-stats")
    public QueueStatsDto getQueueStats(@RequestParam Long deckConfigId,
                                       @RequestParam ConjugationType conjugationType) {
        return cardService.getQueueStats(deckConfigId, conjugationType);
    }
}
