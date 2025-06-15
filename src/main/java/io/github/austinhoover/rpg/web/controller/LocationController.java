package io.github.austinhoover.rpg.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.austinhoover.rpg.game.Global;
import io.github.austinhoover.rpg.game.location.Location;

@RestController
public class LocationController {
    
    @GetMapping("/location")
    public Location getCurrentLocation() {
        Location currentLocation = Global.world.getLocationMap().getLocationById(Global.player.currentLocationId);
        if (currentLocation == null) {
            throw new RuntimeException("Current location not found");
        }
        return currentLocation;
    }
} 