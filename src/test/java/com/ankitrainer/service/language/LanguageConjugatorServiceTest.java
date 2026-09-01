package com.ankitrainer.service.language;

import com.ankitrainer.language.Conjugator;
import com.ankitrainer.language.enums.ConjugationType;
import com.ankitrainer.language.enums.Language;
import com.ankitrainer.language.enums.PartOfSpeech;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LanguageConjugatorServiceTest {

    private Conjugator conjugator(Language language, PartOfSpeech pos) {
        Conjugator conjugator = mock(Conjugator.class);
        when(conjugator.getLanguage()).thenReturn(language);
        when(conjugator.getPartOfSpeech()).thenReturn(pos);
        return conjugator;
    }

    @Test
    void constructor_registersConjugatorForLanguageAndPos() {
        Conjugator verb = conjugator(Language.JAPANESE, PartOfSpeech.VERB);
        LanguageConjugatorService service = new LanguageConjugatorService(List.of(verb));

        assertThat(service.getSupportedConjugationTypes(Language.JAPANESE, PartOfSpeech.VERB))
                .isEmpty();
    }

    @Test
    void constructor_throwsOnDuplicateLanguageAndPos() {
        Conjugator first = conjugator(Language.JAPANESE, PartOfSpeech.VERB);
        Conjugator duplicate = conjugator(Language.JAPANESE, PartOfSpeech.VERB);

        assertThatThrownBy(() -> new LanguageConjugatorService(List.of(first, duplicate)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Duplicate conjugator")
                .hasMessageContaining(Language.JAPANESE.getKey())
                .hasMessageContaining(PartOfSpeech.VERB.getKey());
    }

    @Test
    void getSupportedConjugationTypes_delegatesToConjugator() {
        Conjugator verb = conjugator(Language.JAPANESE, PartOfSpeech.VERB);
        when(verb.getSupportedConjugationTypes()).thenReturn(Set.of(ConjugationType.JP_VERB_PAST));
        LanguageConjugatorService service = new LanguageConjugatorService(List.of(verb));

        assertThat(service.getSupportedConjugationTypes(Language.JAPANESE, PartOfSpeech.VERB))
                .containsExactly(ConjugationType.JP_VERB_PAST);
    }

    @Test
    void conjugate_forUnknownLanguage_throws() {
        LanguageConjugatorService service = new LanguageConjugatorService(List.of());

        assertThatThrownBy(() -> service.conjugate("見る", Language.JAPANESE, PartOfSpeech.VERB, ConjugationType.JP_VERB_PAST))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No conjugators found");
    }

    @Test
    void conjugate_forUnknownPartOfSpeech_throws() {
        Conjugator verb = conjugator(Language.JAPANESE, PartOfSpeech.VERB);
        LanguageConjugatorService service = new LanguageConjugatorService(List.of(verb));

        assertThatThrownBy(() -> service.conjugate("見る", Language.JAPANESE, null, ConjugationType.JP_VERB_PAST))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No conjugator found");
    }
}
