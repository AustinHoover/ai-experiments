package io.github.austinhoover.rpg.game.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Manages units in the game
 */
public class UnitMap {
    private final Map<Long, Unit> units;
    private long nextId;

    public UnitMap() {
        this.units = new HashMap<>();
        this.nextId = 1;
    }

    /**
     * Registers a unit with this map
     * @param unit The unit to register
     */
    protected void register(Unit unit) {
        unit.setId(nextId++);
        units.put(unit.getId(), unit);
    }

    /**
     * Gets a unit by its id
     * @param id The id of the unit
     * @return Optional containing the unit if found
     */
    public Optional<Unit> getUnitById(long id) {
        return Optional.ofNullable(units.get(id));
    }

    /**
     * Gets all units in this map
     * @return Map of all units
     */
    public Map<Long, Unit> getAllUnits() {
        return new HashMap<>(units);
    }
} 