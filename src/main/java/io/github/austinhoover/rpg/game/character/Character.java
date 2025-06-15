package io.github.austinhoover.rpg.game.character;

/**
 * Represents a character in the world
 */
public class Character {
    
    /**
     * Id of the character
     */
    private long id;

    /**
     * Name of the character
     */
    private String name;

    /**
     * Role of the character (e.g., "bartender", "police", "painter")
     */
    private String role;

    /**
     * Current location id of the character
     */
    private long currentLocationId;

    /**
     * Constructor
     * @param name Name of the character
     * @param role Role of the character
     * @param currentLocationId The current location id
     */
    private Character(String name, String role, long currentLocationId) {
        this.name = name;
        this.role = role;
        this.currentLocationId = currentLocationId;
    }

    /**
     * Creates a character
     * @param map The character map
     * @param name Name of the character
     * @param role Role of the character
     * @param currentLocationId The current location id
     * @return The character
     */
    public static Character create(CharacterMap map, String name, String role, long currentLocationId) {
        Character character = new Character(name, role, currentLocationId);
        map.register(character);
        return character;
    }

    /**
     * Sets the id of this character
     * @param id The id
     */
    protected void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the id of this character
     * @return The id
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the name of the character
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the role of the character
     * @return The role
     */
    public String getRole() {
        return role;
    }

    /**
     * Gets the current location id
     * @return The current location id
     */
    public long getCurrentLocationId() {
        return currentLocationId;
    }

    /**
     * Sets the current location id
     * @param currentLocationId The new location id
     */
    public void setCurrentLocationId(long currentLocationId) {
        this.currentLocationId = currentLocationId;
    }
} 