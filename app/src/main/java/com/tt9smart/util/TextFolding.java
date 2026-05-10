package com.tt9smart.util;

import java.text.Normalizer;

public class TextFolding {
    /**
     * Strips diacritical marks from text so that "café" → "cafe", "coûter" → "couter".
     * Used to build accent-insensitive prefix indexes for word prediction.
     */
    public static String fold(String text) {
        if (text == null || text.isEmpty()) return "";
        String nfd = Normalizer.normalize(text, Normalizer.Form.NFD);
        return nfd.replaceAll("\\p{M}", "");
    }
}
