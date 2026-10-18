package com.wingman.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class SocialIntelligenceEngine {

    private SocialIntelligenceEngine() {}

    public static class SocialAnalysis {
        public String communicationStyle;
        public String possibleInterpretation;
        public String alternativeInterpretation;
        public String evidence;
        public String recommendedApproach;
        public int confidence;

        public SocialAnalysis(
                String communicationStyle,
                String possibleInterpretation,
                String alternativeInterpretation,
                String evidence,
                String recommendedApproach,
                int confidence
        ) {
            this.communicationStyle = communicationStyle;
            this.possibleInterpretation = possibleInterpretation;
            this.alternativeInterpretation = alternativeInterpretation;
            this.evidence = evidence;
            this.recommendedApproach = recommendedApproach;
            this.confidence = confidence;
        }
    }

    public static SocialAnalysis analyze(String conversation) {

        if (conversation == null || conversation.trim().isEmpty()) {
            return new SocialAnalysis(
                    "Unknown",
                    "Not enough information",
                    "No interpretation available",
                    "No conversation provided",
                    "Provide conversation context first.",
                    0
            );
        }

        String[] lines = conversation.split("\\r?\\n");
        List<String> messages = new ArrayList<>();

        for (String line : lines) {
            String cleaned = line.trim();

            if (!cleaned.isEmpty()) {
                messages.add(cleaned);
            }
        }

        String lower = conversation.toLowerCase(Locale.ROOT);

        int questions = count(lower, "?");
        int laughter = countAny(
                lower, "haha", "hahaha", "lol", "lmao", "😂"
        );

        int positive = countAny(
                lower, "thank you", "thanks", "nice",
                "great", "😊", "❤️", "❤"
        );

        int boundary = countAny(
                lower, "leave me alone",
                "stop messaging", "not interested",
                "don't contact me", "do not contact me"
        );

        int shortMessages = 0;

        for (String message : messages) {
            if (message.length() <= 12) {
                shortMessages++;
            }
        }

        String style = "Mixed or unclear communication";
        String interpretation =
                "The available text does not establish a clear intention.";
        String alternative =
                "The person may simply communicate differently.";
        String evidence =
                "There is not enough reliable evidence to identify a pattern.";
        String approach =
                "Respond naturally and look at the wider conversation.";
        int confidence = 20;

        if (boundary > 0) {

            style = "Boundary-setting language";
            interpretation =
                    "The person may be asking for distance.";
            alternative =
                    "The exact context may affect the intended meaning, " +
                    "but the boundary should still be respected.";
            evidence =
                    "The conversation includes explicit boundary-related wording.";
            approach =
                    "Respect the request. Do not pressure them to continue.";
            confidence = 85;

        } else if (questions >= 2 && messages.size() >= 3) {

            style = "Question-oriented conversation";
            interpretation =
                    "The conversation includes repeated questions.";
            alternative =
                    "The person may be curious, polite, or keeping " +
                    "the conversation moving.";
            evidence =
                    "Multiple question marks appear in the provided text.";
            approach =
                    "Answer clearly and notice whether both people " +
                    "contribute to the conversation.";
            confidence = 55;

        } else if (laughter >= 2 || positive >= 2) {

            style = "Warm or playful language";
            interpretation =
                    "Some messages may convey friendliness or playfulness.";
            alternative =
                    "Friendly language alone does not establish flirting " +
                    "or romantic attraction.";
            evidence =
                    "Repeated laughter or positive expressions were found.";
            approach =
                    "Match the tone naturally without assuming attraction.";
            confidence = 50;

        } else if (messages.size() >= 4 &&
                shortMessages * 2 >= messages.size()) {

            style = "Mostly brief messages";
            interpretation =
                    "The conversation contains many short messages.";
            alternative =
                    "The person could be busy, concise, distracted, " +
                    "or simply prefer short messages.";
            evidence =
                    "At least half of the non-empty lines are short.";
            approach =
                    "Keep your reply concise and see whether the " +
                    "conversation develops naturally.";
            confidence = 45;
        }

        return new SocialAnalysis(
                style,
                interpretation,
                alternative,
                evidence,
                approach,
                confidence
        );
    }

    private static int count(String text, String target) {

        int total = 0;
        int position = 0;

        while ((position = text.indexOf(target, position)) != -1) {
            total++;
            position += target.length();
        }

        return total;
    }

    private static int countAny(
            String text,
            String... terms
    ) {

        int total = 0;

        for (String term : terms) {
            total += count(text, term);
        }

        return total;
    }
}
