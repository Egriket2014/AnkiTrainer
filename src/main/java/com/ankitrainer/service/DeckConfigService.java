package com.ankitrainer.service;

import com.ankitrainer.entity.DeckConfigEntity;
import com.ankitrainer.exception.DeckNotFoundException;
import com.ankitrainer.mapper.DeckConfigMapper;
import com.ankitrainer.repository.DeckConfigRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeckConfigService {

    private static final Logger log = LoggerFactory.getLogger(DeckConfigService.class);

    @Autowired
    private DeckConfigRepository deckConfigRepository;
    @Autowired
    private DeckConfigMapper deckConfigMapper;
    @Autowired
    private DeckService deckService;

    private final Map<String, DeckConfigEntity> cacheByName = new HashMap<>();
    private final Map<Long, DeckConfigEntity> cacheById = new HashMap<>();

    @PostConstruct
    public void initCache() {
        log.info("Loading deck configurations into cache...");
        List<DeckConfigEntity> allDecks = deckConfigRepository.findAll();
        allDecks.forEach(dc -> {
                cacheByName.put(dc.getDeckName(), dc);
                cacheById.put(dc.getId(), dc);
        });
        log.info("Loaded {} deck configurations into cache", cacheByName.size());
    }

    public List<DeckConfigEntity> getAllDeckConfigs() {
        log.debug("Fetching all deck names");
        List<DeckConfigEntity> deckConfigs =  new ArrayList<>(cacheById.values());
        return !deckConfigs.isEmpty() ? deckConfigs : deckConfigRepository.findAll();
    }

    public DeckConfigEntity getDeckConfigById(Long id) {
        log.debug("Fetching deck configuration by ID: {}", id);
        DeckConfigEntity deckConfig = cacheById.get(id);
        return deckConfig != null ? deckConfig : deckConfigRepository.findById(id)
                .orElseThrow(() -> new DeckNotFoundException("Deck not found with ID: " + id));
    }

    public DeckConfigEntity getDeckConfigByName(String deckName) {
        log.debug("Fetching deck configuration by name: {}", deckName);
        DeckConfigEntity deckConfig = cacheByName.get(deckName);
        return deckConfig != null ? deckConfig :  deckConfigRepository.findByDeckName(deckName)
                .orElseThrow(() -> new DeckNotFoundException("Deck not found with name: " + deckName));
    }

    @Transactional
    public DeckConfigEntity createDeckConfig(DeckConfigEntity deckConfig) {
        log.info("Creating new deck configuration: {}", deckConfig.getDeckName());

        if (deckConfig.getDeckName() == null || deckConfig.getDeckName().isBlank()) {
            throw new IllegalArgumentException("Deck name cannot be empty");
        }

        if (deckConfigRepository.existsByDeckName(deckConfig.getDeckName())) {
            throw new IllegalArgumentException("Deck already exists: " + deckConfig.getDeckName());
        }

        syncCache(deckConfig, false);
        DeckConfigEntity saved = deckConfigRepository.save(deckConfig);
        deckService.createCardsFromAnki(saved);
        return saved;
    }

    @Transactional
    public DeckConfigEntity updateDeckConfig(DeckConfigEntity deckConfig) {
        if (deckConfig.getId() == null) {
            throw new IllegalArgumentException("ID is required for update");
        }

        log.info("Updating deck configuration: {}", deckConfig.getId());

        DeckConfigEntity existing = getDeckConfigById(deckConfig.getId());
        if (!existing.getId().equals(deckConfig.getId())) {
            throw new IllegalArgumentException(
                    "ID mismatch: existing=" + existing.getId() + ", update=" + deckConfig.getId()
            );
        }

        deckConfigMapper.updateEntity(existing, deckConfig);
        syncCache(existing, false);
        return deckConfigRepository.save(existing);
    }

    @Transactional
    public void deleteDeck(Long id) {
        log.info("Deleting deck configuration: {}", id);
        DeckConfigEntity deckConfig = getDeckConfigById(id);
        deckConfigRepository.delete(deckConfig);
        syncCache(deckConfig, true);
    }

    private void syncCache(DeckConfigEntity deckConfig, boolean isDelete) {
        if (isDelete) {
            cacheByName.remove(deckConfig.getDeckName());
            cacheById.remove(deckConfig.getId());
        } else {
            cacheByName.put(deckConfig.getDeckName(), deckConfig);
            cacheById.put(deckConfig.getId(), deckConfig);
        }
    }
}
