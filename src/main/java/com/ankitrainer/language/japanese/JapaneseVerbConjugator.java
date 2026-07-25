package com.ankitrainer.language.japanese;

import com.ankitrainer.language.Conjugator;
import com.ankitrainer.util.Constants;
import com.ankitrainer.util.enums.JapaneseVerbFormLabelEnum;
import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class JapaneseVerbConjugator implements Conjugator {

    private static final Logger log = LoggerFactory.getLogger(JapaneseVerbConjugator.class);

    private final Tokenizer tokenizer = new Tokenizer.Builder().build();

    private static final String LANGUAGE = Constants.JAPANESE;
    private static final String PART_OF_SPEECH = Constants.VERB;
    private static final Set<String> CONJUGATION_TYPES = JapaneseVerbFormLabelEnum.getKeys();

    @Override
    public String conjugate(String verb, String conjugationType) {
        if (verb == null || verb.isEmpty() || conjugationType == null) {
            return null;
        }

        Map<String, String> irregularForms = JapaneseUtills.getIrregularConjugations(verb);
        if (irregularForms != null) {
            String result = irregularForms.get(conjugationType);
            if (result != null) {
                return result;
            }
            log.warn("Unsupported conjugation type '{}' for irregular verb '{}'", conjugationType, verb);
            return null;
        }

        VerbType type = detectVerbType(verb);
        log.debug("Detected verb type: {} for '{}'", type, verb);

        return switch (conjugationType) {
            case "te" -> conjugateToTe(verb, type);
            case "polite" -> conjugateToPolite(verb, type);
            case "negative" -> conjugateToNegative(verb, type);
            case "negative_polite" -> conjugateToNegativePolite(verb, type);
            case "past" -> conjugateToPast(verb, type);
            case "past_polite" -> conjugateToPastPolite(verb, type);
            case "past_negative" -> conjugateToPastNegative(verb, type);
            case "past_negative_polite" -> conjugateToPastNegativePolite(verb, type);
            default -> {
                log.warn("Unsupported conjugation type: {}", conjugationType);
                yield null;
            }
        };
    }

    @Override
    public String getLanguage() {
        return LANGUAGE;
    }

    @Override
    public String getPartOfSpeech() {
        return PART_OF_SPEECH;
    }

    @Override
    public Set<String> getSupportedConjugationTypes() {
        return CONJUGATION_TYPES;
    }

    /*
        て-form
     */
    private String conjugateToTe(String verb, VerbType type) {
        return switch (type) {
            case ICHIDAN -> verb.substring(0, verb.length() - 1) + "て";
            case GODAN -> {
                char lastChar = verb.charAt(verb.length() - 1);
                String stem = verb.substring(0, verb.length() - 1);

                yield switch (lastChar) {
                    case 'う', 'つ', 'る' -> stem + "って";
                    case 'ぶ', 'む', 'ぬ' -> stem + "んで";
                    case 'く'            -> stem + "いて";
                    case 'ぐ'            -> stem + "いで";
                    case 'す'            -> stem + "して";
                    default -> {
                        log.warn("Unknown godan ending: {}", lastChar);
                        yield null;
                    }
                };
            }
        };
    }

    /*
        ます-form
     */
    private String conjugateToPolite(String verb, VerbType type) {
        return switch (type) {
            case ICHIDAN -> verb.substring(0, verb.length() - 1) + "ます";
            case GODAN -> {
                char lastChar = verb.charAt(verb.length() - 1);
                String iRow = JapaneseUtills.getGodanUToI(lastChar);
                if (iRow == null) {
                    log.warn("Unknown godan ending for polite: {}", lastChar);
                    yield null;
                }
                yield verb.substring(0, verb.length() - 1) + iRow + "ます";
            }
        };
    }

    /*
        ない-form
     */
    private String conjugateToNegative(String verb, VerbType type) {
        return switch (type) {
            case ICHIDAN -> verb.substring(0, verb.length() - 1) + "ない";
            case GODAN -> {
                char lastChar = verb.charAt(verb.length() - 1);
                String aRow = JapaneseUtills.getGodanUToA(lastChar);
                if (aRow == null) {
                    log.warn("Unknown godan ending for negative: {}", lastChar);
                    yield null;
                }
                yield verb.substring(0, verb.length() - 1) + aRow + "ない";
            }
        };
    }

    /*
        ません-form
   */
    private String conjugateToNegativePolite(String verb, VerbType type) {
        String polite = conjugateToPolite(verb, type);
        if (polite == null) {
            return null;
        }
        return polite.replace("ます", "ません");
    }

    /*
        た-form
    */
    private String conjugateToPast(String verb, VerbType type) {
        return switch (type) {
            case ICHIDAN -> verb.substring(0, verb.length() - 1) + "た";
            case GODAN -> {
                char lastChar = verb.charAt(verb.length() - 1);
                String stem = verb.substring(0, verb.length() - 1);

                yield switch (lastChar) {
                    case 'う', 'つ', 'る' -> stem + "った";
                    case 'ぶ', 'む', 'ぬ' -> stem + "んだ";
                    case 'く'            -> stem + "いた";
                    case 'ぐ'            -> stem + "いだ";
                    case 'す'            -> stem + "した";
                    default -> {
                        log.warn("Unknown godan ending: {}", lastChar);
                        yield null;
                    }
                };
            }
        };
    }

    /*
        ました-form
    */
    private String conjugateToPastPolite(String verb, VerbType type) {
        String polite = conjugateToPolite(verb, type);
        if (polite == null) {
            return null;
        }
        return polite.replace("ます", "ました");
    }

    /*
        なかった-form
    */
    private String conjugateToPastNegative(String verb, VerbType type) {
        String negative = conjugateToNegative(verb, type);
        if (negative == null) {
            return null;
        }
        return negative.replace("ない", "なかった");
    }

    /*
        ませんでした-form
     */
    private String conjugateToPastNegativePolite(String verb, VerbType type) {
        String negativePolite = conjugateToNegativePolite(verb, type);
        if (negativePolite == null) {
            return null;
        }
        return negativePolite + "でした";
    }

    private VerbType detectVerbType(String verb) {
        if (JapaneseUtills.isGodanException(verb)) {
            return VerbType.GODAN;
        }

        if (isIchidanByReading(verb)) {
            return VerbType.ICHIDAN;
        }

        return VerbType.GODAN;
    }

    private boolean isIchidanByReading(String verb) {
        String reading = getReading(verb);
        if (reading == null || reading.length() < 2) {
            return false;
        }

        if (!reading.endsWith("る")) {
            return false;
        }

        // Проверяем слог перед "る"
        char beforeRu = reading.charAt(reading.length() - 2);
        return JapaneseUtills.isIchidanSyllable(beforeRu);
    }

    private String getReading(String verb) {
        try {
            List<Token> tokens = tokenizer.tokenize(verb);
            if (tokens.isEmpty()) {
                return null;
            }

            Token firstToken = tokens.get(0);
            String reading = firstToken.getReading();
            if (reading == null) {
                return null;
            }

            return katakanaToHiragana(reading);

        } catch (Exception e) {
            log.warn("Error getting reading for '{}': {}", verb, e.getMessage());
            return null;
        }
    }

    private String katakanaToHiragana(String katakana) {
        if (katakana == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (char c : katakana.toCharArray()) {
            if (c >= 'ァ' && c <= 'ン') {
                result.append((char) (c - 0x60));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    private enum VerbType {
        GODAN,
        ICHIDAN
    }
}
