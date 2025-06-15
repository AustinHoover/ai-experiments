package io.github.austinhoover.backend.rpg.character;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Storage object for all characters
 */
public class CharacterMap {
    
    /**
     * Map of id -> character
     */
    private Map<Long, Character> idCharMap = new HashMap<Long, Character>();

    /**
     * Gets a character by its id
     * @param id The id
     * @return The character
     */
    public Character getCharacterById(long id) {
        return idCharMap.get(id);
    }

    /**
     * Registers a character
     * @param character The character
     */
    public void register(Character character) {
        character.setId(idCharMap.size());
        idCharMap.put(character.getId(), character);
    }

    /**
     * Gets all characters
     * @return Collection of all characters
     */
    public Collection<Character> getAllCharacters() {
        return idCharMap.values();
    }
} 