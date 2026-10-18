package com.wingman.app;

import android.content.Context;
import android.graphics.Color;

public final class AppTheme {

    private AppTheme() {}

    public static final int NAVY = Color.rgb(16, 24, 39);
    public static final int TEAL = Color.rgb(53, 214, 197);
    public static final int WHITE = Color.rgb(255, 255, 255);
    public static final int MUTED = Color.rgb(170, 182, 198);

    public static final int CARD_BACKGROUND = Color.rgb(27, 38, 55);
    public static final int CARD_BORDER = Color.rgb(49, 66, 83);

    public static final int BUTTON_HEIGHT = 52;
    public static final int BUTTON_RADIUS = 14;
    public static final int BUTTON_TEXT_SIZE = 15;

    public static final int CARD_RADIUS = 20;
    public static final int CARD_PADDING = 18;
    public static final int CARD_BOTTOM_MARGIN = 14;

    public static final int SCREEN_PADDING = 24;
    public static final int SECTION_SPACING = 24;

    public static final int HEADING_SIZE = 30;
    public static final int SECTION_TITLE_SIZE = 12;
    public static final int DESCRIPTION_SIZE = 13;

    public static int dp(Context context, float value) {
        return Math.round(
                value * context.getResources()
                        .getDisplayMetrics().density
        );
    }
}
