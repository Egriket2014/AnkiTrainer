package com.ankitrainer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtmlUtils {

    private static final Logger log = LoggerFactory.getLogger(HtmlUtils.class);

    /**
     * Clean html tags from string.
     */
    public static String cleanHtml(String html) {
        if (html == null || html.isEmpty()) {
            return "";
        }
        String cleanedHtml = html.replaceAll("<[^>]+>", "").trim();
        log.trace("HTML cleaned: '{}' → '{}'", html, cleanedHtml);
        return cleanedHtml;
    }
}
