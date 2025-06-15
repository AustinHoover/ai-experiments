package io.github.austinhoover.rpg.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.austinhoover.rpg.game.sim.Simulation;

@RestController
public class SimulationController {
    
    @PostMapping("/simulate")
    public String simulate(@RequestBody String input) {
        return Simulation.simulate(input);
    }
} 