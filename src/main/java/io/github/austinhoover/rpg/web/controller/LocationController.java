package io.github.austinhoover.rpg.web.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import io.github.austinhoover.rpg.game.Global;
import io.github.austinhoover.rpg.game.location.Location;
import io.github.austinhoover.rpg.web.dto.LocationDTO;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class LocationController {
    
    @GetMapping("/location")
    public LocationDTO getCurrentLocation() {
        Location currentLocation = Global.world.getLocationMap().getLocationById(Global.player.currentLocationId);
        if (currentLocation == null) {
            throw new RuntimeException("Current location not found");
        }
        return new LocationDTO(currentLocation);
    }

    @GetMapping("/location/{id}")
    public ResponseEntity<LocationDTO> getLocation(@PathVariable long id) {
        Location location = Global.world.getLocationMap().getLocationById(id);
        if (location == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new LocationDTO(location));
    }
} 