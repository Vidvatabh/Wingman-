package com.wingman.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(AppTheme.NAVY);
        getWindow().setNavigationBarColor(AppTheme.NAVY);

        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(AppTheme.NAVY);

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        int padding = AppTheme.dp(this, AppTheme.SCREEN_PADDING);

        root.setPadding(
                padding,
                AppTheme.dp(this, 28),
                padding,
                padding
        );

        root.setBackgroundColor(AppTheme.NAVY);

        scroll.addView(root);
        setContentView(scroll);

        TextView brand = new TextView(this);
        brand.setText("W I N G M A N");
        brand.setTextColor(AppTheme.TEAL);
        brand.setTextSize(14);
        brand.setTypeface(null, Typeface.BOLD);
        brand.setLetterSpacing(0.08f);
        root.addView(brand);

        TextView heading = new TextView(this);
        heading.setText("Your next move.");
        heading.setTextColor(AppTheme.WHITE);
        heading.setTextSize(AppTheme.HEADING_SIZE);
        heading.setTypeface(null, Typeface.BOLD);

        heading.setPadding(
                0,
                AppTheme.dp(this, AppTheme.SECTION_SPACING),
                0,
                AppTheme.dp(this, 8)
        );

        root.addView(heading);

        TextView subtitle = new TextView(this);
        subtitle.setText(
                "Real conversations. More confidence. Better connections."
        );
        subtitle.setTextColor(AppTheme.MUTED);
        subtitle.setTextSize(15);
        subtitle.setLineSpacing(
                AppTheme.dp(this, 5),
                1f
        );
        subtitle.setPadding(
                0, 0, 0,
                AppTheme.dp(this, 26)
        );

        root.addView(subtitle);

        addSectionTitle("YOUR WINGMAN");

        addCard(
                "Reply to a message",
                "Understand the conversation and find your next reply."
        );

        addCard(
                "Start a conversation",
                "Break the ice without sounding forced."
        );

        addCard(
                "Understand the situation",
                "Explore possible meanings, signals and next moves."
        );

        addCard(
                "Practice",
                "Build confidence through realistic social scenarios."
        );

        addCard(
                "My Growth",
                "Track your practice and communication progress."
        );

        TextView footer = new TextView(this);
        footer.setText("YOUR GOAL. YOUR PACE. YOUR CONNECTION.");
        footer.setTextColor(AppTheme.MUTED);
        footer.setTextSize(10);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(
                0,
                AppTheme.dp(this, 30),
                0,
                AppTheme.dp(this, 10)
        );

        root.addView(footer);
    }

    private void addSectionTitle(String text) {

        TextView title = new TextView(this);
        title.setText(text);
        title.setTextColor(AppTheme.TEAL);
        title.setTextSize(AppTheme.SECTION_TITLE_SIZE);
        title.setTypeface(null, Typeface.BOLD);

        title.setPadding(
                0, 0, 0,
                AppTheme.dp(this, 14)
        );

        root.addView(title);
    }

    private void addCard(String title, String description) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);

        int cardPadding = AppTheme.dp(this, AppTheme.CARD_PADDING);

        card.setPadding(
                cardPadding,
                cardPadding,
                cardPadding,
                cardPadding
        );

        GradientDrawable background = new GradientDrawable();
        background.setColor(AppTheme.CARD_BACKGROUND);

        background.setCornerRadius(
                AppTheme.dp(this, AppTheme.CARD_RADIUS)
        );

        background.setStroke(
                AppTheme.dp(this, 1),
                AppTheme.CARD_BORDER
        );

        card.setBackground(background);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                0,
                0,
                0,
                AppTheme.dp(this, AppTheme.CARD_BOTTOM_MARGIN)
        );

        card.setLayoutParams(params);

        TextView name = new TextView(this);
        name.setText(title);
        name.setTextColor(AppTheme.WHITE);
        name.setTextSize(17);
        name.setTypeface(null, Typeface.BOLD);

        card.addView(name);

        TextView detail = new TextView(this);
        detail.setText(description);
        detail.setTextColor(AppTheme.MUTED);
        detail.setTextSize(AppTheme.DESCRIPTION_SIZE);

        detail.setLineSpacing(
                AppTheme.dp(this, 4),
                1f
        );

        detail.setPadding(
                0,
                AppTheme.dp(this, 7),
                0,
                0
        );

        card.addView(detail);

        card.setClickable(true);
        card.setFocusable(true);

        root.addView(card);
    }
}
