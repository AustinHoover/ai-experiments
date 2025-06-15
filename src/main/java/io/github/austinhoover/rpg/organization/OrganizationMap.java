package io.github.austinhoover.rpg.organization;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Storage object for all organizations
 */
public class OrganizationMap {
    
    /**
     * Map of id -> organization
     */
    private Map<Long, Organization> idOrgMap = new HashMap<Long, Organization>();

    /**
     * Gets an organization by its id
     * @param id The id
     * @return The organization
     */
    public Organization getOrganizationById(long id) {
        return idOrgMap.get(id);
    }

    /**
     * Registers an organization
     * @param organization The organization
     */
    public void register(Organization organization) {
        organization.setId(idOrgMap.size());
        idOrgMap.put(organization.getId(), organization);
    }

    /**
     * Gets all organizations
     * @return Collection of all organizations
     */
    public Collection<Organization> getAllOrganizations() {
        return idOrgMap.values();
    }
} 