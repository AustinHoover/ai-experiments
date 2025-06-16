package io.github.austinhoover.rpg.game.location;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A location
 */
public class Location {
    
    /**
     * Id of the location
     */
    private long id;

    /**
     * Type of the location
     */
    public String type;

    /**
     * Description of the location
     */
    private String description;

    /**
     * The neighbor locations
     */
    private List<Long> neighbors = new LinkedList<Long>();

    /**
     * The parent region's ID
     */
    private long parentRegionId;

    /**
     * Tracks whether neighbors have generated or not
     */
    private boolean hasGeneratedNeighbors = false;

    /**
     * Tracks whether this location has been discovered and described
     */
    private boolean isDiscovered = false;

    /**
     * List of character IDs present in this location
     */
    private List<Long> characterIds = new ArrayList<>();

    /**
     * Sets whether neighbors have been generated for this location
     * @param hasGenerated Whether neighbors have been generated
     */
    public void setHasGeneratedNeighbors(boolean hasGenerated) {
        this.hasGeneratedNeighbors = hasGenerated;
    }

    /**
     * Gets whether neighbors have been generated for this location
     * @return Whether neighbors have been generated
     */
    public boolean hasGeneratedNeighbors() {
        return this.hasGeneratedNeighbors;
    }

    /**
     * Sets whether this location has been discovered
     * @param discovered Whether the location has been discovered
     */
    public void setDiscovered(boolean discovered) {
        this.isDiscovered = discovered;
    }

    /**
     * Gets whether this location has been discovered
     * @return Whether the location has been discovered
     */
    public boolean isDiscovered() {
        return this.isDiscovered;
    }

    /**
     * Constructor
     * @param type Type of the location
     * @param description Description of the location
     * @param parentRegionId ID of the parent region
     */
    private Location(String type, String description, long parentRegionId){
        this.type = type;
        this.description = description;
        this.parentRegionId = parentRegionId;
    }

    /**
     * Creates a location
     * @param map The location map
     * @param type The type of the location
     * @param description The description of the location
     * @param parentRegionId ID of the parent region
     * @return The location
     */
    public static Location create(LocationMap map, String type, String description, long parentRegionId){
        Location rVal = new Location(type, description, parentRegionId);
        map.register(rVal);
        return rVal;
    }

    /**
     * Sets the id of this location
     * @param id The id
     */
    protected void setId(long id){
        this.id = id;
    }

    /**
     * Gets the id of this location
     * @return The id
     */
    public long getId(){
        return id;
    }

    /**
     * Gets the type of the location
     * @return The type of the location
     */
    public String getType() {
        return type;
    }

    /**
     * Adds a neighbor
     * @param neighborId The neighbor's id
     */
    public void addNeighbor(long neighborId){
        this.neighbors.add(neighborId);
    }

    /**
     * Adds a neighbor
     * @param neighbor The neighbor
     */
    public void addNeighbor(Location neighbor){
        if(this.neighbors == null){
            this.neighbors = new ArrayList<>();
        }
        if(!this.neighbors.contains(neighbor.getId())){
            this.neighbors.add(neighbor.getId());
            neighbor.addNeighbor(this);
        }
    }

    /**
     * Gets the neighbors of this location
     * @param graph The graph
     * @return The list of neighbors
     */
    public List<Location> getNeighbors(LocationMap graph){
        if(this.neighbors == null){
            return new ArrayList<>();
        }
        return this.neighbors.stream().map((Long neighborId) -> graph.getLocationById(neighborId)).collect(Collectors.toList());
    }

    public List<Long> getNeighborIds(){
        return this.neighbors;
    }

    /**
     * Gets the description of the location
     * @return The description of the location
     */
    public String getDescription(){
        return description;
    }

    /**
     * Sets the description of the location
     * @param description The new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Finds the connection to a given node
     * @param graph The graph
     * @param targetName The target's name
     * @return The target if it exists, null otherwise
     */
    public Location findConnectionTo(LocationMap graph, String targetName){
        List<Location> neighbors = this.getNeighbors(graph);
        for(Location neighbor : neighbors){
            if(neighbor.generateFallbackLabel().equalsIgnoreCase(targetName)){
                return neighbor;
            }
            if(neighbor.getType().equalsIgnoreCase(targetName)){
                return neighbor;
            }
        }
        return null;
    }

    public boolean hasConnectionTo(LocationMap graph, String targetName) {
        return findConnectionTo(graph, targetName) != null;
    }

    /**
     * Generate a prompt for an llm to generate new locations neighboring this one
     * @param graph The existing location graph
     * @return The prompt
     */
    public String generateNearbyLocationPrompt(LocationMap graph){
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are the narrator of a text adventure game.\n");
        prompt.append("The player is currently in a location.");
        prompt.append("Description: " + this.description + "\n");
        prompt.append("Nearby, the player can already see: \n");
        List<Location> neighbors = this.getNeighbors(graph);
        for (Location neighbor : neighbors) {
            prompt.append(" - " + neighbor.generateFallbackLabel() + "\n");
        }
        prompt.append("Please list 1-2 plausible new nearby location types the player might discover. ");
        prompt.append("Examples of location types: alley, warehouse, tavern, cellar, courtyard, etc. ");
        prompt.append("Output ONLY the types, comma-separated. Do not repeat known ones.");
        return prompt.toString();
    }

    public static List<String> parseNearbyLocationResponse(String response){
        List<String> results = new ArrayList<>();
        
        // Split by newlines first to handle numbered lists
        String[] lines = response.split("\n");
        for (String line : lines) {
            // Remove any leading numbers, dashes, dots/spaces
            String cleaned = line.replaceAll("^\\s*(\\d+[.)]?|[-])\\s*", "").trim();
            if (!cleaned.isEmpty()) {
                // Split by commas in case there are multiple items on one line
                String[] commaParts = cleaned.split(",");
                for (String part : commaParts) {
                    String finalPart = part.trim()
                        .replace("\"", "")
                        .replace(".", "")
                        // Remove articles from the beginning (case insensitive)
                        .replaceAll("(?i)^\\s*(a|an|the)\\s+", "")
                        .replaceAll("\\s*\\([a-zA-z\\s]*\\)", "")
                        .toLowerCase();
                    if (!finalPart.isEmpty()) {
                        results.add(finalPart);
                    }
                }
            }
        }
        return results;
    }

    public String generateFallbackLabel() {
        if (this.type != null) return "A " + this.type;
        return "An unknown place";
    }

    /**
     * Gets the parent region ID
     * @return The parent region ID
     */
    public long getParentRegionId() {
        return parentRegionId;
    }

    /**
     * Sets the parent region ID
     * @param parentRegionId The new parent region ID
     */
    public void setParentRegionId(long parentRegionId) {
        this.parentRegionId = parentRegionId;
    }

    /**
     * Gets the list of character IDs in this location
     * @return List of character IDs
     */
    public List<Long> getCharacterIds() {
        return characterIds;
    }

    /**
     * Sets the list of character IDs in this location
     * @param characterIds List of character IDs
     */
    public void setCharacterIds(List<Long> characterIds) {
        this.characterIds = characterIds;
    }

}
