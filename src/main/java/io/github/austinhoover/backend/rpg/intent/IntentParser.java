package io.github.austinhoover.backend.rpg.intent;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import io.github.austinhoover.backend.kobold.Kobold;

/**
 * Parses intent of player actions
 */
public class IntentParser {
    private final Kobold kobold;

    public IntentParser(Kobold kobold) {
        this.kobold = kobold;
    }

    public enum Type {
        MOVE, LOOK, TALK, UNKNOWN
    }

    public static class Intent {
        public Type type;
        public String target;
        public String message;

        public Intent(Type type, String target) {
            this.type = type;
            this.target = target;
        }

        public Intent(Type type, String target, String message) {
            this.type = type;
            this.target = target;
            this.message = message;
        }

        @Override
        public String toString(){
            return "Intent{" + "type=" + type + ", target='" + target + '\'' + 
                (message != null ? ", message='" + message + '\'' : "") + '}';
        }
    }

    private static final Pattern MOVE_PATTERN = Pattern.compile(
        "\\b(go|walk|enter|head|move|step|climb)\\s+(to|into|through|up|down)?\\s*(the\\s+)?(?<target>[a-zA-Z0-9\\s'-]+)",
        Pattern.CASE_INSENSITIVE
    );

    private static final Pattern TALK_INTENT_PATTERN = Pattern.compile(
        "(say|speak|talk)",
        Pattern.CASE_INSENSITIVE
    );

    private static final Pattern MESSAGE_PATTERN = Pattern.compile(
        "['\\\"]([^'\\\"]+)[\\\"]",
        Pattern.CASE_INSENSITIVE
    );

    public Intent parse(String input) {
        input = input.trim().toLowerCase();

        // Check for talk intent first
        if (TALK_INTENT_PATTERN.matcher(input).find()) {
            // Extract message first
            Matcher messageMatcher = MESSAGE_PATTERN.matcher(input);
            String message = messageMatcher.find() ? messageMatcher.group(1) : null;

            if (message != null && kobold != null) {
                // Use LLM to extract target
                String target = extractTargetWithLLM(input, message);
                if (target != null) {
                    return new Intent(Type.TALK, target, message);
                }
            }
        }

        Matcher moveMatcher = MOVE_PATTERN.matcher(input);
        if (moveMatcher.find()) {
            String target = moveMatcher.group("target").trim();
            return new Intent(Type.MOVE, target);
        }

        if (input.startsWith("look") || input.startsWith("examine")) {
            return new Intent(Type.LOOK, null);
        }

        return new Intent(Type.UNKNOWN, null);
    }

    private String extractTargetWithLLM(String input, String message) {
        String prompt = 
            "You are a parser that extracts the target person or character from a sentence. " +
            "The target is who the speaker is talking to. " +
            "Return ONLY the target name/role, nothing else. " +
            "If you can't determine the target, return null. " +
            "Examples:\n" +
            "Input: \"I say to Tom 'Hello'\" -> Output: \"Tom\"\n" +
            "Input: \"I speak to the bartender and say 'Hello'\" -> Output: \"bartender\"\n" +
            "Input: \"I say 'Hello'\" -> Output: null\n\n" +
            "Input: \"" + input + "\"\n" +
            "Message: \"" + message + "\"";

        try {
            String response = kobold.request(prompt).trim().replace("\"", "");
            return response.equals("null") ? null : response;
        } catch (Exception e) {
            System.err.println("Error calling LLM: " + e.getMessage());
            return null;
        }
    }
}
