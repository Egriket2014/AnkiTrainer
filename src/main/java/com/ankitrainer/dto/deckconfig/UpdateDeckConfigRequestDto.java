package com.ankitrainer.dto.deckconfig;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDeckConfigRequestDto {

    @NotNull(message = "ID is required")
    private Long id;

    @NotNull(message = "Review limit is required")
    @Min(value = 1, message = "Review cards limit must be at least 1")
    private Integer reviewLimit;

    @NotNull(message = "New limit is required")
    @Min(value = 0, message = "New cards limit must be at least 0")
    private Integer newLimit;
}
