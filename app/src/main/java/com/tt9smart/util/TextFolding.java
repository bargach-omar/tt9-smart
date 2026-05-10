package com.tt9smart.util;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class TextFolding {
    private static final Pattern DIACRITICS = Pattern.compile("\\p{M}");

    /**
     * Strips diacritical marks from text so that "café" → "cafe", "coûter" → "couter".
     * Used to build accent-insensitive prefix indexes for word prediction.
     */
    public static String fold(String text) {
        if (text == null || text.isEmpty()) return "";
        return DIACRITICS.matcher(Normalizer.normalize(text, Normalizer.Form.NFD)).replaceAll("");
    }
}
