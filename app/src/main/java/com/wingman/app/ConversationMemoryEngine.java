package com.wingman.app;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class ConversationMemoryEngine {

    private ConversationMemoryEngine() {}

    public static final class MemoryAnalysis {
        public final String recurringTopics;
        public final String conversationPattern;
        public final String previousContext;
        public final String continuityAdvice;
        public final List<String> keyDetails;

        public MemoryAnalysis(
                String recurringTopics,
                String conversationPattern,
                String previousContext,
                String continuityAdvice,
                List<String> keyDetails
        ) {
            this.recurringTopics = recurringTopics;
            this.conversationPattern = conversationPattern;
            this.previousContext = previousContext;
            this.continuityAdvice = continuityAdvice;
            this.keyDetails = keyDetails;
        }
    }

    public static MemoryAnalysis analyze(String conversation) {

        List<String> details = new ArrayList<>();

        if (conversation == null || conversation.trim().isEmpty()) {
            return new MemoryAnalysis(
                    "No topics identified.",
                    "No conversation available.",
                    "No previous context available.",
                    "Add a conversation to analyze its context.",
                    details
            );
        }

        String[] lines = conversation.split("\\r?\\n");
        StringBuilder cleaned = new StringBuilder();
        Set<String> topics = new LinkedHashSet<>();

        int questionCount = 0;
        int positiveCount = 0;
        int laughterCount = 0;
        int boundaryCount = 0;
        int messageCount = 0;

        for (String rawLine : lines) {
            String line = rawLine.trim();

            if (line.isEmpty()) {
                continue;
            }

            messageCount++;
            String lower = line.toLowerCase(Locale.ROOT);
            cleaned.append(line).append("\n");

            if (line.contains("?")) {
                questionCount++;
            }

            if (containsAny(lower,
                    "haha", "hahaha", "lol", "😂", "🤣")) {
                laughterCount++;
            }

            if (containsAny(lower,
                    "thank you", "thanks", "nice", "great",
                    "love it", "that's good", "thats good")) {
                positiveCount++;
            }

            if (containsAny(lower,
                    "leave me alone", "stop messaging",
                    "don't contact me", "do not contact me",
                    "not interested", "mujhe message mat",
                    "baat nahi karni")) {
                boundaryCount++;
            }

            addTopicIfFound(topics, lower, "work",
                    "work", "job", "office", "career");
            addTopicIfFound(topics, lower, "studies",
                    "study", "exam", "college", "school");
            addTopicIfFound(topics, lower, "family",
                    "family", "mother", "father", "sister", "brother");
            addTopicIfFound(topics, lower, "plans",
                    "plan", "weekend", "tomorrow", "meet", "meeting");
            addTopicIfFound(topics, lower, "feelings",
                    "feel", "feeling", "miss you", "like you", "love");
            addTopicIfFound(topics, lower, "hobbies",
                    "music", "movie", "film", "game", "travel");
            addTopicIfFound(topics, lower, "daily life",
                    "today", "yesterday", "day", "busy", "tired");
            addTopicIfFound(topics, lower, "food",
                    "food", "lunch", "dinner", "breakfast", "eat");
        }

        String latest = getLatestMessage(cleaned.toString());

        if (!latest.isEmpty()) {
            details.add("Latest message: " + latest);
        }

        details.add("Messages/lines detected: " + messageCount);

        if (questionCount > 0) {
            details.add("Lines containing questions: " + questionCount);
        }

        String topicText = topics.isEmpty()
                ? "No recurring topic detected from the current keyword rules."
                : join(topics);

        String pattern;

        if (boundaryCount > 0) {
            pattern = "A possible boundary or request for space appears. Respect it.";
        } else if (questionCount >= 3) {
            pattern = "Several questions appear in the supplied text.";
        } else if (laughterCount > 0 && positiveCount > 0) {
            pattern = "Some messages contain laughter and positive wording.";
        } else if (questionCount > 0) {
            pattern = "At least one question appears in the supplied text.";
        } else {
            pattern = "No strong communication pattern detected by these simple rules.";
        }

        String previousContext;

        if (messageCount >= 2) {
            previousContext =
                    "Earlier lines are available for context. Review them alongside the latest message.";
        } else {
            previousContext =
                    "Only one non-empty line was detected, so conversation history is limited.";
        }

        String advice;

        if (boundaryCount > 0) {
            advice = "Do not use past conversation details to pressure someone. Respect their stated boundary.";
        } else if (!topics.isEmpty()) {
            advice = "If relevant, naturally refer to an earlier topic instead of abruptly changing the subject.";
        } else {
            advice = "Use the actual conversation as context. Avoid inventing memories or assuming hidden meanings.";
        }

        return new MemoryAnalysis(
                topicText,
                pattern,
                previousContext,
                advice,
                details
        );
    }

    private static void addTopicIfFound(
            Set<String> topics,
            String text,
            String topic,
            String... keywords
    ) {
        if (containsAny(text, keywords)) {
            topics.add(topic);
        }
    }

    private static boolean containsAny(
            String text,
            String... phrases
    ) {
        for (String phrase : phrases) {
            if (text.contains(phrase)) {
                return true;
            }
        }
        return false;
    }

    private static String getLatestMessage(String conversation) {
        String[] lines = conversation.split("\\r?\\n");

        for (int i = lines.length - 1; i >= 0; i--) {
            String line = lines[i].trim();

            if (!line.isEmpty()) {
                return line;
            }
        }

        return "";
    }

    private static String join(Set<String> values) {
        StringBuilder result = new StringBuilder();

        for (String value : values) {
            if (result.length() > 0) {
                result.append(", ");
            }

            result.append(value);
        }

        return result.toString();
    }
}
