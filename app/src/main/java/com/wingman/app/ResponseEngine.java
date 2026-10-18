package com.wingman.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class ResponseEngine {

private ResponseEngine() {}

public static class ReplySuggestion {
    public final String style;
    public final String reply;
    public final String reason;

    public ReplySuggestion(
            String style,
            String reply,
            String reason
    ) {
        this.style = style;
        this.reply = reply;
        this.reason = reason;
    }
}

public static List<ReplySuggestion> generate(
        String conversation
) {
    List<ReplySuggestion> suggestions =
            new ArrayList<>();

    if (conversation == null ||
            conversation.trim().isEmpty()) {

        suggestions.add(new ReplySuggestion(
                "Natural",
                "Paste a conversation first.",
                "The engine needs context before suggesting a reply."
        ));

        return suggestions;
    }

    String latest = getLatestMessage(conversation);

    String text = latest.toLowerCase(Locale.ROOT);

    if (containsAny(
            text,
            "leave me alone",
            "stop messaging",
            "not interested",
            "don't contact me",
            "do not contact me"
    )) {
        suggestions.add(new ReplySuggestion(
                "Respectful",
                "I understand. I'll respect your space.",
                "A clear boundary should be respected."
        ));

        return suggestions;
    }

    if (containsAny(
            text,
            "how are you",
            "how are u",
            "how's it going"
    )) {
        suggestions.add(new ReplySuggestion(
                "Natural",
                "I'm good, thanks! How about you?",
                "Answers the question and keeps the conversation open."
        ));

        suggestions.add(new ReplySuggestion(
                "Warm",
                "Doing pretty well. How has your day been?",
                "Adds a simple question to encourage conversation."
        ));

        suggestions.add(new ReplySuggestion(
                "Playful",
                "Better now that I've got a message to reply to 😄",
                "Adds light humour; use it if that suits your relationship."
        ));

        return suggestions;
    }

    if (containsAny(
            text,
            "what are you doing",
            "what are u doing",
            "wyd"
    )) {
        suggestions.add(new ReplySuggestion(
                "Natural",
                "Just taking it easy. What about you?",
                "Shares a simple answer and returns the question."
        ));

        suggestions.add(new ReplySuggestion(
                "Playful",
                "Trying to decide what to do next. Any suggestions?",
                "Invites the other person to contribute."
        ));

        suggestions.add(new ReplySuggestion(
                "Confident",
                "Finishing a few things, then taking some time to relax.",
                "Sounds self-assured without trying too hard."
        ));

        return suggestions;
    }

    if (containsAny(
            text,
            "haha",
            "hahaha",
            "lol",
            "😂",
            "lmao"
    )) {
        suggestions.add(new ReplySuggestion(
                "Playful",
                "Okay, at least I know I made you laugh 😂",
                "Keeps a light tone if the joke landed well."
        ));

        suggestions.add(new ReplySuggestion(
                "Natural",
                "😂 Anyway, tell me more.",
                "Keeps the conversation moving without forcing a joke."
        ));

        suggestions.add(new ReplySuggestion(
                "Curious",
                "Wait, what part got you laughing the most? 😂",
                "Can extend the exchange if the context supports it."
        ));

        return suggestions;
    }

    if (containsAny(
            text,
            "thank you",
            "thanks",
            "thank u",
            "thx"
    )) {
        suggestions.add(new ReplySuggestion(
                "Natural",
                "You're welcome!",
                "A simple response fits a straightforward thank-you."
        ));

        suggestions.add(new ReplySuggestion(
                "Warm",
                "Of course. Happy to help.",
                "Sounds friendly and considerate."
        ));

        return suggestions;
    }

    if (text.endsWith("?")) {
        suggestions.add(new ReplySuggestion(
                "Direct",
                "Answer their question honestly and clearly.",
                "The best reply depends on what they asked and your real answer."
        ));

        suggestions.add(new ReplySuggestion(
                "Conversational",
                "Answer first, then ask a relevant follow-up if it feels natural.",
                "Builds on their question without changing the subject abruptly."
        ));

        return suggestions;
    }

    suggestions.add(new ReplySuggestion(
            "Natural",
            "I get what you mean. Tell me more.",
            "Shows interest, but use it only if it genuinely fits."
    ));

    suggestions.add(new ReplySuggestion(
            "Curious",
            "What made you think about that?",
            "Invites more context without assuming their intentions."
    ));

    suggestions.add(new ReplySuggestion(
            "Confident",
            "Fair enough. What's your take on it?",
            "Keeps the exchange balanced and encourages their perspective."
    ));

    return suggestions;
}

private static String getLatestMessage(
        String conversation
) {
    String[] lines = conversation.split("\\r?\\n");

    for (int i = lines.length - 1; i >= 0; i--) {
        String line = lines[i].trim();

        if (!line.isEmpty()) {
            return line;
        }
    }

    return "";
}

private static boolean containsAny(
        String text,
        String... keywords
) {
    for (String keyword : keywords) {
        if (text.contains(keyword)) {
            return true;
        }
    }

    return false;
}

}
