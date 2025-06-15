package io.github.austinhoover.rpg.game.organization;

import java.util.HashSet;
import java.util.Set;

import io.github.austinhoover.rpg.game.character.Character;

/**
 * Represents an organization in the world (e.g., guild, faction)
 */
public class Organization {
    
    /**
     * Id of the organization
     */
    private long id;

    /**
     * Name of the organization
     */
    private String name;

    /**
     * Set of character IDs that are members of this organization
     */
    private Set<Long> memberIds;

    /**
     * Constructor
     * @param name Name of the organization
     */
    private Organization(String name) {
        this.name = name;
        this.memberIds = new HashSet<>();
    }

    /**
     * Creates an organization
     * @param map The organization map
     * @param name Name of the organization
     * @return The organization
     */
    public static Organization create(OrganizationMap map, String name) {
        Organization organization = new Organization(name);
        map.register(organization);
        return organization;
    }

    /**
     * Sets the id of this organization
     * @param id The id
     */
    protected void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the id of this organization
     * @return The id
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the name of the organization
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the set of member IDs
     * @return Set of member IDs
     */
    public Set<Long> getMemberIds() {
        return memberIds;
    }

    /**
     * Adds a character to the organization
     * @param character The character to add
     */
    public void addMember(Character character) {
        memberIds.add(character.getId());
    }

    /**
     * Removes a character from the organization
     * @param character The character to remove
     */
    public void removeMember(Character character) {
        memberIds.remove(character.getId());
    }

    /**
     * Checks if a character is a member of this organization
     * @param character The character to check
     * @return true if the character is a member
     */
    public boolean isMember(Character character) {
        return memberIds.contains(character.getId());
    }
} 