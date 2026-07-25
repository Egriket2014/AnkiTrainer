package com.ankitrainer.springconfig;

import com.ankitrainer.language.LanguageAnalyzer;
import com.ankitrainer.language.japanese.JapaneseAnalyzer;
import com.ankitrainer.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);

    @Value("${language.analyzer:" + Constants.DEFAULT_LANGUAGE + "}")
    private String languageCode;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public LanguageAnalyzer languageAnalyzer() {
        log.info("Configuring language analyzer for: {}", languageCode);

        return switch (languageCode.toLowerCase()) {
            case Constants.JAPANESE -> {
                log.debug("Creating JapaneseAnalyzer");
                yield new JapaneseAnalyzer();
            }
            default -> throw new IllegalStateException(
                    "Unsupported language code: '" + languageCode + "'"
            );
        };
    }
}
