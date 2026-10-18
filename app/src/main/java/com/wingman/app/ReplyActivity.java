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
import java.util.List;

public class ReplyActivity extends Activity {

private EditText conversationInput;
private LinearLayout resultContainer;

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
            "Understand the context. Explore possible meanings. " +
            "Choose your next move."
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
            "Your conversation is processed locally using " +
            "Wingman's current rule-based engines."
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

    resultContainer = new LinearLayout(this);
    resultContainer.setOrientation(LinearLayout.VERTICAL);
    resultContainer.setPadding(
            0,
            AppTheme.dp(this, 12),
            0,
            0
    );

    resultCard.addView(resultContainer);

    addResultText(
            "Paste a conversation and tap Analyze Conversation.",
            AppTheme.MUTED,
            14
    );

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

    resultContainer.removeAllViews();

    ContextEngine.Analysis context =
            ContextEngine.analyze(conversation);

    SocialIntelligenceEngine.SocialAnalysis social =
            SocialIntelligenceEngine.analyze(conversation);

    addSection("LATEST MESSAGE", context.latestMessage);
    addSection("CONVERSATION TONE", context.conversationTone);
    addSection("POSSIBLE SIGNAL", context.possibleSignal);

    addSection(
            "SOCIAL COMMUNICATION PATTERN",
            social.communicationStyle
    );

    addSection(
            "POSSIBLE INTERPRETATION",
            social.possibleInterpretation
    );

    addSection(
            "ALTERNATIVE EXPLANATION",
            social.alternativeInterpretation
    );

    addSection("EVIDENCE", social.evidence);

    addSection("SUGGESTED NEXT MOVE", context.nextMove);

    addSection(
            "SOCIAL APPROACH",
            social.recommendedApproach
    );

    addSection("WHY", context.explanation);

    addSection(
            "RULE-BASED SCORES",
            "Context: " + context.confidence + "%\n" +
            "Social pattern: " + social.confidence + "%"
    );
String previousTopics =
        PersistentMemoryEngine.getMemory(this, "last_topics");

ConversationMemoryEngine.MemoryAnalysis memory =
        ConversationMemoryEngine.analyze(conversation);

if (previousTopics != null) {
    addSection("PREVIOUS SAVED TOPICS", previousTopics);
} else {
    addSection(
            "SAVED MEMORY",
            "No previous conversation context has been saved yet."
    );
}

// Save current context for future analysis.
PersistentMemoryEngine.saveMemory(
        this,
        "last_topics",
        memory.recurringTopics
);

PersistentMemoryEngine.saveMemory(
        this,
        "last_pattern",
        memory.conversationPattern
);

PersistentMemoryEngine.saveMemory(
        this,
        "last_context",
        memory.previousContext
);

PersistentMemoryEngine.saveMemory(
        this,
        "last_message",
        memory.keyDetails.isEmpty()
                ? ""
                : memory.keyDetails.get(0)
);

addSection("CONVERSATION MEMORY", memory.recurringTopics);
addSection("CONVERSATION PATTERN", memory.conversationPattern);
addSection("PREVIOUS CONTEXT", memory.previousContext);
addSection("CONTINUITY ADVICE", memory.continuityAdvice);

for (String detail : memory.keyDetails) {
    addSection("KEY DETAIL", detail);
}
    addSection("REPLY SUGGESTIONS", "");

    List<ResponseEngine.ReplySuggestion> suggestions =
            ResponseEngine.generate(conversation);

    for (ResponseEngine.ReplySuggestion suggestion : suggestions) {
        addSection(
                suggestion.style,
                suggestion.reply + "\n\nWhy: " + suggestion.reason
        );
    }

    addResultText(
            "Note: These are rule-based suggestions, not " +
            "generative AI. They cannot determine someone's " +
            "actual feelings or intentions.",
            AppTheme.MUTED,
            12
    );
}

private void addSection(String title, String content) {

    TextView heading = new TextView(this);
    heading.setText(title);
    heading.setTextColor(AppTheme.TEAL);
    heading.setTextSize(12);
    heading.setTypeface(null, Typeface.BOLD);
    heading.setPadding(
            0,
            AppTheme.dp(this, 12),
            0,
            AppTheme.dp(this, 5)
    );

    resultContainer.addView(heading);

    if (content != null && !content.isEmpty()) {
        addResultText(content, AppTheme.WHITE, 14);
    }
}

private void addResultText(
        String text,
        int color,
        int size
) {
    TextView view = new TextView(this);
    view.setText(text);
    view.setTextColor(color);
    view.setTextSize(size);
    view.setLineSpacing(
            AppTheme.dp(this, 4),
            1f
    );

    resultContainer.addView(view);
}

}
