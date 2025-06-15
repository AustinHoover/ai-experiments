package io.github.austinhoover.rpg.game.race;

/**
 * Represents a race in the world (e.g., human, elf, dwarf)
 */
public class Race {
    
    /**
     * Id of the race
     */
    private long id;

    /**
     * Name of the race
     */
    private String name;

    /**
     * Constructor
     * @param name Name of the race
     */
    private Race(String name) {
        this.name = name;
    }

    /**
     * Creates a race
     * @param map The race map
     * @param name Name of the race
     * @return The race
     */
    public static Race create(RaceMap map, String name) {
        Race race = new Race(name);
        map.register(race);
        return race;
    }

    /**
     * Sets the id of this race
     * @param id The id
     */
    protected void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the id of this race
     * @return The id
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the name of the race
     * @return The name
     */
    public String getName() {
        return name;
    }
} 