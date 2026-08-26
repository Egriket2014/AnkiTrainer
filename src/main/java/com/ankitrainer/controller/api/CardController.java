package com.ankitrainer.controller.api;

import com.ankitrainer.dto.card.CardIndexResponseDto;
import com.ankitrainer.dto.session.QueueStatsDto;
import com.ankitrainer.language.enums.ConjugationType;
import com.ankitrainer.mapper.CardMapper;
import com.ankitrainer.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    private final CardMapper cardMapper;

    @GetMapping("/queue-stats")
    public QueueStatsDto getQueueStats(
            @RequestParam Long deckConfigId,
            @RequestParam ConjugationType conjugationType
    ) {
        return cardService.getQueueStats(deckConfigId, conjugationType);
    }

    @GetMapping("/deck/count")
    public int getDeckCardsCount(@RequestParam Long deckConfigId) {
        return cardService.getDeckCardsCount(deckConfigId);
    }

    @GetMapping("/deck")
    public List<CardIndexResponseDto> getDeckCards(
            @RequestParam Long deckConfigId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        int offset = (page - 1) * limit;
        return cardService.getDeckCards(deckConfigId, limit, offset)
                .stream()
                .map(cardMapper::toIndexDto)
                .toList();
    }
}
