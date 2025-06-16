package io.github.austinhoover.rpg.web.dto;

import io.github.austinhoover.rpg.game.character.Character;

public class CharacterDTO {
    private long id;
    private String name;
    private String role;
    private long currentLocationId;

    public CharacterDTO(Character character) {
        this.id = character.getId();
        this.name = character.getName();
        this.role = character.getRole();
        this.currentLocationId = character.getCurrentLocationId();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public long getCurrentLocationId() {
        return currentLocationId;
    }
} 