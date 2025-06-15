package io.github.austinhoover.backend.rpg.world;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;

import io.github.austinhoover.backend.rpg.character.CharacterMap;
import io.github.austinhoover.backend.rpg.location.LocationMap;
import io.github.austinhoover.backend.rpg.location.RegionMap;
import io.github.austinhoover.backend.rpg.model.DefaultGameData;
import io.github.austinhoover.backend.rpg.organization.OrganizationMap;
import io.github.austinhoover.backend.rpg.politics.PoliticalStateGenerator;
import io.github.austinhoover.backend.rpg.politics.PoliticalStateMap;
import io.github.austinhoover.backend.rpg.race.RaceMap;

/**
 * Represents the complete game world state
 */
public class World implements Serializable {
    private static final long serialVersionUID = 1L;

    private final RegionMap regionMap;
    private final LocationMap locationMap;
    private final CharacterMap characterMap;
    private final OrganizationMap organizationMap;
    private final RaceMap raceMap;
    private final PoliticalStateMap politicalStateMap;

    /**
     * Creates a new world with empty maps
     */
    public World() {
        this.regionMap = new RegionMap();
        this.locationMap = new LocationMap();
        this.characterMap = new CharacterMap();
        this.organizationMap = new OrganizationMap();
        this.raceMap = new RaceMap();
        this.politicalStateMap = new PoliticalStateMap();
    }

    /**
     * Creates a new world with political states generated from default game data
     * @param defaultData The default game data to use for generation
     */
    public World(DefaultGameData defaultData) {
        this.regionMap = new RegionMap();
        this.locationMap = new LocationMap();
        this.characterMap = new CharacterMap();
        this.organizationMap = new OrganizationMap();
        this.raceMap = new RaceMap();
        this.politicalStateMap = new PoliticalStateMap();

        // Generate political states based on the races in default data
        PoliticalStateGenerator.generateStates(this.politicalStateMap, defaultData.getRaces());
    }

    public static World loadWorld(String filePath) {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
            Gson gson = new Gson();
            return gson.fromJson(jsonContent, World.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load world from file: " + filePath, e);
        }
    }

    /**
     * Gets the region map
     * @return The region map
     */    
    public RegionMap getRegionMap() {
        return regionMap;
    }

    /**
     * Gets the location map
     * @return The location map
     */
    public LocationMap getLocationMap() {
        return locationMap;
    }

    /**
     * Gets the character map
     * @return The character map
     */
    public CharacterMap getCharacterMap() {
        return characterMap;
    }

    /**
     * Gets the organization map
     * @return The organization map
     */
    public OrganizationMap getOrganizationMap() {
        return organizationMap;
    }

    /**
     * Gets the race map
     * @return The race map
     */
    public RaceMap getRaceMap() {
        return raceMap;
    }

    /**
     * Gets the political state map
     * @return The political state map
     */
    public PoliticalStateMap getPoliticalStateMap() {
        return politicalStateMap;
    }
} 