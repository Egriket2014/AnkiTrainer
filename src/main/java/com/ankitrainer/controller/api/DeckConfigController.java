package com.ankitrainer.controller.api;

import com.ankitrainer.dto.deckconfig.CreateDeckConfigRequestDto;
import com.ankitrainer.dto.deckconfig.DeckConfigListResponseDto;
import com.ankitrainer.dto.deckconfig.DeckConfigResponseDto;
import com.ankitrainer.dto.deckconfig.UpdateDeckConfigRequestDto;
import com.ankitrainer.entity.DeckConfigEntity;
import com.ankitrainer.mapper.DeckConfigMapper;
import com.ankitrainer.service.DeckConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/deck-config")
@RequiredArgsConstructor
public class DeckConfigController {

    private final DeckConfigService deckConfigService;
    private final DeckConfigMapper deckConfigMapper;

    @GetMapping
    public List<DeckConfigListResponseDto> getAllDecks() {
        return deckConfigService.getAllDeckConfigs().stream()
                .map(deckConfigMapper::toListDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public DeckConfigResponseDto getDeckConfig(@PathVariable Long id) {
        DeckConfigEntity entity = deckConfigService.getDeckConfigById(id);
        return deckConfigMapper.toDto(entity);
    }

    @PostMapping("/create")
    public DeckConfigResponseDto createDeckConfig(@Valid @RequestBody CreateDeckConfigRequestDto request) {
        DeckConfigEntity entity = deckConfigMapper.createDtoToEntity(request);
        DeckConfigEntity saved = deckConfigService.createDeckConfig(entity);
        return deckConfigMapper.toDto(saved);
    }

    @PatchMapping("/update")
    public DeckConfigResponseDto updateDeckConfig(@Valid @RequestBody UpdateDeckConfigRequestDto request) {
        DeckConfigEntity entity = deckConfigMapper.updateDtoToEntity(request);
        DeckConfigEntity updated = deckConfigService.updateDeckConfig(entity);
        return deckConfigMapper.toDto(updated);
    }

    @DeleteMapping("/{id}")
    public void deleteDeck(@PathVariable Long id) {
        deckConfigService.deleteDeck(id);
    }

    //    @PostMapping("/{id}/sync")
    //    public ResponseEntity<?> syncDeck(@PathVariable Long id) {
    //        // TODO
    //    }
}