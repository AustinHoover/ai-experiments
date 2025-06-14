package io.github.austinhoover.rpg.location;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Manages all regions in the game world
 */
public class RegionMap {
    private final Map<Long, Region> regions;

    public RegionMap() {
        this.regions = new HashMap<>();
    }

    /**
     * Creates a new region and adds it to the map
     * @param type The type of region
     * @param name Optional name for the region
     * @return The created region
     */
    public Region createRegion(String type, Optional<String> name) {
        Region region = new Region(this.regions.size(), type, name);
        regions.put(region.getId(), region);
        return region;
    }

    /**
     * Gets a region by its ID
     * @param id The ID of the region to find
     * @return Optional containing the region if found
     */
    public Optional<Region> getRegionById(long id) {
        return Optional.ofNullable(regions.get(id));
    }

    /**
     * Gets all regions in the map
     * @return Set of all regions
     */
    public Set<Region> getAllRegions() {
        return Set.copyOf(regions.values());
    }

    /**
     * Removes a region from the map
     * @param region The region to remove
     */
    public void removeRegion(Region region) {
        regions.remove(region.getId());
        // Remove from parent if it exists
        region.getParentRegion().ifPresent(parent -> parent.removeSubregion(region));
    }

    /**
     * Finds the region containing a specific location
     * @param location The location to find
     * @return Optional containing the region if found
     */
    public Optional<Region> findRegionContainingLocation(Location location) {
        return regions.values().stream()
            .filter(region -> region.containsLocation(location))
            .findFirst();
    }

    /**
     * Gets all root regions (regions without parents)
     * @return Set of root regions
     */
    public Set<Region> getRootRegions() {
        return regions.values().stream()
            .filter(region -> region.getParentRegion().isEmpty())
            .collect(java.util.stream.Collectors.toSet());
    }
} 