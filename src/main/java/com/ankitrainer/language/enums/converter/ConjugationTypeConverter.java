package com.ankitrainer.language.enums.converter;

import com.ankitrainer.language.enums.ConjugationType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

@Component
@Converter(autoApply = true)
public class ConjugationTypeConverter implements
        org.springframework.core.convert.converter.Converter<String, ConjugationType>,
        AttributeConverter<ConjugationType, String>
{

    @Override
    public ConjugationType convert(String key) {
        if (key == null || key.isBlank()) {
            return null;
        }
        return ConjugationType.get(key);
    }

    @Override
    public String convertToDatabaseColumn(ConjugationType conjugationType) {
        if (conjugationType == null) {
            return null;
        }
        return conjugationType.getKey();
    }

    @Override
    public ConjugationType convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }
        return ConjugationType.get(dbData);
    }
}
