package io.github.austinhoover.rpg.game.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Represents a combat encounter between allies and enemies
 */
public class Combat {

    /**
     * List of allied units in the combat
     */
    private List<Unit> allies;

    /**
     * List of enemy units in the combat
     */
    private List<Unit> enemies;

    /**
     * Map of units to their current speed scores
     */
    private Map<Unit, Integer> speedScores;

    /**
     * Constructor
     */
    private Combat() {
        this.allies = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.speedScores = new HashMap<>();
    }

    /**
     * Creates a new combat
     * @return The created combat
     */
    public static Combat create() {
        return new Combat();
    }

    /**
     * Gets the list of allied units
     * @return List of allied units
     */
    public List<Unit> getAllies() {
        return new ArrayList<>(allies);
    }

    /**
     * Gets the list of enemy units
     * @return List of enemy units
     */
    public List<Unit> getEnemies() {
        return new ArrayList<>(enemies);
    }

    /**
     * Adds an ally to the combat
     * @param unit The unit to add
     */
    public void addAlly(Unit unit) {
        if (!allies.contains(unit)) {
            allies.add(unit);
            initializeSpeedScore(unit);
        }
    }

    /**
     * Adds an enemy to the combat
     * @param unit The unit to add
     */
    public void addEnemy(Unit unit) {
        if (!enemies.contains(unit)) {
            enemies.add(unit);
            initializeSpeedScore(unit);
        }
    }

    /**
     * Removes an ally from the combat
     * @param unit The unit to remove
     */
    public void removeAlly(Unit unit) {
        allies.remove(unit);
        speedScores.remove(unit);
    }

    /**
     * Removes an enemy from the combat
     * @param unit The unit to remove
     */
    public void removeEnemy(Unit unit) {
        enemies.remove(unit);
        speedScores.remove(unit);
    }

    /**
     * Initializes a unit's speed score with its base speed
     * @param unit The unit to initialize
     */
    private void initializeSpeedScore(Unit unit) {
        speedScores.put(unit, unit.getSpeed());
    }

    /**
     * Gets the next unit to act in combat
     * @return Optional containing the next unit to act, or empty if no units are alive
     */
    public Optional<Unit> getNextUnit() {
        return speedScores.entrySet().stream()
            .filter(entry -> entry.getKey().isAlive())
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey);
    }

    /**
     * Advances a unit's turn by adding its speed to its score
     * @param unit The unit that just acted
     */
    public void advanceUnitTurn(Unit unit) {
        if (speedScores.containsKey(unit)) {
            int currentScore = speedScores.get(unit);
            speedScores.put(unit, currentScore + unit.getSpeed());
        }
    }

    /**
     * Checks if the combat is over
     * @return true if either all allies or all enemies are defeated
     */
    public boolean isOver() {
        boolean allAlliesDefeated = allies.stream().noneMatch(Unit::isAlive);
        boolean allEnemiesDefeated = enemies.stream().noneMatch(Unit::isAlive);
        return allAlliesDefeated || allEnemiesDefeated;
    }

    /**
     * Gets the winner of the combat
     * @return Optional containing the winning side ("allies" or "enemies"), or empty if combat is not over
     */
    public Optional<String> getWinner() {
        if (!isOver()) {
            return Optional.empty();
        }

        boolean allAlliesDefeated = allies.stream().noneMatch(Unit::isAlive);
        boolean allEnemiesDefeated = enemies.stream().noneMatch(Unit::isAlive);

        if (allAlliesDefeated && !allEnemiesDefeated) {
            return Optional.of("enemies");
        } else if (!allAlliesDefeated && allEnemiesDefeated) {
            return Optional.of("allies");
        }

        return Optional.empty();
    }
} 