package io.github.austinhoover.rpg.world;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;

import io.github.austinhoover.rpg.character.CharacterMap;
import io.github.austinhoover.rpg.location.LocationMap;
import io.github.austinhoover.rpg.location.RegionMap;

/**
 * Represents the complete game world state
 */
public class World implements Serializable {
    private static final long serialVersionUID = 1L;

    private final RegionMap regionMap;
    private final LocationMap locationMap;
    private final CharacterMap characterMap;

    /**
     * Creates a new world with empty maps
     */
    public World() {
        this.regionMap = new RegionMap();
        this.locationMap = new LocationMap();
        this.characterMap = new CharacterMap();
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

} 