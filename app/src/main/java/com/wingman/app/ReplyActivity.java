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

    TextView brand = new TextView(this);
    brand.setText("W I N G M A N");
    brand.setTextColor(AppTheme.TEAL);
    brand.setTextSize(13);
    brand.setTypeface(null, Typeface.BOLD);

    root.addView(brand);

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

    TextView description = new TextView(this);
    description.setText(
            "Understand the conversation. Discover your next move."
    );
    description.setTextColor(AppTheme.MUTED);
    description.setTextSize(14);
    description.setLineSpacing(
            AppTheme.dp(this, 4),
            1f
    );

    root.addView(description);

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

    conversationInput = AppComponents.createInput(
            this,
            "Paste the full conversation here..."
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

    TextView helper = new TextView(this);
    helper.setText(
            "Analysis uses Wingman's local rule-based engines."
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

    root.addView(
            AppComponents.createButton(
                    this,
                    "Analyze Conversation",
                    view -> analyzeConversation()
            )
    );

    LinearLayout resultCard = AppComponents.createCard(this);

    LinearLayout.LayoutParams resultParams =
            (LinearLayout.LayoutParams)
                    resultCard.getLayoutParams();

    resultParams.topMargin = AppTheme.dp(this, 24);
    resultCard.setLayoutParams(resultParams);

    TextView resultHeading = new TextView(this);
    resultHeading.setText("WINGMAN ANALYSIS");
    resultHeading.setTextColor(AppTheme.TEAL);
    resultHeading.setTextSize(12);
    resultHeading.setTypeface(null, Typeface.BOLD);

    resultCard.addView(resultHeading);

    resultText = new TextView(this);
    resultText.setText(
            "Paste a conversation and tap Analyze Conversation."
    );
    resultText.setTextColor(AppTheme.MUTED);
    resultText.setTextSize(14);
    resultText.setLineSpacing(
            AppTheme.dp(this, 5),
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

    ContextEngine.Analysis context =
            ContextEngine.analyze(conversation);

    SocialIntelligenceEngine.SocialAnalysis social =
            SocialIntelligenceEngine.analyze(conversation);

    String result =
            "1. LATEST MESSAGE\n\n" +
            context.latestMessage +

            "\n\n2. CONVERSATION TONE\n\n" +
            context.conversationTone +

            "\n\n3. POSSIBLE SIGNAL\n\n" +
            context.possibleSignal +

            "\n\n4. SOCIAL COMMUNICATION PATTERN\n\n" +
            social.communicationStyle +

            "\n\n5. POSSIBLE INTERPRETATION\n\n" +
            social.possibleInterpretation +

            "\n\n6. ALTERNATIVE EXPLANATION\n\n" +
            social.alternativeInterpretation +

            "\n\n7. EVIDENCE FOUND\n\n" +
            social.evidence +

            "\n\n8. SUGGESTED NEXT MOVE\n\n" +
            context.nextMove +

            "\n\n9. SOCIAL APPROACH\n\n" +
            social.recommendedApproach +

            "\n\n10. WHY\n\n" +
            context.explanation +

            "\n\nRULE-BASED SCORES\n" +
            "Context: " + context.confidence + "%\n" +
            "Social pattern: " + social.confidence + "%\n\n" +

            "Important: These scores are heuristic estimates, " +
            "not scientifically calibrated probabilities. " +
            "The engines cannot determine someone's actual feelings.";

    resultText.setText(result);
    resultText.setTextColor(AppTheme.WHITE);
}

}
