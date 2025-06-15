package io.github.austinhoover.backend.rpg.politics;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Storage object for all political states
 */
public class PoliticalStateMap {
    
    /**
     * Map of id -> political state
     */
    private Map<Long, PoliticalState> idStateMap = new HashMap<Long, PoliticalState>();

    /**
     * Gets a political state by its id
     * @param id The id
     * @return The political state
     */
    public PoliticalState getStateById(long id) {
        return idStateMap.get(id);
    }

    /**
     * Registers a political state
     * @param state The political state
     */
    public void register(PoliticalState state) {
        state.setId(idStateMap.size());
        idStateMap.put(state.getId(), state);
    }

    /**
     * Gets all political states
     * @return Collection of all political states
     */
    public Collection<PoliticalState> getAllStates() {
        return idStateMap.values();
    }
} 