package com.wingman.app;

import java.util.Locale;
import java.util.regex.Pattern;

public final class ContextEngine {

    private ContextEngine() {}

    public static class Analysis {

        public String latestMessage;
        public String conversationTone;
        public String possibleSignal;
        public String nextMove;
        public String explanation;
        public int confidence;

        public Analysis(
                String latestMessage,
                String conversationTone,
                String possibleSignal,
                String nextMove,
                String explanation,
                int confidence
        ) {
            this.latestMessage = latestMessage;
            this.conversationTone = conversationTone;
            this.possibleSignal = possibleSignal;
            this.nextMove = nextMove;
            this.explanation = explanation;
            this.confidence = confidence;
        }
    }

    public static Analysis analyze(String conversation) {

        if (conversation == null ||
                conversation.trim().isEmpty()) {

            return new Analysis(
                    "",
                    "Unknown",
                    "No conversation provided",
                    "Paste a conversation first.",
                    "The engine needs conversation text.",
                    0
            );
        }

        String text = conversation.trim();

        String[] lines = text.split("\\r?\\n");

        String latestMessage = "";

        for (int i = lines.length - 1; i >= 0; i--) {

            String line = lines[i].trim();

            if (!line.isEmpty()) {
                latestMessage = line;
                break;
            }
        }

        String lower = latestMessage.toLowerCase(
                Locale.ROOT
        );

        String tone = "Unclear";
        String signal = "Insufficient evidence";
        String nextMove =
                "Reply naturally and avoid assumptions.";
        String explanation =
                "One message is not enough to establish intent.";

        int confidence = 20;

        boolean hasQuestion =
                latestMessage.contains("?");

        boolean positive = containsAny(
                lower,
                "haha", "hahaha", "lol", "😂",
                "😊", "❤️", "nice", "great",
                "sounds good", "thank you", "thanks"
        );

        boolean negative = containsAny(
                lower,
                "leave me alone", "stop messaging",
                "not interested", "don't contact",
                "do not contact", "please stop"
        );

        boolean greeting = containsAny(
                lower,
                "hi", "hello", "hey", "hii", "heyy"
        );

        boolean questionWords = containsAny(
                lower,
                "how are you", "what are you doing",
                "how was your day", "where are you",
                "what happened", "tell me more"
        );

        if (negative) {

            tone = "Boundary or discomfort";
            signal = "The person may want space";
            nextMove =
                    "Respect the boundary. Stop pushing the conversation.";
            explanation =
                    "The message contains language asking for distance.";
            confidence = 85;

        } else if (questionWords || hasQuestion) {

            tone = "Question or curiosity";
            signal =
                    "The person is asking something; the reason is uncertain.";
            nextMove =
                    "Answer their question directly, then continue naturally.";
            explanation =
                    "A question creates an opportunity to respond, " +
                    "but does not prove romantic interest.";
            confidence = 65;

        } else if (positive) {

            tone = "Positive or friendly";
            signal =
                    "The message may reflect a friendly response.";
            nextMove =
                    "Match their energy and continue naturally.";
            explanation =
                    "Positive words or laughter can indicate warmth, " +
                    "but do not prove attraction.";
            confidence = 55;

        } else if (greeting) {

            tone = "Opening conversation";
            signal =
                    "The person has initiated or continued a greeting.";
            nextMove =
                    "Greet them back and ask a simple, natural question.";
            explanation =
                    "A greeting opens a conversation but reveals little " +
                    "about the person's deeper intentions.";
            confidence = 45;
        }

        return new Analysis(
                latestMessage,
                tone,
                signal,
                nextMove,
                explanation,
                confidence
        );
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