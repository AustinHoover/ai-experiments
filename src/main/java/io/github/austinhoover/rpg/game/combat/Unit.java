package io.github.austinhoover.rpg.game.combat;

import java.util.Optional;

import io.github.austinhoover.rpg.game.character.Character;

/**
 * Represents a unit in turn-based combat
 */
public class Unit {
    
    /**
     * Unique identifier for this unit
     */
    private long id;

    /**
     * Name of the unit
     */
    private String name;

    /**
     * Optional character associated with this unit
     */
    private Optional<Character> character;

    /**
     * Current health of the unit
     */
    private int currentHealth;

    /**
     * Maximum health of the unit
     */
    private int maxHealth;

    /**
     * Speed stat of the unit, determines turn order
     */
    private int speed;

    /**
     * Constructor
     * @param name Name of the unit
     * @param character Optional character associated with this unit
     * @param maxHealth Maximum health of the unit
     * @param speed Speed stat of the unit
     */
    private Unit(String name, Optional<Character> character, int maxHealth, int speed) {
        this.name = name;
        this.character = character;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.speed = speed;
    }

    /**
     * Creates a new unit
     * @param map The unit map
     * @param name Name of the unit
     * @param character Optional character associated with this unit
     * @param maxHealth Maximum health of the unit
     * @param speed Speed stat of the unit
     * @return The created unit
     */
    public static Unit create(UnitMap map, String name, Optional<Character> character, int maxHealth, int speed) {
        Unit unit = new Unit(name, character, maxHealth, speed);
        map.register(unit);
        return unit;
    }

    /**
     * Sets the id of this unit
     * @param id The id
     */
    protected void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the id of this unit
     * @return The id
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the name of the unit
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the character associated with this unit, if any
     * @return Optional containing the character
     */
    public Optional<Character> getCharacter() {
        return character;
    }

    /**
     * Gets the current health of the unit
     * @return The current health
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Sets the current health of the unit
     * @param health The new health value
     */
    public void setCurrentHealth(int health) {
        this.currentHealth = Math.min(Math.max(0, health), maxHealth);
    }

    /**
     * Gets the maximum health of the unit
     * @return The maximum health
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Gets the speed stat of the unit
     * @return The speed stat
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Sets the speed stat of the unit
     * @param speed The new speed value
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Checks if the unit is alive
     * @return true if the unit has health greater than 0
     */
    public boolean isAlive() {
        return currentHealth > 0;
    }

    /**
     * Takes damage
     * @param amount Amount of damage to take
     * @return The new current health
     */
    public int takeDamage(int amount) {
        setCurrentHealth(currentHealth - amount);
        return currentHealth;
    }

    /**
     * Heals the unit
     * @param amount Amount to heal
     * @return The new current health
     */
    public int heal(int amount) {
        setCurrentHealth(currentHealth + amount);
        return currentHealth;
    }
} 