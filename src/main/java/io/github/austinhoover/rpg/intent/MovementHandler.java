package io.github.austinhoover.rpg.intent;

import java.util.List;

import io.github.austinhoover.kobold.Kobold;
import io.github.austinhoover.rpg.location.Location;
import io.github.austinhoover.rpg.location.LocationMap;
import io.github.austinhoover.rpg.player.PlayerState;

public class MovementHandler {
    private LocationMap graph;
    private PlayerState player;
    private Kobold kobold;
    private ConversationHandler conversationHandler;

    public MovementHandler(LocationMap graph, PlayerState player, Kobold kobold, ConversationHandler conversationHandler) {
        this.graph = graph;
        this.player = player;
        this.kobold = kobold;
        this.conversationHandler = conversationHandler;
    }

    public boolean attemptMove(String targetName) {
        Location current = graph.getLocationById(player.currentLocationId);
        if (current == null) {
            System.out.println("Error: Invalid current location.");
            return false;
        }

        Location neighbor = current.findConnectionTo(graph, targetName);
        if (neighbor == null) {
            System.out.println("Error: Failed to find neighbor location " + targetName);
            return false;
        }

        // If this is the first time visiting this location, generate its description
        if (!neighbor.isDiscovered()) {
            String descriptionPrompt = MovementHandler.generateLocationDetailPrompt(neighbor.getType());
            String description = kobold.request(descriptionPrompt);
            neighbor.setDescription(description);
            neighbor.setDiscovered(true);
        }

        player.currentLocationId = neighbor.getId();
        player.lastAction = "move";
        System.out.println("You travel to " + neighbor.getType() + ".");
        
        // Reset conversation when moving to a new location
        conversationHandler.resetConversation();
        
        return true;
    }

    public void describeCurrentLocation(){
        Location loc = graph.getLocationById(player.currentLocationId);
        if (loc == null) {
            System.out.println("Unknown location.");
            return;
        }
        System.out.println("=== " + loc.generateFallbackLabel() + " ===");
        System.out.println(loc.getDescription());
        System.out.println("Exits:");
        for(Location neighbor : loc.getNeighbors(graph)) {
            System.out.println(" - " + neighbor.generateFallbackLabel());
        }
    }

    /**
     * Looks for nearby locations
     * @param kobold The kobold connection
     */
    public void lookForNearbyLocations(Kobold kobold){
        Location existingLocation = this.graph.getLocationById(player.currentLocationId);
        
        // If we've already generated neighbors for this location, just describe what's available
        if (existingLocation.hasGeneratedNeighbors()) {
            System.out.println("You look around and see the following places:");
            for(Location neighbor : existingLocation.getNeighbors(graph)) {
                if (neighbor.isDiscovered()) {
                    System.out.println(" - " + neighbor.generateFallbackLabel() + ": " + neighbor.getDescription());
                } else {
                    System.out.println(" - " + neighbor.generateFallbackLabel() + " (undiscovered)");
                }
            }
            return;
        }

        // Generate new locations only if we haven't before
        String prompt = existingLocation.generateNearbyLocationPrompt(graph);
        String response = kobold.request(prompt);

        List<String> suggestions = Location.parseNearbyLocationResponse(response);

        for(String suggestion : suggestions){
            if(!existingLocation.hasConnectionTo(graph, suggestion)){
                Location newLoc = Location.create(graph, suggestion, "An undiscovered place.");
                existingLocation.addNeighbor(newLoc);
                System.out.println("You notice a new place: \"" + newLoc.getType() + "\" (undiscovered)");
            }
        }
        
        // Mark that we've generated neighbors for this location
        existingLocation.setHasGeneratedNeighbors(true);
    }

    public String lookForNearbyLocationsWithReturn(Kobold kobold){
        Location existingLocation = this.graph.getLocationById(player.currentLocationId);
        StringBuilder sb = new StringBuilder();
        // If we've already generated neighbors for this location, just describe what's available
        if (existingLocation.hasGeneratedNeighbors()) {
            sb.append("You look around and see the following places:\n");
            for(Location neighbor : existingLocation.getNeighbors(graph)) {
                if (neighbor.isDiscovered()) {
                    sb.append(" - ").append(neighbor.generateFallbackLabel()).append(": ").append(neighbor.getDescription()).append("\n");
                } else {
                    sb.append(" - ").append(neighbor.generateFallbackLabel()).append(" (undiscovered)\n");
                }
            }
            System.out.print(sb.toString());
            return sb.toString().trim();
        }
        // Generate new locations only if we haven't before
        String prompt = existingLocation.generateNearbyLocationPrompt(graph);
        String response = kobold.request(prompt);
        List<String> suggestions = Location.parseNearbyLocationResponse(response);
        for(String suggestion : suggestions){
            if(!existingLocation.hasConnectionTo(graph, suggestion)){
                Location newLoc = Location.create(graph, suggestion, "An undiscovered place.");
                existingLocation.addNeighbor(newLoc);
                sb.append("You notice a new place: \"").append(newLoc.getType()).append("\" (undiscovered)\n");
            }
        }
        // Mark that we've generated neighbors for this location
        existingLocation.setHasGeneratedNeighbors(true);
        System.out.print(sb.toString());
        return sb.toString().trim();
    }

    public static String generateLocationDetailPrompt(String newLocationType){
        return "There is a location described as: '" + newLocationType + "'. " +
           "\nPlease write a short, immersive paragraph describing the new location. " +
           "\nDo not describe the surroundings of the location, just describe the location itself. " +
           "Do not include a name unless it's natural to do so in the prose.";
    }
}
