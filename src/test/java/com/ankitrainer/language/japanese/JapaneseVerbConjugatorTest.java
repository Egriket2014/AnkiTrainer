package com.ankitrainer.language.japanese;

import org.junit.jupiter.api.Test;

import static com.ankitrainer.language.enums.ConjugationType.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class JapaneseVerbConjugatorTest {
    private final JapaneseVerbConjugator conjugator = new JapaneseVerbConjugator();

    @Test
    void testIchidanConjugations() {
        String verb = "見る";

        assertThat(conjugator.conjugate(verb, JP_VERB_TE)).isEqualTo("見て");
        assertThat(conjugator.conjugate(verb, JP_VERB_POLITE)).isEqualTo("見ます");
        assertThat(conjugator.conjugate(verb, JP_VERB_NEGATIVE)).isEqualTo("見ない");
        assertThat(conjugator.conjugate(verb, JP_VERB_NEGATIVE_POLITE)).isEqualTo("見ません");
        assertThat(conjugator.conjugate(verb, JP_VERB_PAST)).isEqualTo("見た");
        assertThat(conjugator.conjugate(verb, JP_VERB_PAST_POLITE)).isEqualTo("見ました");
        assertThat(conjugator.conjugate(verb, JP_VERB_PAST_NEGATIVE)).isEqualTo("見なかった");
        assertThat(conjugator.conjugate(verb, JP_VERB_PAST_NEGATIVE_POLITE)).isEqualTo("見ませんでした");
    }

    @Test
    void testGodanConjugations() {
        String verb = "話す";

        assertThat(conjugator.conjugate(verb, JP_VERB_TE)).isEqualTo("話して");
        assertThat(conjugator.conjugate(verb, JP_VERB_POLITE)).isEqualTo("話します");
        assertThat(conjugator.conjugate(verb, JP_VERB_NEGATIVE)).isEqualTo("話さない");
        assertThat(conjugator.conjugate(verb, JP_VERB_NEGATIVE_POLITE)).isEqualTo("話しません");
        assertThat(conjugator.conjugate(verb, JP_VERB_PAST)).isEqualTo("話した");
        assertThat(conjugator.conjugate(verb, JP_VERB_PAST_POLITE)).isEqualTo("話しました");
        assertThat(conjugator.conjugate(verb, JP_VERB_PAST_NEGATIVE)).isEqualTo("話さなかった");
        assertThat(conjugator.conjugate(verb, JP_VERB_PAST_NEGATIVE_POLITE)).isEqualTo("話しませんでした");
    }

    @Test
    void testIrregularVerbs() {
        assertThat(conjugator.conjugate("する", JP_VERB_TE)).isEqualTo("して");
        assertThat(conjugator.conjugate("する", JP_VERB_PAST)).isEqualTo("した");
        assertThat(conjugator.conjugate("する", JP_VERB_PAST_POLITE)).isEqualTo("しました");
        assertThat(conjugator.conjugate("する", JP_VERB_NEGATIVE)).isEqualTo("しない");
        assertThat(conjugator.conjugate("する", JP_VERB_NEGATIVE_POLITE)).isEqualTo("しません");

        assertThat(conjugator.conjugate("来る", JP_VERB_TE)).isEqualTo("来て");
        assertThat(conjugator.conjugate("来る", JP_VERB_PAST)).isEqualTo("来た");
        assertThat(conjugator.conjugate("来る", JP_VERB_PAST_POLITE)).isEqualTo("来ました");
        assertThat(conjugator.conjugate("来る", JP_VERB_NEGATIVE)).isEqualTo("来ない");
        assertThat(conjugator.conjugate("来る", JP_VERB_NEGATIVE_POLITE)).isEqualTo("来ません");

        assertThat(conjugator.conjugate("行く", JP_VERB_TE)).isEqualTo("行って");
        assertThat(conjugator.conjugate("行く", JP_VERB_PAST)).isEqualTo("行った");
        assertThat(conjugator.conjugate("行く", JP_VERB_PAST_POLITE)).isEqualTo("行きました");
        assertThat(conjugator.conjugate("行く", JP_VERB_NEGATIVE)).isEqualTo("行かない");
        assertThat(conjugator.conjugate("行く", JP_VERB_NEGATIVE_POLITE)).isEqualTo("行きません");
    }

    @Test
    void testGodanExceptions() {
        assertThat(conjugator.conjugate("要る", JP_VERB_TE)).isEqualTo("要って");
        assertThat(conjugator.conjugate("要る", JP_VERB_PAST)).isEqualTo("要った");
        assertThat(conjugator.conjugate("要る", JP_VERB_NEGATIVE)).isEqualTo("要らない");
    }

    @Test
    void testTeForm_GodanVerbs() {
        assertThat(conjugator.conjugate("話す", JP_VERB_TE)).isEqualTo("話して");
        assertThat(conjugator.conjugate("出す", JP_VERB_TE)).isEqualTo("出して");
        assertThat(conjugator.conjugate("待つ", JP_VERB_TE)).isEqualTo("待って");
        assertThat(conjugator.conjugate("立つ", JP_VERB_TE)).isEqualTo("立って");
        assertThat(conjugator.conjugate("取る", JP_VERB_TE)).isEqualTo("取って");
        assertThat(conjugator.conjugate("飲む", JP_VERB_TE)).isEqualTo("飲んで");
        assertThat(conjugator.conjugate("読む", JP_VERB_TE)).isEqualTo("読んで");
        assertThat(conjugator.conjugate("泳ぐ", JP_VERB_TE)).isEqualTo("泳いで");
        assertThat(conjugator.conjugate("死ぬ", JP_VERB_TE)).isEqualTo("死んで");
        assertThat(conjugator.conjugate("行く", JP_VERB_TE)).isEqualTo("行って");
        assertThat(conjugator.conjugate("切る", JP_VERB_TE)).isEqualTo("切って");
        assertThat(conjugator.conjugate("帰る", JP_VERB_TE)).isEqualTo("帰って");
        assertThat(conjugator.conjugate("走る", JP_VERB_TE)).isEqualTo("走って");
        assertThat(conjugator.conjugate("入る", JP_VERB_TE)).isEqualTo("入って");
        assertThat(conjugator.conjugate("知る", JP_VERB_TE)).isEqualTo("知って");
    }

    @Test
    void testTeForm_IchidanVerbs() {
        assertThat(conjugator.conjugate("見る", JP_VERB_TE)).isEqualTo("見て");
        assertThat(conjugator.conjugate("食べる", JP_VERB_TE)).isEqualTo("食べて");
        assertThat(conjugator.conjugate("寝る", JP_VERB_TE)).isEqualTo("寝て");
        assertThat(conjugator.conjugate("起きる", JP_VERB_TE)).isEqualTo("起きて");
        assertThat(conjugator.conjugate("開ける", JP_VERB_TE)).isEqualTo("開けて");
        assertThat(conjugator.conjugate("閉める", JP_VERB_TE)).isEqualTo("閉めて");
        assertThat(conjugator.conjugate("教える", JP_VERB_TE)).isEqualTo("教えて");
        assertThat(conjugator.conjugate("着る", JP_VERB_TE)).isEqualTo("着て");
        assertThat(conjugator.conjugate("浴びる", JP_VERB_TE)).isEqualTo("浴びて");
        assertThat(conjugator.conjugate("感じる", JP_VERB_TE)).isEqualTo("感じて");
    }

    @Test
    void testTeForm_IrregularVerbs() {
        assertThat(conjugator.conjugate("する", JP_VERB_TE)).isEqualTo("して");
        assertThat(conjugator.conjugate("来る", JP_VERB_TE)).isEqualTo("来て");
    }

    
    @Test
    void testPoliteForm() {
        assertThat(conjugator.conjugate("話す", JP_VERB_POLITE)).isEqualTo("話します");
        assertThat(conjugator.conjugate("待つ", JP_VERB_POLITE)).isEqualTo("待ちます");
        assertThat(conjugator.conjugate("取る", JP_VERB_POLITE)).isEqualTo("取ります");
        assertThat(conjugator.conjugate("飲む", JP_VERB_POLITE)).isEqualTo("飲みます");
        assertThat(conjugator.conjugate("読む", JP_VERB_POLITE)).isEqualTo("読みます");
        assertThat(conjugator.conjugate("行く", JP_VERB_POLITE)).isEqualTo("行きます");
        assertThat(conjugator.conjugate("見る", JP_VERB_POLITE)).isEqualTo("見ます");
        assertThat(conjugator.conjugate("食べる", JP_VERB_POLITE)).isEqualTo("食べます");
        assertThat(conjugator.conjugate("する", JP_VERB_POLITE)).isEqualTo("します");
        assertThat(conjugator.conjugate("来る", JP_VERB_POLITE)).isEqualTo("来ます");
    }

    
    @Test
    void testNegativeForm() {
        assertThat(conjugator.conjugate("話す", JP_VERB_NEGATIVE)).isEqualTo("話さない");
        assertThat(conjugator.conjugate("待つ", JP_VERB_NEGATIVE)).isEqualTo("待たない");
        assertThat(conjugator.conjugate("取る", JP_VERB_NEGATIVE)).isEqualTo("取らない");
        assertThat(conjugator.conjugate("飲む", JP_VERB_NEGATIVE)).isEqualTo("飲まない");
        assertThat(conjugator.conjugate("読む", JP_VERB_NEGATIVE)).isEqualTo("読まない");
        assertThat(conjugator.conjugate("行く", JP_VERB_NEGATIVE)).isEqualTo("行かない");
        assertThat(conjugator.conjugate("見る", JP_VERB_NEGATIVE)).isEqualTo("見ない");
        assertThat(conjugator.conjugate("食べる", JP_VERB_NEGATIVE)).isEqualTo("食べない");
        assertThat(conjugator.conjugate("する", JP_VERB_NEGATIVE)).isEqualTo("しない");
        assertThat(conjugator.conjugate("来る", JP_VERB_NEGATIVE)).isEqualTo("来ない");
    }

    
    @Test
    void testNegativePoliteForm() {
        assertThat(conjugator.conjugate("話す", JP_VERB_NEGATIVE_POLITE)).isEqualTo("話しません");
        assertThat(conjugator.conjugate("待つ", JP_VERB_NEGATIVE_POLITE)).isEqualTo("待ちません");
        assertThat(conjugator.conjugate("取る", JP_VERB_NEGATIVE_POLITE)).isEqualTo("取りません");
        assertThat(conjugator.conjugate("飲む", JP_VERB_NEGATIVE_POLITE)).isEqualTo("飲みません");
        assertThat(conjugator.conjugate("読む", JP_VERB_NEGATIVE_POLITE)).isEqualTo("読みません");
        assertThat(conjugator.conjugate("行く", JP_VERB_NEGATIVE_POLITE)).isEqualTo("行きません");
        assertThat(conjugator.conjugate("見る", JP_VERB_NEGATIVE_POLITE)).isEqualTo("見ません");
        assertThat(conjugator.conjugate("食べる", JP_VERB_NEGATIVE_POLITE)).isEqualTo("食べません");
        assertThat(conjugator.conjugate("する", JP_VERB_NEGATIVE_POLITE)).isEqualTo("しません");
        assertThat(conjugator.conjugate("来る", JP_VERB_NEGATIVE_POLITE)).isEqualTo("来ません");
    }

    
    @Test
    void testPastForm() {
        assertThat(conjugator.conjugate("話す", JP_VERB_PAST)).isEqualTo("話した");
        assertThat(conjugator.conjugate("待つ", JP_VERB_PAST)).isEqualTo("待った");
        assertThat(conjugator.conjugate("取る", JP_VERB_PAST)).isEqualTo("取った");
        assertThat(conjugator.conjugate("飲む", JP_VERB_PAST)).isEqualTo("飲んだ");
        assertThat(conjugator.conjugate("泳ぐ", JP_VERB_PAST)).isEqualTo("泳いだ");
        assertThat(conjugator.conjugate("行く", JP_VERB_PAST)).isEqualTo("行った");
        assertThat(conjugator.conjugate("見る", JP_VERB_PAST)).isEqualTo("見た");
        assertThat(conjugator.conjugate("食べる", JP_VERB_PAST)).isEqualTo("食べた");
        assertThat(conjugator.conjugate("する", JP_VERB_PAST)).isEqualTo("した");
        assertThat(conjugator.conjugate("来る", JP_VERB_PAST)).isEqualTo("来た");
    }

    
    @Test
    void testPastPoliteForm() {
        assertThat(conjugator.conjugate("話す", JP_VERB_PAST_POLITE)).isEqualTo("話しました");
        assertThat(conjugator.conjugate("待つ", JP_VERB_PAST_POLITE)).isEqualTo("待ちました");
        assertThat(conjugator.conjugate("取る", JP_VERB_PAST_POLITE)).isEqualTo("取りました");
        assertThat(conjugator.conjugate("飲む", JP_VERB_PAST_POLITE)).isEqualTo("飲みました");
        assertThat(conjugator.conjugate("読む", JP_VERB_PAST_POLITE)).isEqualTo("読みました");
        assertThat(conjugator.conjugate("行く", JP_VERB_PAST_POLITE)).isEqualTo("行きました");
        assertThat(conjugator.conjugate("見る", JP_VERB_PAST_POLITE)).isEqualTo("見ました");
        assertThat(conjugator.conjugate("食べる", JP_VERB_PAST_POLITE)).isEqualTo("食べました");
        assertThat(conjugator.conjugate("する", JP_VERB_PAST_POLITE)).isEqualTo("しました");
        assertThat(conjugator.conjugate("来る", JP_VERB_PAST_POLITE)).isEqualTo("来ました");
    }

    
    @Test
    void testPastNegativeForm() {
        assertThat(conjugator.conjugate("話す", JP_VERB_PAST_NEGATIVE)).isEqualTo("話さなかった");
        assertThat(conjugator.conjugate("待つ", JP_VERB_PAST_NEGATIVE)).isEqualTo("待たなかった");
        assertThat(conjugator.conjugate("取る", JP_VERB_PAST_NEGATIVE)).isEqualTo("取らなかった");
        assertThat(conjugator.conjugate("飲む", JP_VERB_PAST_NEGATIVE)).isEqualTo("飲まなかった");
        assertThat(conjugator.conjugate("読む", JP_VERB_PAST_NEGATIVE)).isEqualTo("読まなかった");
        assertThat(conjugator.conjugate("行く", JP_VERB_PAST_NEGATIVE)).isEqualTo("行かなかった");
        assertThat(conjugator.conjugate("見る", JP_VERB_PAST_NEGATIVE)).isEqualTo("見なかった");
        assertThat(conjugator.conjugate("食べる", JP_VERB_PAST_NEGATIVE)).isEqualTo("食べなかった");
        assertThat(conjugator.conjugate("する", JP_VERB_PAST_NEGATIVE)).isEqualTo("しなかった");
        assertThat(conjugator.conjugate("来る", JP_VERB_PAST_NEGATIVE)).isEqualTo("来なかった");
    }

    
    @Test
    void testPastNegativePoliteForm() {
        assertThat(conjugator.conjugate("話す", JP_VERB_PAST_NEGATIVE_POLITE)).isEqualTo("話しませんでした");
        assertThat(conjugator.conjugate("待つ", JP_VERB_PAST_NEGATIVE_POLITE)).isEqualTo("待ちませんでした");
        assertThat(conjugator.conjugate("取る", JP_VERB_PAST_NEGATIVE_POLITE)).isEqualTo("取りませんでした");
        assertThat(conjugator.conjugate("飲む", JP_VERB_PAST_NEGATIVE_POLITE)).isEqualTo("飲みませんでした");
        assertThat(conjugator.conjugate("読む", JP_VERB_PAST_NEGATIVE_POLITE)).isEqualTo("読みませんでした");
        assertThat(conjugator.conjugate("行く", JP_VERB_PAST_NEGATIVE_POLITE)).isEqualTo("行きませんでした");
        assertThat(conjugator.conjugate("見る", JP_VERB_PAST_NEGATIVE_POLITE)).isEqualTo("見ませんでした");
        assertThat(conjugator.conjugate("食べる", JP_VERB_PAST_NEGATIVE_POLITE)).isEqualTo("食べませんでした");
        assertThat(conjugator.conjugate("する", JP_VERB_PAST_NEGATIVE_POLITE)).isEqualTo("しませんでした");
        assertThat(conjugator.conjugate("来る", JP_VERB_PAST_NEGATIVE_POLITE)).isEqualTo("来ませんでした");
    }

    @Test
    void testAru_AllForms() {
        assertThat(conjugator.conjugate("ある", JP_VERB_TE)).isEqualTo("あって");
        assertThat(conjugator.conjugate("ある", JP_VERB_POLITE)).isEqualTo("あります");
        assertThat(conjugator.conjugate("ある", JP_VERB_NEGATIVE)).isEqualTo("ない");
        assertThat(conjugator.conjugate("ある", JP_VERB_NEGATIVE_POLITE)).isEqualTo("ありません");
        assertThat(conjugator.conjugate("ある", JP_VERB_PAST)).isEqualTo("あった");
        assertThat(conjugator.conjugate("ある", JP_VERB_PAST_POLITE)).isEqualTo("ありました");
        assertThat(conjugator.conjugate("ある", JP_VERB_PAST_NEGATIVE)).isEqualTo("なかった");
        assertThat(conjugator.conjugate("ある", JP_VERB_PAST_NEGATIVE_POLITE)).isEqualTo("ありませんでした");
    }

    @Test
    void testIru_Ichidan_AllForms() {
        assertThat(conjugator.conjugate("いる", JP_VERB_TE)).isEqualTo("いて");
        assertThat(conjugator.conjugate("いる", JP_VERB_POLITE)).isEqualTo("います");
        assertThat(conjugator.conjugate("いる", JP_VERB_NEGATIVE)).isEqualTo("いない");
        assertThat(conjugator.conjugate("いる", JP_VERB_NEGATIVE_POLITE)).isEqualTo("いません");
        assertThat(conjugator.conjugate("いる", JP_VERB_PAST)).isEqualTo("いた");
        assertThat(conjugator.conjugate("いる", JP_VERB_PAST_POLITE)).isEqualTo("いました");
        assertThat(conjugator.conjugate("いる", JP_VERB_PAST_NEGATIVE)).isEqualTo("いなかった");
        assertThat(conjugator.conjugate("いる", JP_VERB_PAST_NEGATIVE_POLITE)).isEqualTo("いませんでした");
    }

    @Test
    void testIru_GodanException_AllForms() {
        assertThat(conjugator.conjugate("要る", JP_VERB_TE)).isEqualTo("要って");
        assertThat(conjugator.conjugate("要る", JP_VERB_POLITE)).isEqualTo("要ります");
        assertThat(conjugator.conjugate("要る", JP_VERB_NEGATIVE)).isEqualTo("要らない");
        assertThat(conjugator.conjugate("要る", JP_VERB_NEGATIVE_POLITE)).isEqualTo("要りません");
        assertThat(conjugator.conjugate("要る", JP_VERB_PAST)).isEqualTo("要った");
        assertThat(conjugator.conjugate("要る", JP_VERB_PAST_POLITE)).isEqualTo("要りました");
        assertThat(conjugator.conjugate("要る", JP_VERB_PAST_NEGATIVE)).isEqualTo("要らなかった");
        assertThat(conjugator.conjugate("要る", JP_VERB_PAST_NEGATIVE_POLITE)).isEqualTo("要りませんでした");
    }

    @Test
    void testIruAmbiguity() {
        assertThat(conjugator.conjugate("いる", JP_VERB_TE)).isEqualTo("いて");
        assertThat(conjugator.conjugate("要る", JP_VERB_TE)).isEqualTo("要って");
    }
}