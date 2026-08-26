package com.ankitrainer.controller.api;

import com.ankitrainer.dto.card.CardIndexResponseDto;
import com.ankitrainer.dto.card.CardResponseDto;
import com.ankitrainer.dto.session.PrepareRequestDto;
import com.ankitrainer.dto.session.QueueStatsDto;
import com.ankitrainer.entity.CardSrsEntity;
import com.ankitrainer.mapper.CardMapper;
import com.ankitrainer.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/session")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;
    private final CardMapper cardMapper;

    @PostMapping("/prepare")
    public void prepareCards(@RequestBody PrepareRequestDto request) {
        sessionService.prepareCards(
                request.getDeckConfigId(),
                request.getPartOfSpeech(),
                request.getConjugationTypes()
        );
    }

    @GetMapping("/current")
    public CardResponseDto getCurrentCard() {
        if (sessionService.isComplete()) {
            return null;
        }

        CardSrsEntity srsEntity = sessionService.getCurrentCard();
        return cardMapper.toDto(srsEntity);
    }

    @GetMapping("/queue/count")
    public int getQueueCount() {
        return sessionService.getQueueCards().size();
    }

    @GetMapping("/queue")
    public List<CardIndexResponseDto> getQueue(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        int offset = (page - 1) * limit;
        return sessionService.getQueueCards().stream()
                .skip(offset)
                .limit(limit)
                .map(cardMapper::toIndexDto)
                .toList();
    }

    @PostMapping("/check")
    public boolean checkAnswer(@RequestParam String answer) {
        return sessionService.checkAnswer(answer);
    }

    @GetMapping("/is-complete")
    public boolean isComplete() {
        return sessionService.isComplete();
    }

    @GetMapping("/queue-stats")
    public QueueStatsDto getQueueStats() {
        return sessionService.getQueueStats();
    }

    @PostMapping("/save")
    public void saveResults() {
        sessionService.saveResults();
    }
}