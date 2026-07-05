package com.ankitrainer.language.japanese;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class JapaneseConjugatorTest {
    private final JapaneseConjugator conjugator = new JapaneseConjugator();

    @Test
    void testIchidanConjugations() {
        String verb = "見る";

        assertThat(conjugator.conjugate(verb, "te")).isEqualTo("見て");
        assertThat(conjugator.conjugate(verb, "polite")).isEqualTo("見ます");
        assertThat(conjugator.conjugate(verb, "negative")).isEqualTo("見ない");
        assertThat(conjugator.conjugate(verb, "negative_polite")).isEqualTo("見ません");
        assertThat(conjugator.conjugate(verb, "past")).isEqualTo("見た");
        assertThat(conjugator.conjugate(verb, "past_polite")).isEqualTo("見ました");
        assertThat(conjugator.conjugate(verb, "past_negative")).isEqualTo("見なかった");
        assertThat(conjugator.conjugate(verb, "past_negative_polite")).isEqualTo("見ませんでした");
    }

    @Test
    void testGodanConjugations() {
        String verb = "話す";

        assertThat(conjugator.conjugate(verb, "te")).isEqualTo("話して");
        assertThat(conjugator.conjugate(verb, "polite")).isEqualTo("話します");
        assertThat(conjugator.conjugate(verb, "negative")).isEqualTo("話さない");
        assertThat(conjugator.conjugate(verb, "negative_polite")).isEqualTo("話しません");
        assertThat(conjugator.conjugate(verb, "past")).isEqualTo("話した");
        assertThat(conjugator.conjugate(verb, "past_polite")).isEqualTo("話しました");
        assertThat(conjugator.conjugate(verb, "past_negative")).isEqualTo("話さなかった");
        assertThat(conjugator.conjugate(verb, "past_negative_polite")).isEqualTo("話しませんでした");
    }

    @Test
    void testIrregularVerbs() {
        // する
        assertThat(conjugator.conjugate("する", "te")).isEqualTo("して");
        assertThat(conjugator.conjugate("する", "past")).isEqualTo("した");
        assertThat(conjugator.conjugate("する", "past_polite")).isEqualTo("しました");
        assertThat(conjugator.conjugate("する", "negative")).isEqualTo("しない");
        assertThat(conjugator.conjugate("する", "negative_polite")).isEqualTo("しません");

        // 来る
        assertThat(conjugator.conjugate("来る", "te")).isEqualTo("来て");
        assertThat(conjugator.conjugate("来る", "past")).isEqualTo("来た");
        assertThat(conjugator.conjugate("来る", "past_polite")).isEqualTo("来ました");
        assertThat(conjugator.conjugate("来る", "negative")).isEqualTo("来ない");
        assertThat(conjugator.conjugate("来る", "negative_polite")).isEqualTo("来ません");

        // 行く — специальное исключение
        assertThat(conjugator.conjugate("行く", "te")).isEqualTo("行って");
        assertThat(conjugator.conjugate("行く", "past")).isEqualTo("行った");
        assertThat(conjugator.conjugate("行く", "past_polite")).isEqualTo("行きました");
        assertThat(conjugator.conjugate("行く", "negative")).isEqualTo("行かない");
        assertThat(conjugator.conjugate("行く", "negative_polite")).isEqualTo("行きません");
    }

    @Test
    void testGodanExceptions() {
        // 要る — исключение из 一段, на самом деле 五段
        assertThat(conjugator.conjugate("要る", "te")).isEqualTo("要って");
        assertThat(conjugator.conjugate("要る", "past")).isEqualTo("要った");
        assertThat(conjugator.conjugate("要る", "negative")).isEqualTo("要らない");
    }

    @Test
    void testTeForm_GodanVerbs() {
        assertThat(conjugator.conjugate("話す", "te")).isEqualTo("話して");
        assertThat(conjugator.conjugate("出す", "te")).isEqualTo("出して");
        assertThat(conjugator.conjugate("待つ", "te")).isEqualTo("待って");
        assertThat(conjugator.conjugate("立つ", "te")).isEqualTo("立って");
        assertThat(conjugator.conjugate("取る", "te")).isEqualTo("取って");
        assertThat(conjugator.conjugate("飲む", "te")).isEqualTo("飲んで");
        assertThat(conjugator.conjugate("読む", "te")).isEqualTo("読んで");
        assertThat(conjugator.conjugate("泳ぐ", "te")).isEqualTo("泳いで");
        assertThat(conjugator.conjugate("死ぬ", "te")).isEqualTo("死んで");
        assertThat(conjugator.conjugate("行く", "te")).isEqualTo("行って"); // исключение!
        assertThat(conjugator.conjugate("切る", "te")).isEqualTo("切って");
        assertThat(conjugator.conjugate("帰る", "te")).isEqualTo("帰って");
        assertThat(conjugator.conjugate("走る", "te")).isEqualTo("走って");
        assertThat(conjugator.conjugate("入る", "te")).isEqualTo("入って");
        assertThat(conjugator.conjugate("知る", "te")).isEqualTo("知って");
    }

    @Test
    void testTeForm_IchidanVerbs() {
        assertThat(conjugator.conjugate("見る", "te")).isEqualTo("見て");
        assertThat(conjugator.conjugate("食べる", "te")).isEqualTo("食べて");
        assertThat(conjugator.conjugate("寝る", "te")).isEqualTo("寝て");
        assertThat(conjugator.conjugate("起きる", "te")).isEqualTo("起きて");
        assertThat(conjugator.conjugate("開ける", "te")).isEqualTo("開けて");
        assertThat(conjugator.conjugate("閉める", "te")).isEqualTo("閉めて");
        assertThat(conjugator.conjugate("教える", "te")).isEqualTo("教えて");
        assertThat(conjugator.conjugate("着る", "te")).isEqualTo("着て");
        assertThat(conjugator.conjugate("浴びる", "te")).isEqualTo("浴びて");
        assertThat(conjugator.conjugate("感じる", "te")).isEqualTo("感じて");
    }

    @Test
    void testTeForm_IrregularVerbs() {
        assertThat(conjugator.conjugate("する", "te")).isEqualTo("して");
        assertThat(conjugator.conjugate("来る", "te")).isEqualTo("来て");
    }

    // ---------- Вежливая форма (polite) ----------

    @Test
    void testPoliteForm() {
        assertThat(conjugator.conjugate("話す", "polite")).isEqualTo("話します");
        assertThat(conjugator.conjugate("待つ", "polite")).isEqualTo("待ちます");
        assertThat(conjugator.conjugate("取る", "polite")).isEqualTo("取ります");
        assertThat(conjugator.conjugate("飲む", "polite")).isEqualTo("飲みます");
        assertThat(conjugator.conjugate("読む", "polite")).isEqualTo("読みます");
        assertThat(conjugator.conjugate("行く", "polite")).isEqualTo("行きます");
        assertThat(conjugator.conjugate("見る", "polite")).isEqualTo("見ます");
        assertThat(conjugator.conjugate("食べる", "polite")).isEqualTo("食べます");
        assertThat(conjugator.conjugate("する", "polite")).isEqualTo("します");
        assertThat(conjugator.conjugate("来る", "polite")).isEqualTo("来ます");
    }

    // ---------- Отрицательная форма (negative) ----------

    @Test
    void testNegativeForm() {
        assertThat(conjugator.conjugate("話す", "negative")).isEqualTo("話さない");
        assertThat(conjugator.conjugate("待つ", "negative")).isEqualTo("待たない");
        assertThat(conjugator.conjugate("取る", "negative")).isEqualTo("取らない");
        assertThat(conjugator.conjugate("飲む", "negative")).isEqualTo("飲まない");
        assertThat(conjugator.conjugate("読む", "negative")).isEqualTo("読まない");
        assertThat(conjugator.conjugate("行く", "negative")).isEqualTo("行かない");
        assertThat(conjugator.conjugate("見る", "negative")).isEqualTo("見ない");
        assertThat(conjugator.conjugate("食べる", "negative")).isEqualTo("食べない");
        assertThat(conjugator.conjugate("する", "negative")).isEqualTo("しない");
        assertThat(conjugator.conjugate("来る", "negative")).isEqualTo("来ない");
    }

    // ---------- Отрицательная вежливая форма (negative_polite) ----------

    @Test
    void testNegativePoliteForm() {
        assertThat(conjugator.conjugate("話す", "negative_polite")).isEqualTo("話しません");
        assertThat(conjugator.conjugate("待つ", "negative_polite")).isEqualTo("待ちません");
        assertThat(conjugator.conjugate("取る", "negative_polite")).isEqualTo("取りません");
        assertThat(conjugator.conjugate("飲む", "negative_polite")).isEqualTo("飲みません");
        assertThat(conjugator.conjugate("読む", "negative_polite")).isEqualTo("読みません");
        assertThat(conjugator.conjugate("行く", "negative_polite")).isEqualTo("行きません");
        assertThat(conjugator.conjugate("見る", "negative_polite")).isEqualTo("見ません");
        assertThat(conjugator.conjugate("食べる", "negative_polite")).isEqualTo("食べません");
        assertThat(conjugator.conjugate("する", "negative_polite")).isEqualTo("しません");
        assertThat(conjugator.conjugate("来る", "negative_polite")).isEqualTo("来ません");
    }

    // ---------- Прошедшее время (past) ----------

    @Test
    void testPastForm() {
        assertThat(conjugator.conjugate("話す", "past")).isEqualTo("話した");
        assertThat(conjugator.conjugate("待つ", "past")).isEqualTo("待った");
        assertThat(conjugator.conjugate("取る", "past")).isEqualTo("取った");
        assertThat(conjugator.conjugate("飲む", "past")).isEqualTo("飲んだ");
        assertThat(conjugator.conjugate("泳ぐ", "past")).isEqualTo("泳いだ");
        assertThat(conjugator.conjugate("行く", "past")).isEqualTo("行った");
        assertThat(conjugator.conjugate("見る", "past")).isEqualTo("見た");
        assertThat(conjugator.conjugate("食べる", "past")).isEqualTo("食べた");
        assertThat(conjugator.conjugate("する", "past")).isEqualTo("した");
        assertThat(conjugator.conjugate("来る", "past")).isEqualTo("来た");
    }

    // ---------- Прошедшее время вежливое (past_polite) ----------

    @Test
    void testPastPoliteForm() {
        assertThat(conjugator.conjugate("話す", "past_polite")).isEqualTo("話しました");
        assertThat(conjugator.conjugate("待つ", "past_polite")).isEqualTo("待ちました");
        assertThat(conjugator.conjugate("取る", "past_polite")).isEqualTo("取りました");
        assertThat(conjugator.conjugate("飲む", "past_polite")).isEqualTo("飲みました");
        assertThat(conjugator.conjugate("読む", "past_polite")).isEqualTo("読みました");
        assertThat(conjugator.conjugate("行く", "past_polite")).isEqualTo("行きました");
        assertThat(conjugator.conjugate("見る", "past_polite")).isEqualTo("見ました");
        assertThat(conjugator.conjugate("食べる", "past_polite")).isEqualTo("食べました");
        assertThat(conjugator.conjugate("する", "past_polite")).isEqualTo("しました");
        assertThat(conjugator.conjugate("来る", "past_polite")).isEqualTo("来ました");
    }

    // ---------- Прошедшее время отрицательное (past_negative) ----------

    @Test
    void testPastNegativeForm() {
        assertThat(conjugator.conjugate("話す", "past_negative")).isEqualTo("話さなかった");
        assertThat(conjugator.conjugate("待つ", "past_negative")).isEqualTo("待たなかった");
        assertThat(conjugator.conjugate("取る", "past_negative")).isEqualTo("取らなかった");
        assertThat(conjugator.conjugate("飲む", "past_negative")).isEqualTo("飲まなかった");
        assertThat(conjugator.conjugate("読む", "past_negative")).isEqualTo("読まなかった");
        assertThat(conjugator.conjugate("行く", "past_negative")).isEqualTo("行かなかった");
        assertThat(conjugator.conjugate("見る", "past_negative")).isEqualTo("見なかった");
        assertThat(conjugator.conjugate("食べる", "past_negative")).isEqualTo("食べなかった");
        assertThat(conjugator.conjugate("する", "past_negative")).isEqualTo("しなかった");
        assertThat(conjugator.conjugate("来る", "past_negative")).isEqualTo("来なかった");
    }

    // ---------- Прошедшее время отрицательное вежливое (past_negative_polite) ----------

    @Test
    void testPastNegativePoliteForm() {
        assertThat(conjugator.conjugate("話す", "past_negative_polite")).isEqualTo("話しませんでした");
        assertThat(conjugator.conjugate("待つ", "past_negative_polite")).isEqualTo("待ちませんでした");
        assertThat(conjugator.conjugate("取る", "past_negative_polite")).isEqualTo("取りませんでした");
        assertThat(conjugator.conjugate("飲む", "past_negative_polite")).isEqualTo("飲みませんでした");
        assertThat(conjugator.conjugate("読む", "past_negative_polite")).isEqualTo("読みませんでした");
        assertThat(conjugator.conjugate("行く", "past_negative_polite")).isEqualTo("行きませんでした");
        assertThat(conjugator.conjugate("見る", "past_negative_polite")).isEqualTo("見ませんでした");
        assertThat(conjugator.conjugate("食べる", "past_negative_polite")).isEqualTo("食べませんでした");
        assertThat(conjugator.conjugate("する", "past_negative_polite")).isEqualTo("しませんでした");
        assertThat(conjugator.conjugate("来る", "past_negative_polite")).isEqualTo("来ませんでした");
    }
}