package com.wingman.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {

private final int NAVY = Color.rgb(16, 24, 39);
private final int TEAL = Color.rgb(53, 214, 197);
private final int WHITE = Color.rgb(255, 255, 255);
private final int MUTED = Color.rgb(170, 182, 198);

private LinearLayout root;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getWindow().setStatusBarColor(NAVY);
    getWindow().setNavigationBarColor(NAVY);

    ScrollView scroll = new ScrollView(this);
    scroll.setBackgroundColor(NAVY);

    root = new LinearLayout(this);
    root.setOrientation(LinearLayout.VERTICAL);
    root.setPadding(24, 28, 24, 24);
    root.setBackgroundColor(NAVY);

    scroll.addView(root);
    setContentView(scroll);

    TextView brand = new TextView(this);
    brand.setText("W I N G M A N");
    brand.setTextColor(TEAL);
    brand.setTextSize(14);
    brand.setTypeface(null, Typeface.BOLD);
    brand.setLetterSpacing(0.08f);
    root.addView(brand);

    TextView heading = new TextView(this);
    heading.setText("Your next move.");
    heading.setTextColor(WHITE);
    heading.setTextSize(30);
    heading.setTypeface(null, Typeface.BOLD);
    heading.setPadding(0, 24, 0, 8);
    root.addView(heading);

    TextView subtitle = new TextView(this);
    subtitle.setText("Real conversations. More confidence. Better connections.");
    subtitle.setTextColor(MUTED);
    subtitle.setTextSize(15);
    subtitle.setLineSpacing(5, 1f);
    subtitle.setPadding(0, 0, 0, 26);
    root.addView(subtitle);

    addSectionTitle("YOUR WINGMAN");

    addCard("Reply to a message",
            "Understand the conversation and find your next reply.");

    addCard("Start a conversation",
            "Break the ice without sounding forced.");

    addCard("Understand the situation",
            "Explore possible meanings, signals and next moves.");

    addCard("Practice",
            "Build confidence through realistic social scenarios.");

    addCard("My Growth",
            "Track your practice and communication progress.");

    TextView footer = new TextView(this);
    footer.setText("YOUR GOAL. YOUR PACE. YOUR CONNECTION.");
    footer.setTextColor(MUTED);
    footer.setTextSize(10);
    footer.setGravity(Gravity.CENTER);
    footer.setPadding(0, 30, 0, 10);
    root.addView(footer);
}

private void addSectionTitle(String text) {
    TextView title = new TextView(this);
    title.setText(text);
    title.setTextColor(TEAL);
    title.setTextSize(12);
    title.setTypeface(null, Typeface.BOLD);
    title.setPadding(0, 0, 0, 14);
    root.addView(title);
}

private void addCard(String title, String description) {
    LinearLayout card = new LinearLayout(this);
    card.setOrientation(LinearLayout.VERTICAL);
    card.setPadding(18, 18, 18, 18);

    GradientDrawable background = new GradientDrawable();
    background.setColor(Color.rgb(27, 38, 55));
    background.setCornerRadius(20);
    background.setStroke(1, Color.rgb(49, 66, 83));
    card.setBackground(background);

    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
    params.setMargins(0, 0, 0, 14);
    card.setLayoutParams(params);

    TextView name = new TextView(this);
    name.setText(title);
    name.setTextColor(WHITE);
    name.setTextSize(17);
    name.setTypeface(null, Typeface.BOLD);
    card.addView(name);

    TextView detail = new TextView(this);
    detail.setText(description);
    detail.setTextColor(MUTED);
    detail.setTextSize(13);
    detail.setLineSpacing(4, 1f);
    detail.setPadding(0, 7, 0, 0);
    card.addView(detail);

    card.setClickable(true);
    card.setFocusable(true);

    root.addView(card);
}

}
