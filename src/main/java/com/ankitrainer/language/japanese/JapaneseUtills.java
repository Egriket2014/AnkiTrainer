package com.ankitrainer.language.japanese;

import java.util.Map;
import java.util.Set;

public class JapaneseUtills {

    /**
     * Verbs ending in "いる" or "える" that are actually godan verbs.
     */
    private static final Set<String> GODAN_EXCEPTIONS = Set.of(
            "要る", "入る", "走る", "減る", "切る", "知る", "滑る", "散る", "照る", "練る", "罵る", "参る", "混じる", "むせる",
            "帰る", "焦る", "蹴る", "漲る", "這る", "嘲る", "とる", "摂る", "捻る", "湿る", "茂る", "覆る", "蘇る", "煙る",
            "凝る", "図る", "誤る", "限る", "握る", "耽る", "滅入る", "めくる", "狂る", "堪る"
    );

    /**
     * Irregular verbs and their conjugation tables.
     * Each entry contains 7 forms:
     * polite, negative, negative_polite, past, past_polite, past_negative, past_negative_polite
     */
    private static final Map<String, Map<String, String>> IRREGULAR_CONJUGATIONS = Map.of(
            "する", createIrregularMap(
                    "して", "します", "しない", "しません",
                    "した", "しました", "しなかった", "しませんでした"
            ),
            "来る", createIrregularMap(
                    "来て", "来ます", "来ない", "来ません",
                    "来た", "来ました", "来なかった", "来ませんでした"
            ),
            "行く", createIrregularMap(
                    "行って", "行きます", "行かない", "行きません",
                    "行った", "行きました", "行かなかった", "行きませんでした"
            )
    );

    private static final Set<Character> ICHIDAN_BEFORE_RU = Set.of(
            'み', 'き', 'に', 'ひ', 'ぎ', 'じ', 'び', 'ぴ', 'い',
            'へ', 'べ', 'け', 'げ', 'せ', 'ぜ', 'て', 'で', 'ね', 'め', 'れ', 'え'
    );

    private static final Map<Character, String> GODAN_U_TO_I_ROW = Map.of(
            'う', "い",
            'く', "き",
            'ぐ', "ぎ",
            'す', "し",
            'つ', "ち",
            'ぬ', "に",
            'ぶ', "び",
            'む', "み",
            'る', "り"
    );

    private static final Map<Character, String> GODAN_U_TO_A_ROW = Map.of(
            'う', "わ",
            'く', "か",
            'ぐ', "が",
            'す', "さ",
            'つ', "た",
            'ぬ', "な",
            'ぶ', "ば",
            'む', "ま",
            'る', "ら"
    );

    private static Map<String, String> createIrregularMap(
            String te,
            String polite,
            String negative,
            String negativePolite,
            String past,
            String pastPolite,
            String pastNegative,
            String pastNegativePolite
    ) {
        return Map.of(
                "te", te,
                "polite", polite,
                "negative", negative,
                "negative_polite", negativePolite,
                "past", past,
                "past_polite", pastPolite,
                "past_negative", pastNegative,
                "past_negative_polite", pastNegativePolite
        );
    }

    public static Map<String, String> getIrregularConjugations(String verb) {
        return IRREGULAR_CONJUGATIONS.get(verb);
    }

    public static boolean isGodanException(String verb) {
        return GODAN_EXCEPTIONS.contains(verb);
    }

    public static boolean isIchidanSyllable(char c) {
        return ICHIDAN_BEFORE_RU.contains(c);
    }

    public static String getGodanUToI(char c) {
        return GODAN_U_TO_I_ROW.get(c);
    }

    public static String getGodanUToA(char c) {
        return GODAN_U_TO_A_ROW.get(c);
    }
}
