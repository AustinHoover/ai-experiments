package io.github.austinhoover;

import java.util.Scanner;

import io.github.austinhoover.rpg.Global;
import io.github.austinhoover.rpg.model.DefaultGameData;
import io.github.austinhoover.rpg.sim.Simulation;
import io.github.austinhoover.rpg.world.World;

/**
 * Main app class
 */
public final class App {

    /**
     * Entrypoint to app
     */
    public static void main(String[] args){
        // Load default game data
        DefaultGameData defaultData = DefaultGameData.loadFromFile("data/defaultData.json");
        new World(defaultData);

        // Get user input for world generation
        Scanner scanner = new Scanner(System.in);

        Global.player.currentLocationId = Global.world.getLocationMap().getLocationById(0).getId();

        System.out.println("\nType 'exit' to quit.");
        Global.mover.describeCurrentLocation();

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) break;

            Simulation.simulate(input);
        }

        //cleanup
        scanner.close();
    }
}
