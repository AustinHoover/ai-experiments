package io.github.austinhoover.backend.rpg.location;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Manages all regions in the game world
 */
public class RegionMap {
    private final Map<Long, Region> regions;
    private Region topLevelRegion;

    public RegionMap() {
        this.regions = new HashMap<>();
        this.topLevelRegion = null;
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
        
        // If this is the first region, set it as the top level region
        if (topLevelRegion == null) {
            topLevelRegion = region;
        }
        
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
        region.getParentRegionId().ifPresent(parentId -> {
            Region parent = regions.get(parentId);
            if (parent != null) {
                parent.removeSubregion(region);
            }
        });
        
        // If we're removing the top level region, update it
        if (region.equals(topLevelRegion)) {
            // Find a new top level region (first region without a parent)
            topLevelRegion = regions.values().stream()
                .filter(r -> r.getParentRegionId().isEmpty())
                .findFirst()
                .orElse(null);
        }
    }

    /**
     * Finds the region containing a specific location
     * @param location The location to find
     * @return Optional containing the region if found
     */
    public Optional<Region> findRegionForLocation(Location location) {
        return regions.values().stream()
            .filter(region -> region.getLocationIds().contains(location.getId()))
            .findFirst();
    }

    /**
     * Gets the top level region of the world
     * @return The top level region, or null if no regions exist
     */
    public Region getTopLevelRegion() {
        return topLevelRegion;
    }
} 