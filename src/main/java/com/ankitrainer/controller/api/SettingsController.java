package com.ankitrainer.controller.api;

import com.ankitrainer.settings.AppSettings;
import com.ankitrainer.settings.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/settings")
@RestController
@RequiredArgsConstructor
public class SettingsController {

    private final SettingsService settingsService;

    @GetMapping
    public AppSettings getSettings() {
        return settingsService.getSettings();
    }

    @PutMapping
    public AppSettings updateSettings(@RequestBody AppSettings settings) {
        return settingsService.saveSettings(settings);
    }
}