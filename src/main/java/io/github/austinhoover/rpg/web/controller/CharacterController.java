package io.github.austinhoover.rpg.web.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.stream.Collectors;

import io.github.austinhoover.rpg.game.Global;
import io.github.austinhoover.rpg.game.character.Character;
import io.github.austinhoover.rpg.web.dto.CharacterDTO;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class CharacterController {
    
    @GetMapping("/character/{id}")
    public ResponseEntity<CharacterDTO> getCharacter(@PathVariable long id) {
        Character character = Global.world.getCharacterMap().getCharacterById(id);
        if (character == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new CharacterDTO(character));
    }

    @GetMapping("/location/{locationId}/characters")
    public ResponseEntity<List<CharacterDTO>> getCharactersAtLocation(@PathVariable long locationId) {
        // Get all characters and filter by location
        List<CharacterDTO> characters = Global.world.getCharacterMap().getAllCharacters().stream()
            .filter(character -> character.getCurrentLocationId() == locationId)
            .map(CharacterDTO::new)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(characters);
    }
} 