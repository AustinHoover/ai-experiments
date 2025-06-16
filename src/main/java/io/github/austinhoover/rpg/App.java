package io.github.austinhoover.rpg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.austinhoover.rpg.process.ProcessManager;
import io.github.austinhoover.rpg.process.ServiceConfig;
import io.github.austinhoover.rpg.game.sim.Simulation;
import io.github.austinhoover.rpg.game.world.World;
import java.util.Scanner;

import io.github.austinhoover.rpg.game.Global;
import io.github.austinhoover.rpg.game.model.DefaultGameData;

/**
 * Main app class
 */
@SpringBootApplication
public class App {

    /**
     * Entrypoint to app
     */
    public static void main(String[] args) {
        // Add shutdown hook to clean up processes
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down application...");
            ProcessManager.INSTANCE.shutdownAll();
        }));

        ServiceConfig koboldConfig = ProcessManager.INSTANCE.loadConfigFile("kobold");
        if(koboldConfig != null) {
            ProcessManager.INSTANCE.registerService(koboldConfig);
            ProcessManager.INSTANCE.startService("kobold");
        } else {
            System.out.println("Failed to load kobold config");
        }

        // Load default game data
        DefaultGameData defaultData = DefaultGameData.loadFromFile("data/defaultData.json");
        Global.world = new World(defaultData);
        Global.conversation.setWorld(Global.world);
        Global.mover.setWorld(Global.world);
        Global.story.setWorld(Global.world);

        // Get user input for world generation
        Scanner scanner = new Scanner(System.in);

        Global.player.currentLocationId = 1;

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
