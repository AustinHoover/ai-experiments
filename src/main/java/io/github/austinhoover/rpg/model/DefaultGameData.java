package io.github.austinhoover.rpg.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.google.gson.Gson;

/**
 * Represents default game data used for world generation
 */
public class DefaultGameData {
    
    /**
     * List of default race names
     */
    private List<String> races;

    /**
     * Constructor
     * @param races List of default race names
     */
    public DefaultGameData(List<String> races) {
        this.races = races;
    }

    /**
     * Gets the list of default race names
     * @return List of race names
     */
    public List<String> getRaces() {
        return races;
    }

    /**
     * Loads and deserializes the default game data from JSON
     * @param filePath Path to the defaultData.json file
     * @return The deserialized DefaultGameData object
     * @throws RuntimeException if the file cannot be read or deserialized
     */
    public static DefaultGameData loadFromFile(String filePath) {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
            Gson gson = new Gson();
            return gson.fromJson(jsonContent, DefaultGameData.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load default game data from file: " + filePath, e);
        }
    }
} 