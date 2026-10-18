package com.wingman.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

public class ReplyActivity extends Activity {

    private EditText conversationInput;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(AppTheme.NAVY);
        getWindow().setNavigationBarColor(AppTheme.NAVY);

        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(AppTheme.NAVY);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        int padding = AppTheme.dp(
                this,
                AppTheme.SCREEN_PADDING
        );

        root.setPadding(padding, padding, padding, padding);

        scroll.addView(root);
        setContentView(scroll);

        // BRAND
        TextView brand = new TextView(this);
        brand.setText("W I N G M A N");
        brand.setTextColor(AppTheme.TEAL);
        brand.setTextSize(13);
        brand.setTypeface(null, Typeface.BOLD);

        root.addView(brand);

        // HEADING
        TextView heading = AppComponents.createHeading(
                this,
                "Reply to a message"
        );

        heading.setPadding(
                0,
                AppTheme.dp(this, 20),
                0,
                AppTheme.dp(this, 8)
        );

        root.addView(heading);

        // DESCRIPTION
        TextView description = new TextView(this);
        description.setText(
                "Share the conversation. Understand the context " +
                "and discover your next move."
        );
        description.setTextColor(AppTheme.MUTED);
        description.setTextSize(14);
        description.setLineSpacing(
                AppTheme.dp(this, 4),
                1f
        );

        root.addView(description);

        // INPUT LABEL
        TextView label = new TextView(this);
        label.setText("YOUR CONVERSATION");
        label.setTextColor(AppTheme.TEAL);
        label.setTextSize(12);
        label.setTypeface(null, Typeface.BOLD);

        label.setPadding(
                0,
                AppTheme.dp(this, 28),
                0,
                AppTheme.dp(this, 10)
        );

        root.addView(label);

        // CONVERSATION INPUT
        conversationInput = AppComponents.createInput(
                this,
                "Paste the conversation here..."
        );

        conversationInput.setGravity(
                Gravity.TOP | Gravity.START
        );

        conversationInput.setMinLines(7);
        conversationInput.setSingleLine(false);

        conversationInput.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        AppTheme.dp(this, 190)
                )
        );

        root.addView(conversationInput);

        // HELPER TEXT
        TextView helper = new TextView(this);
        helper.setText(
                "Wingman will consider the full conversation."
        );
        helper.setTextColor(AppTheme.MUTED);
        helper.setTextSize(12);

        helper.setPadding(
                0,
                AppTheme.dp(this, 12),
                0,
                AppTheme.dp(this, 18)
        );

        root.addView(helper);

        // ANALYZE BUTTON
        root.addView(
                AppComponents.createButton(
                        this,
                        "Analyze Conversation",
                        view -> analyzeConversation()
                )
        );

        // RESULT CARD
        LinearLayout resultCard =
                AppComponents.createCard(this);

        LinearLayout.LayoutParams resultParams =
                (LinearLayout.LayoutParams)
                        resultCard.getLayoutParams();

        resultParams.topMargin = AppTheme.dp(this, 24);
        resultCard.setLayoutParams(resultParams);

        TextView resultHeading = new TextView(this);
        resultHeading.setText("YOUR NEXT MOVE");
        resultHeading.setTextColor(AppTheme.TEAL);
        resultHeading.setTextSize(12);
        resultHeading.setTypeface(null, Typeface.BOLD);

        resultCard.addView(resultHeading);

        resultText = new TextView(this);
        resultText.setText(
                "Your conversation analysis will appear here."
        );
        resultText.setTextColor(AppTheme.MUTED);
        resultText.setTextSize(14);
        resultText.setLineSpacing(
                AppTheme.dp(this, 4),
                1f
        );

        resultText.setPadding(
                0,
                AppTheme.dp(this, 12),
                0,
                0
        );

        resultCard.addView(resultText);
        root.addView(resultCard);
    }

    private void analyzeConversation() {

        String conversation =
                conversationInput.getText().toString().trim();

        if (conversation.isEmpty()) {

            Toast.makeText(
                    this,
                    "Paste your conversation first.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        resultText.setText(
                "Conversation received successfully.\n\n" +
                "Length: " + conversation.length() +
                " characters.\n\n" +
                "AI analysis is not connected yet. " +
                "This screen currently checks the input only."
        );

        resultText.setTextColor(AppTheme.WHITE);
    }
}
