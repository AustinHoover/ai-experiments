package io.github.austinhoover.rpg.web.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import io.github.austinhoover.rpg.game.Global;
import io.github.austinhoover.rpg.game.location.Location;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class LocationController {
    
    @GetMapping("/location")
    public Location getCurrentLocation() {
        Location currentLocation = Global.world.getLocationMap().getLocationById(Global.player.currentLocationId);
        if (currentLocation == null) {
            throw new RuntimeException("Current location not found");
        }
        return currentLocation;
    }

    @GetMapping("/location/{id}")
    public ResponseEntity<Location> getLocation(@PathVariable long id) {
        Location location = Global.world.getLocationMap().getLocationById(id);
        if (location == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(location);
    }
} 