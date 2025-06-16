package io.github.austinhoover.rpg.game.name;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.github.austinhoover.rpg.game.Global;

/**
 * Service for caching and retrieving generated names
 */
public class NameCacheService {
    private static final int NAMES_TO_GENERATE = 50;
    private static final String CACHE_DIR = ".cache";
    private static final String CACHE_FILE = "name_cache.json";
    private final Map<String, List<String>> nameCache;
    private final Random random;
    private final Gson gson;

    public NameCacheService(Random random) {
        this.random = random;
        this.gson = new Gson();
        this.nameCache = loadCache();
    }

    /**
     * Gets a name for the given query. If the query hasn't been cached,
     * generates new names using the Kobold service.
     * 
     * @param query The query string to get a name for
     * @return A randomly selected name from the cache
     */
    public String getName(String query) {
        // Check if we have cached names for this query
        List<String> names = nameCache.get(query);
        
        // If no cached names exist, generate new ones
        if (names == null || names.isEmpty()) {
            names = generateNewNames(query);
            nameCache.put(query, names);
            saveCache(); // Save to disk when new names are generated
        }
        
        // Return a random name from the list
        return names.get(random.nextInt(names.size()));
    }

    /**
     * Generates new names using the Kobold service
     * 
     * @param query The query string to generate names for
     * @return A list of generated names
     */
    private List<String> generateNewNames(String query) {
        List<String> names = new ArrayList<>();
        try {
            while(names.size() < NAMES_TO_GENERATE) {
                String response = Global.kobold.request(query).trim();
                String generatedName = response.replaceAll("\"", "");
                if(!names.contains(generatedName)) {
                    names.add(generatedName);
                }
            }
        } catch (Exception e) {
            System.err.println("Error generating names: " + e.getMessage());
            // Add some fallback names if generation fails
            names.add("Unknown");
            names.add("Mysterious");
            names.add("Nameless");
        }

        return names;
    }

    /**
     * Loads the name cache from disk
     * @return The loaded cache, or an empty map if no cache exists
     */
    private Map<String, List<String>> loadCache() {
        try {
            File cacheFile = new File(CACHE_DIR, CACHE_FILE);
            if (!cacheFile.exists()) {
                return new HashMap<>();
            }

            String json = new String(Files.readAllBytes(cacheFile.toPath()));
            return gson.fromJson(json, new TypeToken<Map<String, List<String>>>(){}.getType());
        } catch (IOException e) {
            System.err.println("Error loading name cache: " + e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * Saves the name cache to disk
     */
    private void saveCache() {
        try {
            // Create cache directory if it doesn't exist
            File cacheDir = new File(CACHE_DIR);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }

            // Write cache to file
            File cacheFile = new File(cacheDir, CACHE_FILE);
            String json = gson.toJson(nameCache);
            try (FileWriter writer = new FileWriter(cacheFile)) {
                writer.write(json);
            }
        } catch (IOException e) {
            System.err.println("Error saving name cache: " + e.getMessage());
        }
    }
} 