package com.ankitrainer.settings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppSettings {

    @Builder.Default
    private boolean stemAutoCompleteOnTab = true;

    @Builder.Default
    private boolean showQueueButton = false;

    public static AppSettings defaults() {
        return AppSettings.builder().build();
    }
}