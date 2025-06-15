package io.github.austinhoover.rpg.web.dto;

import io.github.austinhoover.rpg.game.location.Location;
import java.util.List;

public class LocationDTO {
    private long id;
    private String type;
    private String description;
    private List<Long> neighborIds;

    public LocationDTO(Location location) {
        this.id = location.getId();
        this.type = location.getType();
        this.description = location.getDescription();
        this.neighborIds = location.getNeighborIds();
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public List<Long> getNeighborIds() {
        return neighborIds;
    }
} 