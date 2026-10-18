package com.wingman.app;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.LinkedHashMap;

public final class PersistentMemoryEngine {

    private static final String PREFS_NAME =
            "WingmanPersistentMemory";

    private static final String KEY_PREFIX = "memory_";

    private PersistentMemoryEngine() {}

    private static SharedPreferences getPreferences(Context context) {
        return context.getApplicationContext()
                .getSharedPreferences(
                        PREFS_NAME,
                        Context.MODE_PRIVATE
                );
    }

    // Save or update a memory.
    public static boolean saveMemory(
            Context context,
            String key,
            String value
    ) {
        if (context == null
                || key == null
                || key.trim().isEmpty()
                || value == null
                || value.trim().isEmpty()) {
            return false;
        }

        SharedPreferences preferences = getPreferences(context);

        preferences.edit()
                .putString(KEY_PREFIX + key.trim(), value.trim())
                .apply();

        return true;
    }

    // Retrieve a saved memory.
    public static String getMemory(
            Context context,
            String key
    ) {
        if (context == null || key == null) {
            return null;
        }

        return getPreferences(context)
                .getString(KEY_PREFIX + key.trim(), null);
    }

    // Retrieve all saved memories.
    public static Map<String, String> getAllMemories(
            Context context
    ) {
        Map<String, String> memories = new LinkedHashMap<>();

        if (context == null) {
            return memories;
        }

        Map<String, ?> stored =
                getPreferences(context).getAll();

        for (Map.Entry<String, ?> entry : stored.entrySet()) {
            String storedKey = entry.getKey();
            Object storedValue = entry.getValue();

            if (storedKey.startsWith(KEY_PREFIX)
                    && storedValue instanceof String) {

                String key = storedKey.substring(KEY_PREFIX.length());

                memories.put(key, (String) storedValue);
            }
        }

        return memories;
    }

    // Delete one memory.
    public static boolean deleteMemory(
            Context context,
            String key
    ) {
        if (context == null || key == null) {
            return false;
        }

        SharedPreferences preferences = getPreferences(context);
        String storedKey = KEY_PREFIX + key.trim();

        if (!preferences.contains(storedKey)) {
            return false;
        }

        preferences.edit()
                .remove(storedKey)
                .apply();

        return true;
    }

    // Delete all memories saved by this engine.
    public static void clearAllMemories(Context context) {
        if (context == null) {
            return;
        }

        SharedPreferences preferences = getPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        for (String key : preferences.getAll().keySet()) {
            if (key.startsWith(KEY_PREFIX)) {
                editor.remove(key);
            }
        }

        editor.apply();
    }

    // Check whether a memory exists.
    public static boolean hasMemory(
            Context context,
            String key
    ) {
        if (context == null || key == null) {
            return false;
        }

        return getPreferences(context)
                .contains(KEY_PREFIX + key.trim());
    }

    // Count saved memories.
    public static int getMemoryCount(Context context) {
        return getAllMemories(context).size();
    }
}
