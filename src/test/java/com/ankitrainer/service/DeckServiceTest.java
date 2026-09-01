package com.ankitrainer.service;

import com.ankitrainer.dto.session.QueueStatsDto;
import com.ankitrainer.dto.sync.SyncDeckResultDto;
import com.ankitrainer.entity.CardEntity;
import com.ankitrainer.entity.CardSrsEntity;
import com.ankitrainer.entity.DeckConfigEntity;
import com.ankitrainer.exception.DeckNotFoundException;
import com.ankitrainer.language.enums.ConjugationType;
import com.ankitrainer.language.enums.Language;
import com.ankitrainer.language.enums.PartOfSpeech;
import com.ankitrainer.repository.CardRepository;
import com.ankitrainer.repository.CardSrsRepository;
import com.ankitrainer.repository.DeckConfigRepository;
import com.ankitrainer.service.anki.AnkiConnectService;
import com.ankitrainer.service.language.LanguageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeckServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardSrsRepository cardSrsRepository;

    @Mock
    private AnkiConnectService ankiConnectService;

    @Mock
    private LanguageService languageService;

    @Mock
    private DeckConfigRepository deckConfigRepository;

    private DeckService newService() {
        DeckService service = new DeckService();
        setField(service, "cardRepository", cardRepository);
        setField(service, "cardSrsRepository", cardSrsRepository);
        setField(service, "ankiConnectService", ankiConnectService);
        setField(service, "languageService", languageService);
        setField(service, "deckConfigRepository", deckConfigRepository);
        return service;
    }

    private DeckConfigEntity deck(Long id, String name, int newLimit, int reviewLimit) {
        return DeckConfigEntity.builder()
                .id(id)
                .deckName(name)
                .language(Language.JAPANESE)
                .newLimit(newLimit)
                .reviewLimit(reviewLimit)
                .build();
    }

    private CardEntity card(Long noteId, String word, String translation, String extra) {
        return CardEntity.builder()
                .noteId(noteId)
                .word(word)
                .translation(translation)
                .extra(extra)
                .partOfSpeech(PartOfSpeech.VERB)
                .deckName("ЯПОНСКИЙ")
                .build();
    }

    @Test
    void syncCards_allNewCardsAreCreatedAndCounted() {
        DeckService service = newService();
        CardEntity ankiCard = card(100L, "走る", "run", "read");
        when(deckConfigRepository.findById(1L)).thenReturn(Optional.of(deck(1L, "ЯПОНСКИЙ", 10, 10)));
        when(ankiConnectService.getSupportedCardsForDeck(any())).thenReturn(List.of(ankiCard));
        when(cardRepository.findAllByDeckName("ЯПОНСКИЙ")).thenReturn(List.of());
        when(cardRepository.existsByNoteId(100L)).thenReturn(false);
        when(cardRepository.saveAndFlush(any())).thenAnswer(inv -> inv.getArgument(0));
        SyncDeckResultDto result = service.syncCards(1L);

        assertThat(result.getCreated()).isEqualTo(1);
        assertThat(result.getUpdated()).isZero();
        assertThat(result.getDeleted()).isZero();
    }

    @Test
    void syncCards_existingCardUnchanged_noUpdateCounted() {
        DeckService service = newService();
        CardEntity ankiCard = card(100L, "走る", "run", "read");
        CardEntity dbCard = card(100L, "走る", "run", "read");
        when(deckConfigRepository.findById(1L)).thenReturn(Optional.of(deck(1L, "ЯПОНСКИЙ", 10, 10)));
        when(ankiConnectService.getSupportedCardsForDeck(any())).thenReturn(List.of(ankiCard));
        when(cardRepository.findAllByDeckName("ЯПОНСКИЙ")).thenReturn(List.of(dbCard));
        SyncDeckResultDto result = service.syncCards(1L);

        assertThat(result.getCreated()).isZero();
        assertThat(result.getUpdated()).isZero();
        assertThat(result.getDeleted()).isZero();
        verify(cardRepository, never()).save(any());
    }

    @Test
    void syncCards_changedFields_areUpdatedAndCounted() {
        DeckService service = newService();
        CardEntity ankiCard = card(100L, "走る", "run", "new-read");
        CardEntity dbCard = card(100L, "走る", "run", "old-read");
        when(deckConfigRepository.findById(1L)).thenReturn(Optional.of(deck(1L, "ЯПОНСКИЙ", 10, 10)));
        when(ankiConnectService.getSupportedCardsForDeck(any())).thenReturn(List.of(ankiCard));
        when(cardRepository.findAllByDeckName("ЯПОНСКИЙ")).thenReturn(List.of(dbCard));
        SyncDeckResultDto result = service.syncCards(1L);

        assertThat(result.getUpdated()).isEqualTo(1);
        assertThat(dbCard.getExtra()).isEqualTo("new-read");
        verify(cardRepository).save(dbCard);
    }

    @Test
    void syncCards_cardsMissingInAnki_areDeletedAndCounted() {
        DeckService service = newService();
        CardEntity dbCard = card(100L, "走る", "run", "read");
        when(deckConfigRepository.findById(1L)).thenReturn(Optional.of(deck(1L, "ЯПОНСКИЙ", 10, 10)));
        when(ankiConnectService.getSupportedCardsForDeck(any())).thenReturn(List.of());
        when(cardRepository.findAllByDeckName("ЯПОНСКИЙ")).thenReturn(List.of(dbCard));
        SyncDeckResultDto result = service.syncCards(1L);

        assertThat(result.getDeleted()).isEqualTo(1);
        verify(cardRepository).delete(dbCard);
    }

    @Test
    void syncCards_deckNotFound_throws() {
        DeckService service = newService();
        when(deckConfigRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.syncCards(99L))
                .isInstanceOf(DeckNotFoundException.class);
    }

    @Test
    void createCardsFromAnki_skipsExistingCardByNoteId() {
        DeckService service = newService();
        CardEntity c = card(100L, "走る", "run", "read");
        when(ankiConnectService.getSupportedCardsForDeck(any())).thenReturn(List.of(c));
        when(cardRepository.existsByNoteId(100L)).thenReturn(true);
        service.createCardsFromAnki(deck(1L, "ЯПОНСКИЙ", 10, 10));

        verify(cardRepository, never()).saveAndFlush(any());
    }

    @Test
    void createCardsFromAnki_skipsConjugationWhenWordIsBlank() {
        DeckService service = newService();
        CardEntity c = card(100L, "走る", "run", "read");
        when(ankiConnectService.getSupportedCardsForDeck(any())).thenReturn(List.of(c));
        when(cardRepository.existsByNoteId(100L)).thenReturn(false);
        when(cardRepository.saveAndFlush(any())).thenReturn(c);
        when(languageService.getSupportedConjugationTypes(Language.JAPANESE, PartOfSpeech.VERB))
                .thenReturn(Set.of(ConjugationType.JP_VERB_PAST));
        when(languageService.conjugate(anyString(), any(), any(), any())).thenReturn("  ");
        service.createCardsFromAnki(deck(1L, "ЯПОНСКИЙ", 10, 10));

        verify(cardSrsRepository, never()).saveAll(anyList());
    }

    @Test
    void createCardsFromAnki_createsSrsEntitiesForSuccessfulConjugations() {
        DeckService service = newService();
        CardEntity c = card(100L, "走る", "run", "read");
        when(ankiConnectService.getSupportedCardsForDeck(any())).thenReturn(List.of(c));
        when(cardRepository.existsByNoteId(100L)).thenReturn(false);
        when(cardRepository.saveAndFlush(any())).thenReturn(c);
        when(languageService.getSupportedConjugationTypes(Language.JAPANESE, PartOfSpeech.VERB))
                .thenReturn(Set.of(ConjugationType.JP_VERB_PAST));
        when(languageService.conjugate(anyString(), any(), any(), any())).thenReturn("走った");
        service.createCardsFromAnki(deck(1L, "ЯПОНСКИЙ", 10, 10));

        verify(cardSrsRepository).saveAll(anyList());
    }

    @Test
    void getRGBStats_computesBlueRedGreenWithinLimits() {
        DeckService service = newService();
        DeckConfigEntity config = deck(1L, "ЯПОНСКИЙ", 10, 5);
        when(deckConfigRepository.findById(1L)).thenReturn(Optional.of(config));

        // type1 = 30, type2 = 6, type3 = 3, type4 = 2, type5 = 20
        when(cardSrsRepository.countSeenTodayNewCards(anyString(), anyString(), anyString())).thenReturn(6);
        when(cardSrsRepository.countNewCardsForToday(anyString(), anyString())).thenReturn(30);
        when(cardSrsRepository.countSeenNotTodayNewCards(anyString(), anyString(), anyString())).thenReturn(3);
        when(cardSrsRepository.countRelearningCards(anyString(), anyString())).thenReturn(2);
        when(cardSrsRepository.countReviewCards(anyString(), anyString())).thenReturn(20);
        QueueStatsDto stats = service.getRGBStats(1L, ConjugationType.JP_VERB_PAST);

        assertThat(stats.getBlue()).isEqualTo(4);
        assertThat(stats.getRed()).isEqualTo(11);
        assertThat(stats.getGreen()).isEqualTo(5);
    }

    @Test
    void getRGBStats_blueIsZeroWhenSeenTodayExceedsNewLimit() {
        DeckService service = newService();
        DeckConfigEntity config = deck(1L, "ЯПОНСКИЙ", 10, 5);
        when(deckConfigRepository.findById(1L)).thenReturn(Optional.of(config));

        when(cardSrsRepository.countSeenTodayNewCards(anyString(), anyString(), anyString())).thenReturn(20);
        when(cardSrsRepository.countNewCardsForToday(anyString(), anyString())).thenReturn(5);
        when(cardSrsRepository.countSeenNotTodayNewCards(anyString(), anyString(), anyString())).thenReturn(0);
        when(cardSrsRepository.countRelearningCards(anyString(), anyString())).thenReturn(0);
        when(cardSrsRepository.countReviewCards(anyString(), anyString())).thenReturn(0);

        QueueStatsDto stats = service.getRGBStats(1L, ConjugationType.JP_VERB_PAST);

        assertThat(stats.getBlue()).isZero();
        assertThat(stats.getRed()).isEqualTo(20);
        assertThat(stats.getGreen()).isZero();
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
