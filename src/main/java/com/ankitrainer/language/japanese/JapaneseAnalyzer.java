package com.ankitrainer.language.japanese;

import com.ankitrainer.language.LanguageAnalyzer;
import com.ankitrainer.util.Constants;
import com.ankitrainer.util.HtmlUtils;
import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JapaneseAnalyzer implements LanguageAnalyzer {

    private static final Logger log = LoggerFactory.getLogger(JapaneseAnalyzer.class);

    private final Tokenizer tokenizer = new Tokenizer.Builder().build();

    private static final String VERB_POS = "動詞"; // Part for verbs in Kuromoji's IPADIC dictionary
    private static final String INDEPENDENT_POS = "自立"; // Speech level 2 values that indicate an independent verb

    @Override
    public boolean isVerb(String word) {
        String cleanedWord = HtmlUtils.cleanHtml(word);
        if (cleanedWord.isEmpty()) {
            log.debug("Word is empty after HTML stripping");
            return false;
        }

        try {
            List<Token> tokens = tokenizer.tokenize(cleanedWord);
            if (tokens.isEmpty()) {
                log.debug("No tokens generated for word: '{}'", cleanedWord);
                return false;
            }

            Token firstToken = tokens.get(0);
            String pos = firstToken.getPartOfSpeechLevel1();
            String posLevel2 = firstToken.getPartOfSpeechLevel2();
            String surface = firstToken.getSurface();

            boolean isVerb = VERB_POS.equals(pos)
                    && (posLevel2 == null || "*".equals(posLevel2) || INDEPENDENT_POS.equals(posLevel2));

            log.debug("Analysis: '{}' (POS={}/{}) -> isVerb={}", surface, pos, posLevel2, isVerb);

            return isVerb;

        } catch (Exception e) {
            log.warn("Error analyzing word '{}': {}", cleanedWord, e.getMessage());
            return false;
        }
    }

    @Override
    public String getLanguage() {
        return Constants.JAPANESE;
    }
}
