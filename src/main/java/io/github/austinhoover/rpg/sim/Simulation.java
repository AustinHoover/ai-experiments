package io.github.austinhoover.rpg.sim;

import io.github.austinhoover.rpg.Global;
import io.github.austinhoover.rpg.intent.IntentParser;

public class Simulation {
    
    public static void simulate(String input){
        IntentParser.Intent intent = Global.parser.parse(input);
        String response = null;
        switch (intent.type) {
            case MOVE -> {
                if (Global.mover.attemptMove(intent.target)) {
                    response = "You travel to " + Global.world.getLocationMap().getLocationById(Global.player.currentLocationId).getType() + ".";
                    Global.mover.describeCurrentLocation();
                } else {
                    response = "You can't go there.";
                }
            }
            case LOOK -> {
                // Capture output for logging
                response = Global.mover.lookForNearbyLocationsWithReturn(Global.kobold);
            }
            case TALK -> {
                response = Global.conversation.handleTalkWithReturn(intent.target, intent.message);
            }
            default -> {
                response = Global.story.handleUnknownIntent(input);
            }
        }
        Global.gameLog.addEntry(input, response);
    }

}
