package io.github.austinhoover.rpg.game.location;

import java.util.HashSet;
import java.util.Set;
import java.util.Optional;

import io.github.austinhoover.rpg.kobold.Kobold;

/**
 * Represents a region that can contain locations and nested regions
 */
public class Region {
    private final long id;
    private final String type;
    private String name;
    private Set<Long> locationIds;
    private Set<Long> subregionIds;
    private Long parentRegionId;

    protected Region(long id, String type, Optional<String> name) {
        this.id = id;
        this.type = type;
        this.name = name.orElse(null);
        this.locationIds = new HashSet<>();
        this.subregionIds = new HashSet<>();
        this.parentRegionId = null;
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
        return Optional.ofNullable(name);
    }

    public void setName(Optional<String> name) {
        this.name = name.orElse(null);
    }

    public Set<Long> getLocationIds() {
        return new HashSet<>(locationIds);
    }

    public Set<Long> getSubregionIds() {
        return new HashSet<>(subregionIds);
    }

    public Optional<Long> getParentRegionId() {
        return Optional.ofNullable(parentRegionId);
    }

    /**
     * Adds a location to this region
     * @param location The location to add
     */
    public void addLocation(Location location) {
        if(locationIds == null){
            locationIds = new HashSet<>();
        }
        locationIds.add(location.getId());
    }

    /**
     * Removes a location from this region
     * @param location The location to remove
     */
    public void removeLocation(Location location) {
        locationIds.remove(location.getId());
    }

    /**
     * Adds a subregion to this region
     * @param subregion The subregion to add
     */
    public void addSubregion(Region subregion) {
        if (subregion.parentRegionId != null) {
            // Note: This will need to be handled by the RegionMap to properly update the parent region
            subregion.parentRegionId = null;
        }
        subregion.parentRegionId = this.id;
        subregionIds.add(subregion.getId());
    }

    /**
     * Removes a subregion from this region
     * @param subregion The subregion to remove
     */
    public void removeSubregion(Region subregion) {
        if (subregionIds.remove(subregion.getId())) {
            subregion.parentRegionId = null;
        }
    }

    /**
     * Checks if this region contains a specific location
     * @param location The location to check for
     * @return true if the location is in this region or any subregion
     */
    public boolean containsLocation(Location location) {
        if (locationIds.contains(location.getId())) {
            return true;
        }
        // Note: This will need to be handled by the RegionMap to properly check subregions
        return false;
    }

    /**
     * Gets all location IDs in this region and its subregions
     * @return Set of all location IDs in this region and subregions
     */
    public Set<Long> getAllLocationIds() {
        Set<Long> allLocationIds = new HashSet<>(locationIds);
        // Note: This will need to be handled by the RegionMap to properly get subregion locations
        return allLocationIds;
    }
}