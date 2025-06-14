package io.github.austinhoover.rpg.intent;

import java.util.ArrayList;
import java.util.List;

public class GameLog {
    private final List<LogEntry> entries;
    private static final int MAX_ENTRIES = 10; // Keep last 10 entries

    public GameLog() {
        this.entries = new ArrayList<>();
    }

    public void addEntry(String playerInput, String storyResponse) {
        entries.add(new LogEntry(playerInput, storyResponse));
        if (entries.size() > MAX_ENTRIES) {
            entries.remove(0); // Remove oldest entry
        }
    }

    public List<LogEntry> getRecentEntries() {
        return new ArrayList<>(entries);
    }

    public void clear() {
        entries.clear();
    }

    public static class LogEntry {
        private final String playerInput;
        private final String storyResponse;

        public LogEntry(String playerInput, String storyResponse) {
            this.playerInput = playerInput;
            this.storyResponse = storyResponse;
        }

        public String getPlayerInput() {
            return playerInput;
        }

        public String getStoryResponse() {
            return storyResponse;
        }
    }
} 