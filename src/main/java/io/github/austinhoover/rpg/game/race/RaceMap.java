package io.github.austinhoover.rpg.game.race;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Storage object for all races
 */
public class RaceMap {
    
    /**
     * Map of id -> race
     */
    private Map<Long, Race> idRaceMap = new HashMap<Long, Race>();

    /**
     * Gets a race by its id
     * @param id The id
     * @return The race
     */
    public Race getRaceById(long id) {
        return idRaceMap.get(id);
    }

    /**
     * Registers a race
     * @param race The race
     */
    public void register(Race race) {
        race.setId(idRaceMap.size());
        idRaceMap.put(race.getId(), race);
    }

    /**
     * Gets all races
     * @return Collection of all races
     */
    public Collection<Race> getAllRaces() {
        return idRaceMap.values();
    }
} 