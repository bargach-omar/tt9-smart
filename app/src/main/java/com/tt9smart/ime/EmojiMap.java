package com.tt9smart.ime;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.tt9smart.R;

public class EmojiMap {
    private static final Map<String, List<String>> cache = new HashMap<>();
    private static String loadedLangCode = null;
    private static volatile boolean isDownloading = false;

    public static void load(Context context, Locale locale) {
        String langCode = locale.getLanguage();
        if (langCode.equals(loadedLangCode)) return;

        cache.clear();
        loadedLangCode = langCode;

        File cacheFile = getCacheFile(context, langCode);
        if (!cacheFile.exists()) return;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(cacheFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split(",");
                if (parts.length < 2) continue;
                String word = parts[0].trim();
                List<String> emojis = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(parts, 1, parts.length)));
                cache.put(word, emojis);
            }
        } catch (Exception ignored) {}
    }

    public static void downloadIfMissing(Context context, Locale locale) {
        String langCode = locale.getLanguage();
        if (getCacheFile(context, langCode).exists() || isDownloading) return;

        isDownloading = true;
        new Thread(() -> {
            try {
                String url = context.getString(R.string.emoji_map_url, langCode);
                URLConnection connection = new URL(url).openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(10000);

                File tmp = new File(context.getFilesDir(), "emoji_map_" + langCode + ".tmp");
                try (InputStream is = connection.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                     FileWriter writer = new FileWriter(tmp)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        writer.write(line);
                        writer.write('\n');
                    }
                }

                // Atomic rename so a half-written file is never loaded
                tmp.renameTo(getCacheFile(context, langCode));

                // Force reload next time load() is called
                if (langCode.equals(loadedLangCode)) {
                    loadedLangCode = null;
                }
            } catch (Exception ignored) {
                // Network unavailable — will retry next language switch
            } finally {
                isDownloading = false;
            }
        }).start();
    }

    private static File getCacheFile(Context context, String langCode) {
        return new File(context.getFilesDir(), "emoji_map_" + langCode + ".csv");
    }

    public static List<String> getEmojis(String foldedWord) {
        if (foldedWord == null || foldedWord.isEmpty()) return Collections.emptyList();
        List<String> emojis = cache.get(foldedWord);
        return emojis != null ? emojis : Collections.emptyList();
    }
}
