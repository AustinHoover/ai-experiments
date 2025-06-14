package io.github.austinhoover;

import java.util.Scanner;

import io.github.austinhoover.kobold.Kobold;
import io.github.austinhoover.rpg.intent.IntentParser;
import io.github.austinhoover.rpg.intent.MovementHandler;
import io.github.austinhoover.rpg.intent.ConversationHandler;
import io.github.austinhoover.rpg.intent.StoryHandler;
import io.github.austinhoover.rpg.player.PlayerState;
import io.github.austinhoover.rpg.world.World;
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
        World world = World.loadWorld("data/testworld1.json");
        PlayerState player = new PlayerState();
        IntentParser parser = new IntentParser(kobold);
        ConversationHandler conversation = new ConversationHandler(world, player, kobold);
        MovementHandler mover = new MovementHandler(world, player, kobold, conversation);
        GameLog gameLog = new GameLog();
        StoryHandler story = new StoryHandler(world, player, kobold, conversation, gameLog);

        // Get user input for world generation
        Scanner scanner = new Scanner(System.in);

        player.currentLocationId = world.getLocationMap().getLocationById(0).getId();

        System.out.println("\nType 'exit' to quit.");
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
                        response = "You travel to " + world.getLocationMap().getLocationById(player.currentLocationId).getType() + ".";
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
