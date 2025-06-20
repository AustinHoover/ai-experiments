package io.github.austinhoover.rpg.game.location;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import io.github.austinhoover.rpg.game.politics.PoliticalState;
import io.github.austinhoover.rpg.game.race.Race;
import io.github.austinhoover.rpg.game.world.World;
import io.github.austinhoover.rpg.game.Global;
import io.github.austinhoover.rpg.game.character.Character;

/**
 * Generates regions for the world
 */
public class RegionGenerator {
    private static final Random random = new Random();
    private static final String[] TOWN_TYPES = {
        "town", "village", "hamlet", "settlement", "outpost"
    };
    
    private static final String[] TOWN_ROLES = {
        "innkeeper", "blacksmith", "merchant", "guard", "farmer",
        "baker", "tailor", "carpenter", "healer", "hunter"
    };

    private static final String[] GENDERS = {
        "male", "female"
    };

    /**
     * Generates a continent with subregions for each political state
     * @param world The world to generate regions in
     * @return The generated continent region
     */
    public static Region generateContinent(World world) {
        // Create the top-level continent region
        Region continent = Region.create(world.getRegionMap(), "continent", Optional.of("Continent"));
        
        // Keep track of state locations to connect them later
        List<Location> stateLocations = new ArrayList<>();
        
        // Create a subregion for each political state
        world.getPoliticalStateMap().getAllStates().forEach(state -> {
            // Create a subregion named after the state
            Region stateRegion = Region.create(world.getRegionMap(), "territory", Optional.of(state.getName()));
            
            // Add it as a subregion of the continent
            continent.addSubregion(stateRegion);
            
            // Create a location for this state
            Location stateLocation = Location.create(
                world.getLocationMap(),
                "capital",
                String.format("The capital city of %s.", state.getName()),
                stateRegion.getId()
            );
            
            // Add the location to the state's region
            stateRegion.addLocation(stateLocation);
            
            // Add to our list for later connection
            stateLocations.add(stateLocation);
            
            // Generate towns within this state's territory
            generateTown(world, stateRegion, state, stateLocation);
        });
        
        // Create a sparse but connected graph of locations
        if (!stateLocations.isEmpty()) {
            // First, create a minimum spanning tree to ensure connectivity
            List<Location> unconnected = new ArrayList<>(stateLocations);
            List<Location> connected = new ArrayList<>();
            
            // Start with a random location
            Location start = unconnected.remove(random.nextInt(unconnected.size()));
            connected.add(start);
            
            // Connect all locations in a tree structure
            while (!unconnected.isEmpty()) {
                Location toConnect = unconnected.remove(random.nextInt(unconnected.size()));
                Location connectTo = connected.get(random.nextInt(connected.size()));
                toConnect.addNeighbor(connectTo);
                connected.add(toConnect);
            }
            
            // Add a few random additional connections to create some cycles
            // but keep the graph sparse
            int numExtraConnections = stateLocations.size() / 2; // Add connections for half the locations
            for (int i = 0; i < numExtraConnections; i++) {
                Location loc1 = stateLocations.get(random.nextInt(stateLocations.size()));
                Location loc2 = stateLocations.get(random.nextInt(stateLocations.size()));
                if (loc1 != loc2 && !loc1.getNeighbors(world.getLocationMap()).contains(loc2)) {
                    loc1.addNeighbor(loc2);
                }
            }
        }
        
        return continent;
    }

    /**
     * Generates towns within a region for a given political state
     * @param world The world instance
     * @param region The region to generate towns in
     * @param state The political state that owns these towns
     * @param stateCapital The capital location of the state
     */
    public static void generateTown(World world, Region region, PoliticalState state, Location stateCapital) {
        // Generate 2-4 towns for each state
        int numTowns = random.nextInt(3) + 2;
        
        for (int i = 0; i < numTowns; i++) {
            String townType = TOWN_TYPES[random.nextInt(TOWN_TYPES.length)];
            String townName = String.format("%s %s %d", state.getRace(), townType, i + 1);
            
            // Create the town region
            Region town = Region.create(world.getRegionMap(), townType, Optional.of(townName));
            
            // Add it as a subregion of the state's territory
            region.addSubregion(town);
            
            // Create a location for this town
            Location townLocation = Location.create(
                world.getLocationMap(),
                townType,
                String.format("A %s in the territory of %s.", townType, state.getName()),
                town.getId()
            );
            
            // Add the location to the town's region
            town.addLocation(townLocation);
            
            // Connect the town to the state capital
            townLocation.addNeighbor(stateCapital);

            // Generate characters for this town
            generateTownCharacters(world, state, townLocation);
        }
    }

    /**
     * Generates characters for a town
     * @param world The world instance
     * @param state The political state that owns the town
     * @param location The town location
     */
    private static void generateTownCharacters(World world, PoliticalState state, Location location) {
        // Generate 3-6 characters for each town
        int numCharacters = random.nextInt(4) + 3;
        
        for (int i = 0; i < numCharacters; i++) {
            String role = TOWN_ROLES[random.nextInt(TOWN_ROLES.length)];
            String gender = GENDERS[random.nextInt(GENDERS.length)];
            String name = RegionGenerator.generateName(world.getRaceMap().getRaceByName(state.getRace()), gender, role);
            
            // Create the character
            Character character = Character.create(
                world.getCharacterMap(),
                name,
                role,
                gender,
                location.getId()
            );

            // Add the character to the location's character list
            location.getCharacterIds().add(character.getId());
        }
    }

    private static String generateName(Race race, String gender, String role){
        String prompt = 
            "I am generating names for characters for a " + gender + " " + race.getName() + ". Can you give me a name? Please write just the name."
        ;
        try {
            String response = Global.nameCache.getName(prompt);
            return response.equals("null") ? null : response;
        } catch (Exception e) {
            System.err.println("Error calling LLM: " + e.getMessage());
            return null;
        }
    }

} 