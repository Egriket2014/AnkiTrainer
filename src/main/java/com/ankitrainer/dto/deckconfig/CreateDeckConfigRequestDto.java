package com.ankitrainer.dto.deckconfig;

import com.ankitrainer.language.enums.Language;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeckConfigRequestDto {

    @NotBlank(message = "Deck name is required")
    private String deckName;

    @NotBlank(message = "Model name is required")
    private String modelName;

    @NotNull(message = "Language is required")
    private Language language;

    @NotBlank(message = "Word field is required")
    private String wordField;

    @NotBlank(message = "Translation field is required")
    private String translationField;

    private String extraField;

    @NotNull(message = "Review limit is required")
    @Min(value = 1, message = "Review cards limit must be at least 1")
    private Integer reviewLimit;

    @NotNull(message = "New limit is required")
    @Min(value = 0, message = "New cards limit must be at least 0")
    private Integer newLimit;
}