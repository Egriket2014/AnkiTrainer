package com.ankitrainer.controller.api;

import com.ankitrainer.dto.card.CardIndexResponseDto;
import com.ankitrainer.dto.session.QueueStatsDto;
import com.ankitrainer.dto.sync.SyncDeckResultDto;
import com.ankitrainer.language.enums.ConjugationType;
import com.ankitrainer.mapper.CardMapper;
import com.ankitrainer.service.DeckService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deck")
@RequiredArgsConstructor
public class DeckController {

    private final DeckService deckService;
    private final CardMapper cardMapper;

    @GetMapping("/rgb-stats")
    public QueueStatsDto getRGBStats(
            @RequestParam Long deckConfigId,
            @RequestParam ConjugationType conjugationType
    ) {
        return deckService.getRGBStats(deckConfigId, conjugationType);
    }

    @GetMapping("/count")
    public int getDeckCardsCount(@RequestParam Long deckConfigId) {
        return deckService.getDeckCardsCount(deckConfigId);
    }

    @GetMapping
    public List<CardIndexResponseDto> getDeckCards(
            @RequestParam Long deckConfigId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        int offset = (page - 1) * limit;
        return deckService.getDeckCards(deckConfigId, limit, offset)
                .stream()
                .map(cardMapper::toIndexDto)
                .toList();
    }

    @PostMapping("/sync")
    public SyncDeckResultDto syncDeck(@RequestParam Long deckConfigId) {
        return deckService.syncCards(deckConfigId);
    }
}