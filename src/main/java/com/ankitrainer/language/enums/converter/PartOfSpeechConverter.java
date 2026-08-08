package com.ankitrainer.language.enums.converter;

import com.ankitrainer.language.enums.PartOfSpeech;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

@Component
@Converter(autoApply = true)
public class PartOfSpeechConverter implements
        org.springframework.core.convert.converter.Converter<String, PartOfSpeech>,
        AttributeConverter<PartOfSpeech, String>
{

    @Override
    public PartOfSpeech convert(String key) {
        if (key == null || key.isBlank()) {
            return null;
        }
        return PartOfSpeech.get(key);
    }

    @Override
    public String convertToDatabaseColumn(PartOfSpeech partOfSpeech) {
        if (partOfSpeech == null) {
            return null;
        }
        return partOfSpeech.getKey();
    }

    @Override
    public PartOfSpeech convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }
        return PartOfSpeech.get(dbData);
    }
}
