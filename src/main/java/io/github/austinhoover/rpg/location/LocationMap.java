package io.github.austinhoover.rpg.location;

import java.util.HashMap;
import java.util.Map;

/**
 * Storage object for all locations
 */
public class LocationMap {
    
    /**
     * Map of id -> location
     */
    private Map<Long,Location> idLocMap = new HashMap<Long,Location>();

    /**
     * Gets a location by its id
     * @param id The id
     * @return The location
     */
    public Location getLocationById(long id){
        return idLocMap.get(id);
    }

    /**
     * Registers a location
     * @param loc The location
     */
    public void register(Location loc){
        loc.setId(idLocMap.size());
        idLocMap.put(loc.getId(),loc);
    }

}
