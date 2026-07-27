package com.ankitrainer.springconfig;

import com.ankitrainer.language.LanguageAnalyzer;
import com.ankitrainer.language.japanese.JapaneseAnalyzer;
import com.ankitrainer.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.openspacedrepetition.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class AppConfig {

    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);

    @Value("${language.analyzer:" + Constants.DEFAULT_LANGUAGE + "}")
    private String languageCode;

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
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

    @Bean
    public Scheduler scheduler() {
        return Scheduler.builder().build();
    }
}
