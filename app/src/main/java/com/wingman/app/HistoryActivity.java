package com.wingman.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends Activity {

    private LinearLayout historyContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(AppTheme.NAVY);
        getWindow().setNavigationBarColor(AppTheme.NAVY);

        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(AppTheme.NAVY);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        int padding = AppTheme.dp(this, AppTheme.SCREEN_PADDING);
        root.setPadding(padding, padding, padding, padding);

        scroll.addView(root);
        setContentView(scroll);

        TextView brand = new TextView(this);
        brand.setText("W I N G M A N");
        brand.setTextColor(AppTheme.TEAL);
        brand.setTextSize(13);
        brand.setTypeface(null, Typeface.BOLD);
        root.addView(brand);

        TextView heading = AppComponents.createHeading(
                this,
                "Conversation History"
        );

        heading.setPadding(
                0,
                AppTheme.dp(this, 20),
                0,
                AppTheme.dp(this, 8)
        );

        root.addView(heading);

        TextView description = new TextView(this);
        description.setText(
                "Your saved conversations stay on this device."
        );
        description.setTextColor(AppTheme.MUTED);
        description.setTextSize(14);
        root.addView(description);

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        buttonParams.topMargin = AppTheme.dp(this, 20);

        android.widget.Button clearButton =
                AppComponents.createButton(
                        this,
                        "Clear All History",
                        view -> confirmClearAll()
                );

        clearButton.setLayoutParams(buttonParams);
        root.addView(clearButton);

        historyContainer = new LinearLayout(this);
        historyContainer.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams listParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        listParams.topMargin = AppTheme.dp(this, 24);
        historyContainer.setLayoutParams(listParams);

        root.addView(historyContainer);

        refreshHistory();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (historyContainer != null) {
            refreshHistory();
        }
    }

    private void refreshHistory() {
        historyContainer.removeAllViews();

        List<ConversationHistoryEngine.ConversationEntry> history =
                ConversationHistoryEngine.getConversationHistory(this);

        if (history.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText(
                    "No saved conversations yet.\n\n" +
                    "Analyze a conversation in Wingman to save it here."
            );
            empty.setTextColor(AppTheme.MUTED);
            empty.setTextSize(14);
            empty.setPadding(
                    0,
                    AppTheme.dp(this, 16),
                    0,
                    AppTheme.dp(this, 16)
            );

            historyContainer.addView(empty);
            return;
        }

        for (ConversationHistoryEngine.ConversationEntry entry : history) {
            addHistoryCard(entry);
        }
    }

    private void addHistoryCard(
            ConversationHistoryEngine.ConversationEntry entry
    ) {
        LinearLayout card = AppComponents.createCard(this);

        TextView date = new TextView(this);
        date.setText(formatDate(entry.timestamp));
        date.setTextColor(AppTheme.TEAL);
        date.setTextSize(12);
        date.setTypeface(null, Typeface.BOLD);
        card.addView(date);

        TextView preview = new TextView(this);
        preview.setText(createPreview(entry.conversation));
        preview.setTextColor(AppTheme.WHITE);
        preview.setTextSize(14);
        preview.setPadding(
                0,
                AppTheme.dp(this, 10),
                0,
                AppTheme.dp(this, 14)
        );
        card.addView(preview);

        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        actions.setGravity(Gravity.CENTER_VERTICAL);

        android.widget.Button openButton =
                new android.widget.Button(this);
        openButton.setText("Open");
        openButton.setAllCaps(false);
        openButton.setTextColor(AppTheme.TEAL);
        openButton.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        openButton.setOnClickListener(
                view -> showConversation(entry)
        );

        android.widget.Button deleteButton =
                new android.widget.Button(this);
        deleteButton.setText("Delete");
        deleteButton.setAllCaps(false);
        deleteButton.setTextColor(
                android.graphics.Color.rgb(255, 130, 130)
        );
        deleteButton.setBackgroundColor(
                android.graphics.Color.TRANSPARENT
        );
        deleteButton.setOnClickListener(
                view -> confirmDelete(entry)
        );

        actions.addView(openButton);
        actions.addView(deleteButton);

        card.addView(actions);
        historyContainer.addView(card);
    }

    private void showConversation(
            ConversationHistoryEngine.ConversationEntry entry
    ) {
        TextView content = new TextView(this);
        content.setText(entry.conversation);
        content.setTextColor(AppTheme.WHITE);
        content.setTextSize(15);
        content.setPadding(
                AppTheme.dp(this, 8),
                AppTheme.dp(this, 8),
                AppTheme.dp(this, 8),
                AppTheme.dp(this, 8)
        );

        ScrollView contentScroll = new ScrollView(this);
        contentScroll.addView(content);

        new AlertDialog.Builder(this)
                .setTitle("Saved Conversation")
                .setView(contentScroll)
                .setPositiveButton("Close", null)
                .show();
    }

    private void confirmDelete(
            ConversationHistoryEngine.ConversationEntry entry
    ) {
        new AlertDialog.Builder(this)
                .setTitle("Delete conversation?")
                .setMessage(
                        "This saved conversation will be permanently deleted."
                )
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialog, which) -> {
                    boolean deleted =
                            ConversationHistoryEngine.deleteConversation(
                                    this,
                                    entry.id
                            );

                    if (deleted) {
                        Toast.makeText(
                                this,
                                "Conversation deleted.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    refreshHistory();
                })
                .show();
    }

    private void confirmClearAll() {
        List<ConversationHistoryEngine.ConversationEntry> history =
                ConversationHistoryEngine.getConversationHistory(this);

        if (history.isEmpty()) {
            Toast.makeText(
                    this,
                    "History is already empty.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Clear all history?")
                .setMessage(
                        "All saved conversations will be permanently deleted."
                )
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Clear All", (dialog, which) -> {
                    ConversationHistoryEngine.clearHistory(this);
                    refreshHistory();

                    Toast.makeText(
                            this,
                            "History cleared.",
                            Toast.LENGTH_SHORT
                    ).show();
                })
                .show();
    }

    private String formatDate(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat(
                "dd MMM yyyy, hh:mm a",
                Locale.getDefault()
        );

        return format.format(new Date(timestamp));
    }

    private String createPreview(String conversation) {
        String cleaned = conversation.trim()
                .replace('\n', ' ')
                .replace('\r', ' ');

        if (cleaned.length() > 150) {
            return cleaned.substring(0, 150) + "...";
        }

        return cleaned;
    }
}
