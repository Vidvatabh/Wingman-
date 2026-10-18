package com.wingman.app;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ConversationHistoryEngine {

    private static final String PREFS_NAME =
            "WingmanConversationHistory";

    private static final String HISTORY_KEY =
            "saved_conversations";

    private static final int MAX_HISTORY = 50;

    private ConversationHistoryEngine() {}

    public static final class ConversationEntry {
        public final String id;
        public final String conversation;
        public final long timestamp;

        public ConversationEntry(
                String id,
                String conversation,
                long timestamp
        ) {
            this.id = id;
            this.conversation = conversation;
            this.timestamp = timestamp;
        }
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getApplicationContext()
                .getSharedPreferences(
                        PREFS_NAME,
                        Context.MODE_PRIVATE
                );
    }

    // Save a conversation locally.
    public static boolean saveConversation(
            Context context,
            String conversation
    ) {
        if (context == null
                || conversation == null
                || conversation.trim().isEmpty()) {
            return false;
        }

        SharedPreferences preferences = getPreferences(context);

        List<ConversationEntry> history =
                getConversationHistory(context);

        String cleaned = conversation.trim();

        // Avoid saving the same conversation repeatedly
        // when it is already the newest entry.
        if (!history.isEmpty()
                && history.get(0).conversation.equals(cleaned)) {
            return true;
        }

        String id = String.valueOf(System.currentTimeMillis());

        ConversationEntry entry = new ConversationEntry(
                id,
                cleaned,
                System.currentTimeMillis()
        );

        history.add(0, entry);

        // Keep only the latest 50 conversations.
        while (history.size() > MAX_HISTORY) {
            history.remove(history.size() - 1);
        }

        JSONArray array = new JSONArray();

        try {
            for (ConversationEntry item : history) {
                JSONObject object = new JSONObject();

                object.put("id", item.id);
                object.put("conversation", item.conversation);
                object.put("timestamp", item.timestamp);

                array.put(object);
            }
        } catch (JSONException e) {
            return false;
        }

        preferences.edit()
                .putString(HISTORY_KEY, array.toString())
                .apply();

        return true;
    }

    // Retrieve saved conversations, newest first.
    public static List<ConversationEntry> getConversationHistory(
            Context context
    ) {
        List<ConversationEntry> history = new ArrayList<>();

        if (context == null) {
            return history;
        }

        String saved = getPreferences(context)
                .getString(HISTORY_KEY, "[]");

        try {
            JSONArray array = new JSONArray(saved);

            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);

                String id = object.optString("id", "");
                String conversation =
                        object.optString("conversation", "");

                long timestamp = object.optLong("timestamp", 0);

                if (!conversation.trim().isEmpty()) {
                    history.add(
                            new ConversationEntry(
                                    id,
                                    conversation,
                                    timestamp
                            )
                    );
                }
            }
        } catch (JSONException e) {
            history.clear();
        }

        Collections.sort(
                history,
                (first, second) ->
                        Long.compare(second.timestamp, first.timestamp)
        );

        return history;
    }

    // Find a saved conversation by ID.
    public static ConversationEntry getConversation(
            Context context,
            String id
    ) {
        if (context == null || id == null) {
            return null;
        }

        for (ConversationEntry entry :
                getConversationHistory(context)) {

            if (entry.id.equals(id)) {
                return entry;
            }
        }

        return null;
    }

    // Delete one saved conversation.
    public static boolean deleteConversation(
            Context context,
            String id
    ) {
        if (context == null || id == null) {
            return false;
        }

        List<ConversationEntry> history =
                getConversationHistory(context);

        boolean removed = history.removeIf(
                entry -> entry.id.equals(id)
        );

        if (removed) {
            saveHistoryList(context, history);
        }

        return removed;
    }

    // Delete all saved conversations.
    public static void clearHistory(Context context) {
        if (context == null) {
            return;
        }

        getPreferences(context)
                .edit()
                .remove(HISTORY_KEY)
                .apply();
    }

    // Return the number of saved conversations.
    public static int getHistoryCount(Context context) {
        return getConversationHistory(context).size();
    }

    private static void saveHistoryList(
            Context context,
            List<ConversationEntry> history
    ) {
        JSONArray array = new JSONArray();

        try {
            for (ConversationEntry item : history) {
                JSONObject object = new JSONObject();

                object.put("id", item.id);
                object.put("conversation", item.conversation);
                object.put("timestamp", item.timestamp);

                array.put(object);
            }
        } catch (JSONException e) {
            return;
        }

        getPreferences(context)
                .edit()
                .putString(HISTORY_KEY, array.toString())
                .apply();
    }
}
