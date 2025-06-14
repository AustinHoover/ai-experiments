package io.github.austinhoover;

import java.util.Scanner;

import io.github.austinhoover.kobold.Kobold;
import io.github.austinhoover.rpg.character.Character;
import io.github.austinhoover.rpg.character.CharacterMap;
import io.github.austinhoover.rpg.intent.IntentParser;
import io.github.austinhoover.rpg.intent.MovementHandler;
import io.github.austinhoover.rpg.intent.ConversationHandler;
import io.github.austinhoover.rpg.intent.StoryHandler;
import io.github.austinhoover.rpg.location.Location;
import io.github.austinhoover.rpg.location.LocationMap;
import io.github.austinhoover.rpg.player.PlayerState;
import io.github.austinhoover.rpg.intent.GameLog;

/**
 * Main app class
 */
public final class App {

    /**
     * Entrypoint to app
     */
    public static void main(String[] args){
        Kobold kobold = new Kobold();
        LocationMap graph = new LocationMap();
        CharacterMap characters = new CharacterMap();
        PlayerState player = new PlayerState();
        IntentParser parser = new IntentParser(kobold);
        ConversationHandler conversation = new ConversationHandler(graph, characters, player, kobold);
        MovementHandler mover = new MovementHandler(graph, player, kobold, conversation);
        GameLog gameLog = new GameLog();
        StoryHandler story = new StoryHandler(graph, characters, player, kobold, conversation, gameLog);

        //create world
        Location tavern = Location.create(graph, "tavern", "A warm tavern filled with rowdy patrons.");
        Location cellar = Location.create(graph, "cellar", "A damp cellar beneath the tavern.");
        Location alley = Location.create(graph, "alley", "A shadowy alley between tall buildings.");
        tavern.addNeighbor(alley);
        tavern.addNeighbor(cellar);
        player.currentLocationId = alley.getId();

        // Create initial character
        Character bartender = Character.create(characters, "Tom", "bartender", tavern.getId());

        Scanner scanner = new Scanner(System.in);
        System.out.println("Type 'exit' to quit.");
        mover.describeCurrentLocation();

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) break;

            IntentParser.Intent intent = parser.parse(input);
            String response = null;
            switch (intent.type) {
                case MOVE -> {
                    if (mover.attemptMove(intent.target)) {
                        response = "You travel to " + graph.getLocationById(player.currentLocationId).getType() + ".";
                        mover.describeCurrentLocation();
                    } else {
                        response = "You can't go there.";
                    }
                }
                case LOOK -> {
                    // Capture output for logging
                    response = mover.lookForNearbyLocationsWithReturn(kobold);
                }
                case TALK -> {
                    response = conversation.handleTalkWithReturn(intent.target, intent.message);
                }
                default -> {
                    response = story.handleUnknownIntent(input);
                }
            }
            gameLog.addEntry(input, response);
        }

        //cleanup
        scanner.close();
    }
}
