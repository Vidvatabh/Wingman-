package com.wingman.app;

import java.util.Locale;

public final class NextMoveEngine {

    private NextMoveEngine() {}

    public static final class NextMove {
        public final String action;
        public final String reason;
        public final String whenToUse;
        public final String avoid;

        public NextMove(
                String action,
                String reason,
                String whenToUse,
                String avoid
        ) {
            this.action = action;
            this.reason = reason;
            this.whenToUse = whenToUse;
            this.avoid = avoid;
        }
    }

    public static NextMove analyze(String conversation) {

        if (conversation == null || conversation.trim().isEmpty()) {
            return new NextMove(
                    "Add the conversation first",
                    "The engine needs context before suggesting a next move.",
                    "Paste the relevant conversation, including the latest message.",
                    "Avoid drawing conclusions without any messages."
            );
        }

        String text = conversation.toLowerCase(Locale.ROOT).trim();
        String latestMessage = getLatestMessage(text);

        // 1. Respect clear boundaries.
        if (containsAny(text,
                "leave me alone",
                "stop messaging",
                "don't contact me",
                "do not contact me",
                "dont contact me",
                "not interested",
                "please stop",
                "mujhe message mat",
                "baat nahi karni",
                "contact mat karna")) {

            return new NextMove(
                    "Respect their boundary",
                    "The conversation contains language that may indicate a clear request for space.",
                    "Stop pursuing the conversation. Reply briefly only if necessary.",
                    "Do not pressure them, repeatedly message them, or try to change their mind."
            );
        }

        // 2. Detect a request for space.
        if (containsAny(text,
                "i need space",
                "need some space",
                "i need time",
                "not ready to talk",
                "mujhe time chahiye",
                "thoda space chahiye",
                "abhi baat nahi")) {

            return new NextMove(
                    "Give them space",
                    "They may need time before continuing the conversation.",
                    "Acknowledge their request calmly and let them initiate when ready.",
                    "Avoid demanding an explanation or sending repeated follow-ups."
            );
        }

        // 3. A direct question usually deserves a direct answer.
        if (latestMessage.contains("?")
                || containsAny(latestMessage,
                "how are you",
                "how was your day",
                "what are you doing",
                "what do you do",
                "where are you",
                "kaise ho",
                "kya kar rahe",
                "kahan ho",
                "kyu",
                "kyun")) {

            return new NextMove(
                    "Answer, then continue naturally",
                    "The latest message appears to invite a response or provide an opening.",
                    "Answer honestly and add one relevant question if it feels natural.",
                    "Avoid turning a simple exchange into an interview with multiple questions."
            );
        }

        // 4. Positive or playful conversation.
        if (containsAny(latestMessage,
                "haha",
                "hahaha",
                "lol",
                "😂",
                "🤣",
                "that is funny",
                "that's funny",
                "cute",
                "nice",
                "sounds good",
                "mast hai",
                "accha hai",
                "achha hai")) {

            return new NextMove(
                    "Keep the conversation light",
                    "The latest message may indicate a relaxed or playful tone, but it is not proof of attraction.",
                    "Respond to what they said with a natural comment, playful remark, or related topic.",
                    "Avoid assuming romantic interest from laughter or friendliness alone."
            );
        }

        // 5. A short acknowledgement may mean the topic has slowed.
        if (containsAny(latestMessage,
                "ok",
                "okay",
                "hmm",
                "hmmm",
                "acha",
                "achha",
                "theek hai",
                "fine",
                "right",
                "cool")) {

            return new NextMove(
                    "Check whether the topic has more to offer",
                    "This could be a simple acknowledgement, a busy moment, or reduced interest. One short reply is not enough to know.",
                    "If you have something relevant to add, continue once. Otherwise, let the conversation pause.",
                    "Avoid forcing a new topic or repeatedly asking whether something is wrong."
            );
        }

        // 6. Appreciation.
        if (containsAny(latestMessage,
                "thank you",
                "thanks",
                "thankyou",
                "shukriya",
                "dhanyawad")) {

            return new NextMove(
                    "Acknowledge warmly",
                    "The message expresses appreciation and may not require a long response.",
                    "Reply naturally and continue only if there is something relevant to discuss.",
                    "Avoid extending the conversation unnecessarily just to keep it alive."
            );
        }

        // 7. Default recommendation.
        return new NextMove(
                "Respond to the actual message",
                "There is not enough clear evidence to identify a specific intention confidently.",
                "Address their latest point, share a genuine thought, or ask one relevant follow-up.",
                "Avoid guessing their feelings, overthinking a single message, or using pressure to get a response."
        );
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

    private static boolean containsAny(String text, String... phrases) {
        for (String phrase : phrases) {
            if (text.contains(phrase)) {
                return true;
            }
        }
        return false;
    }
}
