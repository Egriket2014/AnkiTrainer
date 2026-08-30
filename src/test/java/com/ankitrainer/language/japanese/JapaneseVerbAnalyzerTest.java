package com.ankitrainer.language.japanese;

import com.ankitrainer.language.enums.Language;
import com.ankitrainer.language.enums.PartOfSpeech;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JapaneseVerbAnalyzerTest {

    private final JapaneseVerbAnalyzer analyzer = new JapaneseVerbAnalyzer();

    @Test
    void matches_true_forGodanVerbs() {
        assertThat(analyzer.matches("話す")).isTrue();
        assertThat(analyzer.matches("書く")).isTrue();
        assertThat(analyzer.matches("飲む")).isTrue();
        assertThat(analyzer.matches("読む")).isTrue();
        assertThat(analyzer.matches("泳ぐ")).isTrue();
        assertThat(analyzer.matches("待つ")).isTrue();
        assertThat(analyzer.matches("取る")).isTrue();
    }

    @Test
    void matches_true_forIchidanVerbs() {
        assertThat(analyzer.matches("見る")).isTrue();
        assertThat(analyzer.matches("食べる")).isTrue();
        assertThat(analyzer.matches("寝る")).isTrue();
        assertThat(analyzer.matches("起きる")).isTrue();
        assertThat(analyzer.matches("教える")).isTrue();
    }

    @Test
    void matches_true_forIrregularVerbs() {
        assertThat(analyzer.matches("する")).isTrue();
        assertThat(analyzer.matches("来る")).isTrue();
        assertThat(analyzer.matches("行く")).isTrue();
    }

    @Test
    void matches_false_forNouns() {
        assertThat(analyzer.matches("本")).isFalse();
        assertThat(analyzer.matches("水")).isFalse();
        assertThat(analyzer.matches("猫")).isFalse();
        assertThat(analyzer.matches("学校")).isFalse();
        assertThat(analyzer.matches("車")).isFalse();
        assertThat(analyzer.matches("時間")).isFalse();
    }

    @Test
    void matches_false_forAdjectives() {
        assertThat(analyzer.matches("大きい")).isFalse();
        assertThat(analyzer.matches("高い")).isFalse();
        assertThat(analyzer.matches("新しい")).isFalse();
    }

    @Test
    void matches_false_forAdverbsAndParticles() {
        assertThat(analyzer.matches("とても")).isFalse();
        assertThat(analyzer.matches("が")).isFalse();
        assertThat(analyzer.matches("これ")).isFalse();
    }

    @Test
    void matches_false_forEmptyAndBlankStrings() {
        assertThat(analyzer.matches("")).isFalse();
        assertThat(analyzer.matches("   ")).isFalse();
        assertThat(analyzer.matches("\t")).isFalse();
    }

    @Test
    void matches_false_forDigits() {
        assertThat(analyzer.matches("123")).isFalse();
        assertThat(analyzer.matches("4.5")).isFalse();
        assertThat(analyzer.matches("２０２４")).isFalse();
    }

    @Test
    void matches_false_forLatinAndSymbols() {
        assertThat(analyzer.matches("abc")).isFalse();
        assertThat(analyzer.matches("hello")).isFalse();
        assertThat(analyzer.matches("日本語123")).isFalse();
    }

    @Test
    void getPartOfSpeech_returnsVerb() {
        assertThat(analyzer.getPartOfSpeech()).isEqualTo(PartOfSpeech.VERB);
    }

    @Test
    void getLanguage_returnsJapanese() {
        assertThat(analyzer.getLanguage()).isEqualTo(Language.JAPANESE);
    }
}
