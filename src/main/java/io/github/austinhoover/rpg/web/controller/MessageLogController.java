package io.github.austinhoover.rpg.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.austinhoover.rpg.game.Global;

@RestController
public class MessageLogController {
    
    @GetMapping("/messages")
    public List<String> getLatestMessages() {
        return Global.gameLog.getRecentEntries().stream()
            .flatMap(entry -> List.of(
                "> " + entry.getPlayerInput(),
                entry.getStoryResponse()
            ).stream())
            .collect(Collectors.toList());
    }
} 