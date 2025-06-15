package io.github.austinhoover.rpg.politics;

import java.util.List;
import java.util.Random;

/**
 * Generates political states for the world
 */
public class PoliticalStateGenerator {
    private static final Random random = new Random();
    private static final String[] STATE_TYPES = {
        "Kingdom", "Empire", "Republic", "Federation", "Duchy", 
        "Principality", "Theocracy", "Confederation", "Alliance"
    };

    /**
     * Generates political states for each race
     * @param map The political state map to register states with
     * @param races List of race names to generate states for
     * @return The number of states generated
     */
    public static int generateStates(PoliticalStateMap map, List<String> races) {
        int totalStates = 0;
        
        for (String race : races) {
            // Generate 3-5 states for each race
            int numStates = random.nextInt(3) + 3; // Random number between 3 and 5
            
            for (int i = 0; i < numStates; i++) {
                String stateType = STATE_TYPES[random.nextInt(STATE_TYPES.length)];
                String stateName = String.format("%s of %s %d", stateType, race, i + 1);
                
                PoliticalState.create(map, stateName, race);
                totalStates++;
            }
        }
        
        return totalStates;
    }
} 