package net.diecode.killermoney.objects;

import org.bukkit.entity.EntityType;

import java.util.ArrayList;

public class EntityProperties {

    private EntityType entityType;
    private ArrayList<WorldProperties> worldProperties = new ArrayList<WorldProperties>();

    public EntityProperties(EntityType entityType) {
        this.entityType = entityType;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public ArrayList<WorldProperties> getWorldProperties() {
        return worldProperties;
    }
}
