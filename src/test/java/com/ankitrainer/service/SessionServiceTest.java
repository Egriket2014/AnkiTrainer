package com.ankitrainer.service;

import com.ankitrainer.entity.CardEntity;
import com.ankitrainer.entity.CardSrsEntity;
import com.ankitrainer.entity.DeckConfigEntity;
import com.ankitrainer.language.enums.ConjugationType;
import com.ankitrainer.language.enums.Language;
import com.ankitrainer.language.enums.PartOfSpeech;
import io.github.openspacedrepetition.Card;
import io.github.openspacedrepetition.Rating;
import io.github.openspacedrepetition.Scheduler;
import io.github.openspacedrepetition.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private DeckService deckService;

    @Mock
    private DeckConfigService deckConfigService;

    private Scheduler scheduler;

    private SessionService service;

    @BeforeEach
    void setUp() {
        scheduler = spy(Scheduler.builder().build());
        service = new SessionService();
        setField(service, "deckService", deckService);
        setField(service, "deckConfigService", deckConfigService);
        setField(service, "scheduler", scheduler);
    }

    private DeckConfigEntity deckConfig() {
        return DeckConfigEntity.builder()
                .id(1L)
                .deckName("ЯПОНСКИЙ")
                .language(Language.JAPANESE)
                .newLimit(10)
                .reviewLimit(5)
                .build();
    }

    private CardSrsEntity srsCard(String answer, Card srsCard) {
        return CardSrsEntity.builder()
                .id(1L)
                .card(CardEntity.builder().word("走る").build())
                .conjugationType(ConjugationType.JP_VERB_PAST)
                .answer(answer)
                .srsCard(srsCard)
                .build();
    }

    private void prepareSingleType2Card(CardSrsEntity card) {
        when(deckConfigService.getDeckConfigById(1L)).thenReturn(deckConfig());
        when(deckService.findSeenTodayNewCards(anyString(), any(ConjugationType.class), any()))
                .thenReturn(List.of(card));
        when(deckService.findNewCardsForToday(anyString(), any(ConjugationType.class), anyInt())).thenReturn(List.of());
        when(deckService.findSeenNotTodayNewCards(anyString(), any(ConjugationType.class), any())).thenReturn(List.of());
        when(deckService.findRelearningCards(anyString(), any(ConjugationType.class))).thenReturn(List.of());
        when(deckService.findReviewCards(anyString(), any(ConjugationType.class), anyInt())).thenReturn(List.of());

        service.prepareCards(1L, PartOfSpeech.VERB, Set.of(ConjugationType.JP_VERB_PAST));
    }

    private void prepareSingleLearningCard(String answer) {
        prepareSingleType2Card(srsCard(answer, Card.builder().build()));
    }

    private void prepareSingleMatureReviewCard(String answer) {
        Card mature = Card.builder()
                .state(State.REVIEW)
                .stability(10.0)
                .difficulty(4.0)
                .build();
        prepareSingleType2Card(srsCard(answer, mature));
    }

    @Test
    void isComplete_beforePrepare_returnsTrue() {
        assertThat(service.isComplete()).isTrue();
    }

    @Test
    void getCurrentCard_beforePrepare_returnsNull() {
        assertThat(service.getCurrentCard()).isNull();
    }

    @Test
    void getRGBStats_beforePrepare_returnsZeros() {
        assertThat(service.getRGBStats().getBlue()).isZero();
        assertThat(service.getRGBStats().getRed()).isZero();
        assertThat(service.getRGBStats().getGreen()).isZero();
    }

    @Test
    void getQueueCards_beforePrepare_returnsEmpty() {
        assertThat(service.getQueueCards()).isEmpty();
    }

    @Test
    void getCurrentCard_returnsPeekWithCards() {
        prepareSingleLearningCard("走った");

        assertThat(service.getCurrentCard()).isNotNull();
        assertThat(service.isComplete()).isFalse();
    }

    @Test
    void getQueueCards_afterPrepare_reflectsQueueState() {
        prepareSingleLearningCard("走った");

        assertThat(service.getQueueCards()).hasSize(1);
    }

    @Test
    void saveResults_clearsSession() {
        prepareSingleLearningCard("走った");
        service.saveResults();

        assertThat(service.isComplete()).isTrue();
        assertThat(service.getCurrentCard()).isNull();
    }


    @Test
    void checkAnswer_whenSessionComplete_throws() {
        assertThatThrownBy(() -> service.checkAnswer("anything"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("complete");
    }

    @Test
    void checkAnswer_correctAnswer_usesGoodRatingReturnsTrueAndSaves() {
        prepareSingleLearningCard("走った");
        boolean correct = service.checkAnswer("走った");

        assertThat(correct).isTrue();
        verify(scheduler).reviewCard(any(Card.class), eq(Rating.GOOD));
        verify(deckService).saveCardSrs(any(CardSrsEntity.class));
    }

    @Test
    void checkAnswer_wrongAnswer_usesAgainRatingAndReturnsFalse() {
        prepareSingleLearningCard("走った");
        boolean correct = service.checkAnswer("walked");

        assertThat(correct).isFalse();
        verify(scheduler).reviewCard(any(Card.class), eq(Rating.AGAIN));
    }

    @Test
    void checkAnswer_trimsUserAnswerBeforeComparing() {
        prepareSingleLearningCard("走った");
        boolean correct = service.checkAnswer("  走った  ");

        assertThat(correct).isTrue();
        verify(scheduler).reviewCard(any(Card.class), eq(Rating.GOOD));
    }

    @Test
    void checkAnswer_learningCardAfterReview_isReturnedToQueue() {
        prepareSingleLearningCard("走った");
        service.checkAnswer("走った");

        assertThat(service.isComplete()).isFalse();
        assertThat(service.getCurrentCard()).isNotNull();
        verify(deckService).saveCardSrs(any(CardSrsEntity.class));
    }

    @Test
    void checkAnswer_reviewCardAfterReview_isRemovedFromQueue() {
        prepareSingleMatureReviewCard("走った");
        service.checkAnswer("走った");

        assertThat(service.isComplete()).isTrue();
        assertThat(service.getCurrentCard()).isNull();
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
