package com.ankitrainer.service;

import com.ankitrainer.entity.DeckConfigEntity;
import com.ankitrainer.exception.DeckNotFoundException;
import com.ankitrainer.language.enums.Language;
import com.ankitrainer.mapper.DeckConfigMapper;
import com.ankitrainer.repository.DeckConfigRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeckConfigServiceTest {

    @Mock
    private DeckConfigRepository deckConfigRepository;

    @Mock
    private DeckConfigMapper deckConfigMapper;

    @Mock
    private DeckService deckService;

    private DeckConfigService newService() {
        DeckConfigService service = new DeckConfigService();
        setField(service, "deckConfigRepository", deckConfigRepository);
        setField(service, "deckConfigMapper", deckConfigMapper);
        setField(service, "deckService", deckService);
        return service;
    }

    private DeckConfigEntity deck(Long id, String name) {
        return DeckConfigEntity.builder()
                .id(id)
                .deckName(name)
                .modelName("model")
                .language(Language.JAPANESE)
                .wordField("Word")
                .translationField("Meaning")
                .build();
    }

    @Test
    void getDeckConfigById_fromCacheWhenPresent() {
        DeckConfigEntity d = deck(1L, "ЯПОНСКИЙ");
        DeckConfigService service = newService();
        when(deckConfigRepository.findAll()).thenReturn(List.of(d));
        service.initCache();
        DeckConfigEntity result = service.getDeckConfigById(1L);

        assertThat(result).isSameAs(d);
    }

    @Test
    void getDeckConfigById_fallbackToRepositoryWhenNotCached() {
        DeckConfigEntity d = deck(1L, "ЯПОНСКИЙ");
        DeckConfigService service = newService();
        when(deckConfigRepository.findById(1L)).thenReturn(Optional.of(d));
        DeckConfigEntity result = service.getDeckConfigById(1L);

        assertThat(result).isSameAs(d);
        verify(deckConfigRepository).findById(1L);
    }

    @Test
    void getDeckConfigById_notFound_throws() {
        DeckConfigService service = newService();
        when(deckConfigRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getDeckConfigById(99L))
                .isInstanceOf(DeckNotFoundException.class);
    }

    @Test
    void getDeckConfigByName_fallbackToRepositoryWhenNotCached() {
        DeckConfigEntity d = deck(1L, "ЯПОНСКИЙ");
        DeckConfigService service = newService();
        when(deckConfigRepository.findByDeckName("ЯПОНСКИЙ")).thenReturn(Optional.of(d));
        DeckConfigEntity result = service.getDeckConfigByName("ЯПОНСКИЙ");

        assertThat(result).isSameAs(d);
    }

    @Test
    void getDeckConfigByName_notFound_throws() {
        DeckConfigService service = newService();
        when(deckConfigRepository.findByDeckName("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getDeckConfigByName("missing"))
                .isInstanceOf(DeckNotFoundException.class);
    }

    @Test
    void getAllDeckConfigs_returnsCachedValues() {
        DeckConfigEntity d = deck(1L, "ЯПОНСКИЙ");
        DeckConfigService service = newService();
        when(deckConfigRepository.findAll()).thenReturn(List.of(d));
        service.initCache();
        List<DeckConfigEntity> result = service.getAllDeckConfigs();

        assertThat(result).containsExactly(d);
    }

    @Test
    void getAllDeckConfigs_fallbackToRepositoryWhenCacheEmpty() {
        DeckConfigEntity d = deck(1L, "ЯПОНСКИЙ");
        DeckConfigService service = newService();
        when(deckConfigRepository.findAll()).thenReturn(List.of(d));
        List<DeckConfigEntity> result = service.getAllDeckConfigs();

        assertThat(result).containsExactly(d);
    }

    @Test
    void createDeckConfig_blankName_throws() {
        DeckConfigService service = newService();
        DeckConfigEntity d = deck(null, "   ");

        assertThatThrownBy(() -> service.createDeckConfig(d))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Deck name cannot be empty");
    }

    @Test
    void createDeckConfig_duplicateName_throws() {
        DeckConfigService service = newService();
        DeckConfigEntity d = deck(null, "ЯПОНСКИЙ");
        when(deckConfigRepository.existsByDeckName("ЯПОНСКИЙ")).thenReturn(true);

        assertThatThrownBy(() -> service.createDeckConfig(d))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Deck already exists");
    }

    @Test
    void createDeckConfig_success_savesInRepository() {
        DeckConfigEntity d = deck(null, "ЯПОНСКИЙ");
        DeckConfigEntity saved = deck(7L, "ЯПОНСКИЙ");
        DeckConfigService service = newService();
        when(deckConfigRepository.existsByDeckName("ЯПОНСКИЙ")).thenReturn(false);
        when(deckConfigRepository.save(d)).thenReturn(saved);
        DeckConfigEntity result = service.createDeckConfig(d);

        assertThat(result).isSameAs(saved);
        verify(deckConfigRepository).save(d);
    }

    @Test
    void updateDeckConfig_nullId_throws() {
        DeckConfigService service = newService();
        DeckConfigEntity d = deck(null, "ЯПОНСКИЙ");

        assertThatThrownBy(() -> service.updateDeckConfig(d))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID is required");
    }

    @Test
    void deleteDeck_notFound_throws() {
        DeckConfigService service = newService();
        when(deckConfigRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteDeck(99L))
                .isInstanceOf(DeckNotFoundException.class);
    }

    @Test
    void deleteDeck_success_deletesAndRemovesFromCache() {
        DeckConfigEntity d = deck(1L, "ЯПОНСКИЙ");
        DeckConfigService service = newService();
        when(deckConfigRepository.findAll()).thenReturn(List.of(d));
        service.initCache();
        service.deleteDeck(1L);

        verify(deckConfigRepository).delete(d);

        when(deckConfigRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getDeckConfigById(1L))
                .isInstanceOf(DeckNotFoundException.class);
    }

    private void setField(Object target, String name, Object value) {
        try {
            var field = target.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot set field: " + name, e);
        }
    }
}
