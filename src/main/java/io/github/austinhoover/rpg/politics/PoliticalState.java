package io.github.austinhoover.rpg.politics;

/**
 * Represents a political state in the world (e.g., kingdom, empire, republic)
 */
public class PoliticalState {
    
    /**
     * Id of the political state
     */
    private long id;

    /**
     * Name of the political state
     */
    private String name;

    /**
     * Race that primarily inhabits this state
     */
    private String race;

    /**
     * Constructor
     * @param name Name of the political state
     * @param race Race that primarily inhabits this state
     */
    private PoliticalState(String name, String race) {
        this.name = name;
        this.race = race;
    }

    /**
     * Creates a political state
     * @param map The political state map
     * @param name Name of the political state
     * @param race Race that primarily inhabits this state
     * @return The political state
     */
    public static PoliticalState create(PoliticalStateMap map, String name, String race) {
        PoliticalState state = new PoliticalState(name, race);
        map.register(state);
        return state;
    }

    /**
     * Sets the id of this political state
     * @param id The id
     */
    protected void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the id of this political state
     * @return The id
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the name of the political state
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the race that primarily inhabits this state
     * @return The race
     */
    public String getRace() {
        return race;
    }
} 