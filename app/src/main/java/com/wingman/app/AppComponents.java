package com.wingman.app;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.TextView;

public final class AppComponents {

    private AppComponents() {}

    // CENTRAL BUTTON SYSTEM
    public static Button createButton(
            Context context,
            String text,
            View.OnClickListener listener) {

        Button button = new Button(context);

        button.setText(text);
        button.setTextSize(AppTheme.BUTTON_TEXT_SIZE);
        button.setTextColor(AppTheme.NAVY);
        button.setTypeface(null, Typeface.BOLD);
        button.setAllCaps(false);
        button.setGravity(Gravity.CENTER);

        GradientDrawable background = new GradientDrawable();
        background.setColor(AppTheme.TEAL);
        background.setCornerRadius(
                AppTheme.dp(context, AppTheme.BUTTON_RADIUS)
        );

        button.setBackground(background);
        button.setMinHeight(0);
        button.setMinimumHeight(0);
        button.setPadding(
                AppTheme.dp(context, 16),
                0,
                AppTheme.dp(context, 16),
                0
        );

        button.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        AppTheme.dp(context, AppTheme.BUTTON_HEIGHT)
                )
        );

        if (listener != null) {
            button.setOnClickListener(listener);
        }

        return button;
    }

    // CENTRAL CARD SYSTEM
    public static LinearLayout createCard(Context context) {

        LinearLayout card = new LinearLayout(context);
        card.setOrientation(LinearLayout.VERTICAL);

        int padding = AppTheme.dp(
                context,
                AppTheme.CARD_PADDING
        );

        card.setPadding(padding, padding, padding, padding);

        GradientDrawable background = new GradientDrawable();
        background.setColor(AppTheme.CARD_BACKGROUND);
        background.setCornerRadius(
                AppTheme.dp(context, AppTheme.CARD_RADIUS)
        );
        background.setStroke(
                AppTheme.dp(context, 1),
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
                AppTheme.dp(context, AppTheme.CARD_BOTTOM_MARGIN)
        );

        card.setLayoutParams(params);

        return card;
    }

    // CENTRAL HEADING SYSTEM
    public static TextView createHeading(
            Context context,
            String text) {

        TextView heading = new TextView(context);

        heading.setText(text);
        heading.setTextColor(AppTheme.WHITE);
        heading.setTextSize(AppTheme.HEADING_SIZE);
        heading.setTypeface(null, Typeface.BOLD);

        return heading;
    }

    // CENTRAL INPUT FIELD SYSTEM
    public static EditText createInput(
            Context context,
            String hint) {

        EditText input = new EditText(context);

        input.setHint(hint);
        input.setTextColor(AppTheme.WHITE);
        input.setHintTextColor(AppTheme.MUTED);
        input.setTextSize(15);
        input.setSingleLine(false);
        input.setGravity(Gravity.TOP | Gravity.START);
        input.setPadding(
                AppTheme.dp(context, 14),
                AppTheme.dp(context, 12),
                AppTheme.dp(context, 14),
                AppTheme.dp(context, 12)
        );

        GradientDrawable background = new GradientDrawable();
        background.setColor(AppTheme.CARD_BACKGROUND);
        background.setCornerRadius(
                AppTheme.dp(context, AppTheme.BUTTON_RADIUS)
        );
        background.setStroke(
                AppTheme.dp(context, 1),
                AppTheme.CARD_BORDER
        );

        input.setBackground(background);

        return input;
    }
}
