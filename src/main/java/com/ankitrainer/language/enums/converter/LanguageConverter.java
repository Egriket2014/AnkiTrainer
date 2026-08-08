package com.ankitrainer.language.enums.converter;

import com.ankitrainer.language.enums.Language;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

@Component
@Converter(autoApply = true)
public class LanguageConverter implements
        org.springframework.core.convert.converter.Converter<String, Language>,
        AttributeConverter<Language, String>
{

    @Override
    public Language convert(String key) {
        if (key == null || key.isBlank()) {
            return null;
        }
        return Language.get(key);
    }

    @Override
    public String convertToDatabaseColumn(Language language) {
        if (language == null) {
            return null;
        }
        return language.getKey();
    }

    @Override
    public Language convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }
        return Language.get(dbData);
    }
}
