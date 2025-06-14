package io.github.austinhoover.rpg.location;

import java.util.HashSet;
import java.util.Set;
import java.util.Optional;

import io.github.austinhoover.kobold.Kobold;

/**
 * Represents a region that can contain locations and nested regions
 */
public class Region {
    private final long id;
    private final String type;
    private Optional<String> name;
    private final Set<Location> locations;
    private final Set<Region> subregions;
    private Region parentRegion;

    protected Region(long id, String type, Optional<String> name) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.locations = new HashSet<>();
        this.subregions = new HashSet<>();
        this.parentRegion = null;
    }

    /**
     * Creates a new region with the given type and optional name
     * @param type The type of region
     * @param name Optional name for the region
     * @return A new Region instance
     */
    public static Region create(RegionMap map, String type, Optional<String> name) {
        return map.createRegion(type, name);
    }

    /**
     * Generates a region type based on a user-provided summary
     * @param kobold The Kobold instance to use for LLM requests
     * @param summary The user-provided summary of the region
     * @return The generated region type
     */
    public static String generateRegionType(Kobold kobold, String summary) {
        String prompt = "You are the narrator of a text adventure game.\n" +
                       "Based on the following description, what type of region would this be?\n" +
                       "Description: " + summary + "\n\n" +
                       "Examples of region types: city, forest, desert, mountain range, coastal area, etc.\n" +
                       "Respond with ONLY the region type, nothing else. Keep it to 1-3 words.";
        
        String response = kobold.request(prompt).trim();
        
        // Clean up the response to ensure we get just the region type
        return response.replaceAll("(?i)^(a|an|the)\\s+", "")  // Remove leading articles
                      .replaceAll("\\s*\\([^)]*\\)", "")       // Remove parenthetical notes
                      .replaceAll("\\s*\\.$", "")              // Remove trailing period
                      .replaceAll("\\s+", " ")                 // Normalize whitespace
                      .toLowerCase();                          // Convert to lowercase
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Optional<String> getName() {
        return name;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public Set<Location> getLocations() {
        return new HashSet<>(locations);
    }

    public Set<Region> getSubregions() {
        return new HashSet<>(subregions);
    }

    public Optional<Region> getParentRegion() {
        return Optional.ofNullable(parentRegion);
    }

    /**
     * Adds a location to this region
     * @param location The location to add
     */
    public void addLocation(Location location) {
        locations.add(location);
    }

    /**
     * Removes a location from this region
     * @param location The location to remove
     */
    public void removeLocation(Location location) {
        locations.remove(location);
    }

    /**
     * Adds a subregion to this region
     * @param subregion The subregion to add
     */
    public void addSubregion(Region subregion) {
        if (subregion.parentRegion != null) {
            subregion.parentRegion.removeSubregion(subregion);
        }
        subregion.parentRegion = this;
        subregions.add(subregion);
    }

    /**
     * Removes a subregion from this region
     * @param subregion The subregion to remove
     */
    public void removeSubregion(Region subregion) {
        if (subregions.remove(subregion)) {
            subregion.parentRegion = null;
        }
    }

    /**
     * Checks if this region contains a specific location
     * @param location The location to check for
     * @return true if the location is in this region or any subregion
     */
    public boolean containsLocation(Location location) {
        if (locations.contains(location)) {
            return true;
        }
        return subregions.stream().anyMatch(region -> region.containsLocation(location));
    }

    /**
     * Gets all locations in this region and its subregions
     * @return Set of all locations in this region and subregions
     */
    public Set<Location> getAllLocations() {
        Set<Location> allLocations = new HashSet<>(locations);
        subregions.forEach(region -> allLocations.addAll(region.getAllLocations()));
        return allLocations;
    }
} 