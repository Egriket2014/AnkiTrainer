package com.ankitrainer.service.language;

import com.ankitrainer.language.PartOfSpeechAnalyzer;
import com.ankitrainer.language.enums.Language;
import com.ankitrainer.language.enums.PartOfSpeech;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LanguagePartOfSpeechAnalyzerServiceTest {

    private PartOfSpeechAnalyzer analyzer(Language language, PartOfSpeech pos) {
        PartOfSpeechAnalyzer analyzer = mock(PartOfSpeechAnalyzer.class);
        when(analyzer.getLanguage()).thenReturn(language);
        when(analyzer.getPartOfSpeech()).thenReturn(pos);
        return analyzer;
    }

    @Test
    void constructor_registersAnalyzerForLanguageAndPos() {
        PartOfSpeechAnalyzer verb = analyzer(Language.JAPANESE, PartOfSpeech.VERB);
        LanguagePartOfSpeechAnalyzerService service =
                new LanguagePartOfSpeechAnalyzerService(List.of(verb));

        assertThat(service.getSupportedPartsOfSpeech(Language.JAPANESE))
                .containsExactly(PartOfSpeech.VERB);
    }

    @Test
    void constructor_throwsOnDuplicateLanguageAndPos() {
        PartOfSpeechAnalyzer first = analyzer(Language.JAPANESE, PartOfSpeech.VERB);
        PartOfSpeechAnalyzer duplicate = analyzer(Language.JAPANESE, PartOfSpeech.VERB);

        assertThatThrownBy(() -> new LanguagePartOfSpeechAnalyzerService(List.of(first, duplicate)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Duplicate analyzer")
                .hasMessageContaining(Language.JAPANESE.getKey())
                .hasMessageContaining(PartOfSpeech.VERB.getKey());
    }

    @Test
    void noAnalyzersForLanguage_returnsEmptySet() {
        LanguagePartOfSpeechAnalyzerService service =
                new LanguagePartOfSpeechAnalyzerService(List.of());
        Set<PartOfSpeech> result = service.getSupportedPartsOfSpeech(Language.JAPANESE);

        assertThat(result).isEmpty();
    }

    @Test
    void detectPartOfSpeech_whenNoAnalyzersForLanguage_returnsNull() {
        LanguagePartOfSpeechAnalyzerService service =
                new LanguagePartOfSpeechAnalyzerService(List.of());

        assertThat(service.detectPartOfSpeech("見る", Language.JAPANESE)).isNull();
    }
}
