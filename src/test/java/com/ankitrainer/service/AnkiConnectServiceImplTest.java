package com.ankitrainer.service;

import com.ankitrainer.ankiconnect.AnkiConnectClient;
import com.ankitrainer.entity.CardEntity;
import com.ankitrainer.entity.DeckConfigEntity;
import com.ankitrainer.language.enums.Language;
import com.ankitrainer.language.enums.PartOfSpeech;
import com.ankitrainer.service.anki.AnkiConnectServiceImpl;
import com.ankitrainer.service.language.LanguageService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnkiConnectServiceImplTest {

    private static final String DECK = "ЯПОНСКИЙ";
    private static final String MODEL = "Japanese Sentence Card";

    @Mock
    private AnkiConnectClient client;

    @Mock
    private LanguageService languageService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private AnkiConnectServiceImpl service;

    private DeckConfigEntity deckConfig;

    @BeforeEach
    void setUp() {
        service = new AnkiConnectServiceImpl();
        setField(service, "client", client);
        setField(service, "objectMapper", objectMapper);
        setField(service, "languageService", languageService);

        deckConfig = DeckConfigEntity.builder()
                .deckName(DECK)
                .modelName(MODEL)
                .wordField("Word")
                .translationField("Meaning")
                .extraField("Reading")
                .language(Language.JAPANESE)
                .build();
    }

    @Test
    void getDecksNames_returnsDecksFromClientResponse() {
        when(client.getDeckNames()).thenReturn(node(List.of("Basic", "ЯПОНСКИЙ")));

        List<String> result = service.getDecksNames();

        assertThat(result).containsExactly("Basic", "ЯПОНСКИЙ");
        verify(client).getDeckNames();
    }

    @Test
    void getModelNames_returnsModelsFromClientResponse() {
        when(client.getModelNames()).thenReturn(node(List.of("Basic", MODEL)));

        List<String> result = service.getModelNames();

        assertThat(result).containsExactly("Basic", MODEL);
        verify(client).getModelNames();
    }

    @Test
    void getModelFieldNames_returnsFieldsFromClientResponse() {
        when(client.getFieldNamesForModel(MODEL)).thenReturn(node(List.of("Word", "Meaning", "Reading")));

        List<String> result = service.getModelFieldNames(MODEL);

        assertThat(result).containsExactly("Word", "Meaning", "Reading");
        verify(client).getFieldNamesForModel(MODEL);
    }

    @Test
    void getSupportedCardsForDeck_emptyDeck_returnsEmptyAndDoesNotFetchNotesInfo() {
        when(client.getNotesIdsForDeck(DECK)).thenReturn(node(List.of()));

        List<CardEntity> result = service.getSupportedCardsForDeck(deckConfig);

        assertThat(result).isEmpty();
        verify(client, never()).getNotesInfoByIds(anyList());
    }

    @Test
    void getSupportedCardsForDeck_filtersNotes_andBuildsCard() {
        when(client.getNotesIdsForDeck(DECK)).thenReturn(node(List.of(100L, 101L, 102L, 103L)));
        when(client.getNotesInfoByIds(anyList())).thenReturn(node(List.of(
                note(100L, MODEL, "走る", "走", "read1"),
                note(101L, "WrongModel", "食べる", "食べ", "read2"),
                note(102L, MODEL, "  空  ", "空", "read3"),
                note(103L, MODEL, "", "", "read4")
        )));
        when(languageService.getSupportedPartsOfSpeech(Language.JAPANESE))
                .thenReturn(Set.of(PartOfSpeech.VERB));
        when(languageService.detectPartOfSpeech(anyString(), any()))
                .thenAnswer(inv -> "走る".equals(inv.getArgument(0)) ? PartOfSpeech.VERB : PartOfSpeech.VERB);

        List<CardEntity> result = service.getSupportedCardsForDeck(deckConfig);

        assertThat(result).hasSize(2);
        assertThat(result.get(0))
                .satisfies(c -> {
                    assertThat(c.getNoteId()).isEqualTo(100L);
                    assertThat(c.getWord()).isEqualTo("走る");
                    assertThat(c.getTranslation()).isEqualTo("走");
                    assertThat(c.getExtra()).isEqualTo("read1");
                    assertThat(c.getDeckName()).isEqualTo(DECK);
                    assertThat(c.getPartOfSpeech()).isEqualTo(PartOfSpeech.VERB);
                });
        assertThat(result.get(1).getNoteId()).isEqualTo(102L);
        assertThat(result.get(1).getWord()).isEqualTo("空");
        verify(client).getNotesInfoByIds(List.of(100L, 101L, 102L, 103L));
    }

    @Test
    void getSupportedCardsForDeck_filtersUnsupportedPartOfSpeech() {
        when(client.getNotesIdsForDeck(DECK)).thenReturn(node(List.of(100L, 101L)));
        when(client.getNotesInfoByIds(anyList())).thenReturn(node(List.of(
                note(100L, MODEL, "走る", "走", "read1"),
                note(101L, MODEL, "食べる", "食べ", "read2")
        )));
        when(languageService.getSupportedPartsOfSpeech(Language.JAPANESE))
                .thenReturn(Set.of(PartOfSpeech.VERB));
        when(languageService.detectPartOfSpeech(anyString(), any()))
                .thenReturn(PartOfSpeech.VERB);

        List<CardEntity> result = service.getSupportedCardsForDeck(deckConfig);

        assertThat(result).hasSize(2);
    }

    @Test
    void getSupportedCardsForDeck_emptyExtraField_setsExtraToNull() {
        DeckConfigEntity config = DeckConfigEntity.builder()
                .deckName(DECK)
                .modelName(MODEL)
                .wordField("Word")
                .translationField("Meaning")
                .extraField("   ")
                .language(Language.JAPANESE)
                .build();

        when(client.getNotesIdsForDeck(DECK)).thenReturn(node(List.of(100L)));
        when(client.getNotesInfoByIds(anyList())).thenReturn(node(List.of(
                note(100L, MODEL, "走る", "走", "read")
        )));
        when(languageService.getSupportedPartsOfSpeech(Language.JAPANESE))
                .thenReturn(Set.of(PartOfSpeech.VERB));
        when(languageService.detectPartOfSpeech(anyString(), any()))
                .thenReturn(PartOfSpeech.VERB);

        List<CardEntity> result = service.getSupportedCardsForDeck(config);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getExtra()).isNull();
    }

    private JsonNode node(Object value) {
        return objectMapper.createObjectNode().set("result", objectMapper.valueToTree(value));
    }

    private JsonNode note(Long noteId, String modelName, String word, String meaning, String reading) {
        return objectMapper.createObjectNode()
                .put("noteId", noteId)
                .put("modelName", modelName)
                .set("fields", fields(word, meaning, reading));
    }

    private ObjectNode fields(String word, String meaning, String reading) {
        ObjectNode fields = objectMapper.createObjectNode();
        fields.set("Word", valueField(word));
        fields.set("Meaning", valueField(meaning));
        fields.set("Reading", valueField(reading));
        return fields;
    }

    private JsonNode valueField(String value) {
        ObjectNode field = objectMapper.createObjectNode();
        field.set("value", value == null ? objectMapper.nullNode() : objectMapper.getNodeFactory().textNode(value));
        return field;
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
