package com.ankitrainer.language.japanese;

import com.ankitrainer.language.PartOfSpeechAnalyzer;
import com.ankitrainer.language.enums.Language;
import com.ankitrainer.language.enums.PartOfSpeech;
import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JapaneseVerbAnalyzer implements PartOfSpeechAnalyzer {

    private static final Logger log = LoggerFactory.getLogger(JapaneseVerbAnalyzer.class);

    private final Tokenizer tokenizer = new Tokenizer.Builder().build();

    private static final String VERB_POS = "動詞"; // Part for verbs in Kuromoji's IPADIC dictionary
    private static final String INDEPENDENT_POS = "自立"; // Speech level 2 values that indicate an independent verb

    @Override
    public boolean matches(String word) {
        try {
            List<Token> tokens = tokenizer.tokenize(word);
            if (tokens.isEmpty()) {
                log.debug("No tokens generated for word: '{}'", word);
                return false;
            }

            Token firstToken = tokens.get(0);
            String pos = firstToken.getPartOfSpeechLevel1();
            String posLevel2 = firstToken.getPartOfSpeechLevel2();
            String surface = firstToken.getSurface();

            boolean isVerb = VERB_POS.equals(pos)
                    && (posLevel2 == null || "*".equals(posLevel2) || INDEPENDENT_POS.equals(posLevel2));

            log.trace("Analysis: '{}' (POS={}/{}) -> isVerb={}", surface, pos, posLevel2, isVerb);

            return isVerb;

        } catch (Exception e) {
            log.warn("Error analyzing word '{}': {}", word, e.getMessage());
            return false;
        }
    }

    @Override
    public PartOfSpeech getPartOfSpeech() {
        return PartOfSpeech.VERB;
    }

    @Override
    public Language getLanguage() {
        return Language.JAPANESE;
    }
}
