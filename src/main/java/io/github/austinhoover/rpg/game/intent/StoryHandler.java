package io.github.austinhoover.rpg.game.intent;

import java.util.List;
import java.util.stream.Collectors;

import io.github.austinhoover.rpg.kobold.Kobold;
import io.github.austinhoover.rpg.game.character.Character;
import io.github.austinhoover.rpg.game.location.Location;
import io.github.austinhoover.rpg.game.player.PlayerState;
import io.github.austinhoover.rpg.game.world.World;

public class StoryHandler {
    private World world;
    private PlayerState player;
    private Kobold kobold;
    private ConversationHandler conversationHandler;
    private GameLog gameLog;

    public StoryHandler(World world, PlayerState player, 
                       Kobold kobold, ConversationHandler conversationHandler, GameLog gameLog) {
        this.world = world;
        this.player = player;
        this.kobold = kobold;
        this.conversationHandler = conversationHandler;
        this.gameLog = gameLog;
    }

    public String handleUnknownIntent(String input) {
        Location currentLocation = world.getLocationMap().getLocationById(player.currentLocationId);
        if (currentLocation == null) {
            String errorMsg = "Error: Invalid current location.";
            System.out.println(errorMsg);
            return errorMsg;
        }

        // Get nearby characters
        List<Character> nearbyCharacters = world.getCharacterMap().getAllCharacters().stream()
            .filter(c -> c.getCurrentLocationId() == currentLocation.getId())
            .collect(Collectors.toList());

        // Build context for the story
        StringBuilder contextBuilder = new StringBuilder();
        contextBuilder.append(String.format(
            "You are narrating a story where the player is in %s. %s\n\n",
            currentLocation.getType(),
            currentLocation.getDescription()
        ));

        // Add nearby characters
        if (!nearbyCharacters.isEmpty()) {
            contextBuilder.append("Nearby characters:\n");
            for (Character character : nearbyCharacters) {
                contextBuilder.append(String.format("- %s (a %s)\n", 
                    character.getName(), character.getRole()));
            }
            contextBuilder.append("\n");
        }

        // Add recent conversation history
        List<Conversation.DialogueEntry> recentConversation = conversationHandler.getCurrentConversation().getEntries();
        if (!recentConversation.isEmpty()) {
            contextBuilder.append("Recent conversation:\n");
            // Include up to the last 3 messages
            int startIndex = Math.max(0, recentConversation.size() - 3);
            for (int i = startIndex; i < recentConversation.size(); i++) {
                Conversation.DialogueEntry entry = recentConversation.get(i);
                contextBuilder.append(String.format("%s: \"%s\"\n", 
                    entry.speaker, entry.message));
            }
            contextBuilder.append("\n");
        }

        // Add recent game log entries
        List<GameLog.LogEntry> logEntries = gameLog.getRecentEntries();
        if (!logEntries.isEmpty()) {
            contextBuilder.append("Recent actions:\n");
            for (GameLog.LogEntry entry : logEntries) {
                contextBuilder.append("Player: ").append(entry.getPlayerInput()).append("\n");
                contextBuilder.append("Response: ").append(entry.getStoryResponse()).append("\n");
            }
            contextBuilder.append("\n");
        }

        // Add player's last action
        if (player.lastAction != null) {
            contextBuilder.append(String.format("The player's last action was: %s\n\n", 
                player.lastAction));
        }

        // Add the current input
        contextBuilder.append(String.format(
            "The player has just said or done: \"%s\"\n\n" +
            "Based on this context, narrate what happens next in the story. " +
            "Keep it brief and engaging. Focus on the immediate consequences or reactions " +
            "to the player's action. Do not use quotation marks in your response.",
            input
        ));

        String response;
        try {
            response = kobold.request(contextBuilder.toString()).trim();
            System.out.println(response);
        } catch (Exception e) {
            response = "Nothing particularly interesting happens.";
            System.err.println("Error generating story progression: " + e.getMessage());
            System.out.println(response);
        }
        gameLog.addEntry(input, response);
        return response;
    }

    /**
     * Sets the world instance
     * @param world The world instance to set
     */
    public void setWorld(World world) {
        this.world = world;
    }
} 