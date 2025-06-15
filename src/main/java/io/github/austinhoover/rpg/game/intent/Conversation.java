package io.github.austinhoover.rpg.game.intent;

import java.util.ArrayList;
import java.util.List;

public class Conversation {
    public static class DialogueEntry {
        public final String speaker;
        public final String message;
        public final boolean isPlayer;

        public DialogueEntry(String speaker, String message, boolean isPlayer) {
            this.speaker = speaker;
            this.message = message;
            this.isPlayer = isPlayer;
        }
    }

    private List<DialogueEntry> entries = new ArrayList<>();

    public void addEntry(String speaker, String message, boolean isPlayer) {
        entries.add(new DialogueEntry(speaker, message, isPlayer));
    }

    public void clear() {
        entries.clear();
    }

    public List<DialogueEntry> getEntries() {
        return entries;
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }
} 