package io.github.austinhoover.rpg;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.austinhoover.rpg.game.Global;
import io.github.austinhoover.rpg.game.model.DefaultGameData;
import io.github.austinhoover.rpg.game.sim.Simulation;
import io.github.austinhoover.rpg.game.world.World;

/**
 * Main app class
 */
@SpringBootApplication
public class App {

    /**
     * Entrypoint to app
     */
    public static void main(String[] args){
        // Load default game data
        DefaultGameData defaultData = DefaultGameData.loadFromFile("data/defaultData.json");
        Global.world = new World(defaultData);
        Global.conversation.setWorld(Global.world);
        Global.mover.setWorld(Global.world);
        Global.story.setWorld(Global.world);

        // Get user input for world generation
        Scanner scanner = new Scanner(System.in);

        Global.player.currentLocationId = 0;

        // Add initial location description to game log
        String initialDescription = Global.mover.describeCurrentLocationWithReturn();
        Global.gameLog.addEntry("", initialDescription);

        System.out.println("\nType 'exit' to quit.");
        Global.mover.describeCurrentLocation();

        // Start Spring Boot server
        SpringApplication.run(App.class, args);

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
