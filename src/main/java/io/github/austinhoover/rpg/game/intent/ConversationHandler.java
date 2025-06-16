package io.github.austinhoover.rpg.game.intent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;

import io.github.austinhoover.rpg.kobold.Kobold;
import io.github.austinhoover.rpg.game.character.Character;
import io.github.austinhoover.rpg.game.location.Location;
import io.github.austinhoover.rpg.game.player.PlayerState;
import io.github.austinhoover.rpg.game.world.World;

public class ConversationHandler {
    private World world;
    private PlayerState player;
    private Kobold kobold;
    private Conversation currentConversation;

    private static final Pattern MESSAGE_PATTERN = Pattern.compile(
        "['\\\"]([^'\\\"]+)[\\\"]",
        Pattern.CASE_INSENSITIVE
    );

    public ConversationHandler(World world, PlayerState player, Kobold kobold) {
        this.world = world;
        this.kobold = kobold;
        this.currentConversation = new Conversation();
    }

    public void handleTalk(String targetName, String message) {
        Location currentLocation = world.getLocationMap().getLocationById(player.currentLocationId);
        if (currentLocation == null) {
            System.out.println("Error: Invalid current location.");
            return;
        }

        // Find the target character by name or role
        String targetLower = targetName.toLowerCase();
        Character targetCharacter = null;
        for (Character character : world.getCharacterMap().getAllCharacters()) {
            if (character.getCurrentLocationId() == currentLocation.getId() &&
                (character.getName().toLowerCase().equals(targetLower) ||
                 character.getRole().toLowerCase().equals(targetLower))) {
                targetCharacter = character;
                break;
            }
        }

        if (targetCharacter == null) {
            System.out.println("You don't see anyone like \"" + targetName + "\" here.");
            return;
        }

        // Add player's message to conversation history
        currentConversation.addEntry("You", message, true);
        System.out.println("You say: \"" + message + "\"");

        // Construct prompt for character response
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append(String.format(
            "You are roleplaying as %s, a %s in %s. " +
            "The player has just said to you: \"%s\"\n\n",
            targetCharacter.getName(),
            targetCharacter.getRole(),
            currentLocation.getType(),
            message
        ));

        // Add recent conversation history if available
        List<Conversation.DialogueEntry> entries = currentConversation.getEntries();
        if (!entries.isEmpty()) {
            promptBuilder.append("Recent conversation:\n");
            // Include up to the last 3 messages
            int startIndex = Math.max(0, entries.size() - 3);
            for (int i = startIndex; i < entries.size(); i++) {
                Conversation.DialogueEntry entry = entries.get(i);
                promptBuilder.append(String.format("%s: \"%s\"\n", 
                    entry.speaker, entry.message));
            }
            promptBuilder.append("\n");
        }

        promptBuilder.append(
            "Respond naturally as your character would, considering:\n" +
            "1. Your role and personality as a " + targetCharacter.getRole() + "\n" +
            "2. The setting of " + currentLocation.getType() + "\n" +
            "3. The context of the player's message\n" +
            "4. The recent conversation history\n\n" +
            "Keep your response brief and in character. Surround the response in quotation marks."
        );

        try {
            String response = kobold.request(promptBuilder.toString()).trim();
            Matcher matcher = MESSAGE_PATTERN.matcher(response);
            if (matcher.find()) {
                response = matcher.group(1);
            } else {
                throw new Error("No message found in response");
            }
            System.out.println(targetCharacter.getName() + ": \"" + response + "\"");
            // Add character's response to conversation history
            currentConversation.addEntry(targetCharacter.getName(), response, false);
        } catch (Exception | Error e) {
            System.err.println("Error generating character response: " + e.getMessage());
            System.out.println(targetCharacter.getName() + " seems to be lost in thought.");
        }
    }

    public String handleTalkWithReturn(String targetName, String message) {
        Location currentLocation = world.getLocationMap().getLocationById(player.currentLocationId);
        if (currentLocation == null) {
            String error = "Error: Invalid current location.";
            System.out.println(error);
            return error;
        }
        String targetLower = targetName.toLowerCase();
        Character targetCharacter = null;
        for (Character character : world.getCharacterMap().getAllCharacters()) {
            if (character.getCurrentLocationId() == currentLocation.getId() &&
                (character.getName().toLowerCase().equals(targetLower) ||
                 character.getRole().toLowerCase().equals(targetLower))) {
                targetCharacter = character;
                break;
            }
        }
        if (targetCharacter == null) {
            String notFound = "You don't see anyone like \"" + targetName + "\" here.";
            System.out.println(notFound);
            return notFound;
        }
        currentConversation.addEntry("You", message, true);
        System.out.println("You say: \"" + message + "\"");
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append(String.format(
            "You are roleplaying as %s, a %s in %s. " +
            "The player has just said to you: \"%s\"\n\n",
            targetCharacter.getName(),
            targetCharacter.getRole(),
            currentLocation.getType(),
            message
        ));
        List<Conversation.DialogueEntry> entries = currentConversation.getEntries();
        if (!entries.isEmpty()) {
            promptBuilder.append("Recent conversation:\n");
            int startIndex = Math.max(0, entries.size() - 3);
            for (int i = startIndex; i < entries.size(); i++) {
                Conversation.DialogueEntry entry = entries.get(i);
                promptBuilder.append(String.format("%s: \"%s\"\n", 
                    entry.speaker, entry.message));
            }
            promptBuilder.append("\n");
        }
        promptBuilder.append(
            "Respond naturally as your character would, considering:\n" +
            "1. Your role and personality as a " + targetCharacter.getRole() + "\n" +
            "2. The setting of " + currentLocation.getType() + "\n" +
            "3. The context of the player's message\n" +
            "4. The recent conversation history\n\n" +
            "Keep your response brief and in character. Surround the response in quotation marks."
        );
        try {
            String response = kobold.request(promptBuilder.toString()).trim();
            Matcher matcher = MESSAGE_PATTERN.matcher(response);
            if (matcher.find()) {
                response = matcher.group(1);
            } else {
                throw new Error("No message found in response");
            }
            String output = targetCharacter.getName() + ": \"" + response + "\"";
            System.out.println(output);
            currentConversation.addEntry(targetCharacter.getName(), response, false);
            return output;
        } catch (Exception | Error e) {
            String error = targetCharacter.getName() + " seems to be lost in thought.";
            System.err.println("Error generating character response: " + e.getMessage());
            System.out.println(error);
            return error;
        }
    }

    public void resetConversation() {
        currentConversation.clear();
    }

    public Conversation getCurrentConversation() {
        return currentConversation;
    }

    public void setWorld(World world) {
        this.world = world;
    }
} 