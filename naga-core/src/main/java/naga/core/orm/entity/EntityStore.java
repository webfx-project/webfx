package naga.core.orm.entity;


import java.util.HashMap;
import java.util.Map;

/**
 * A store for entities that are transactionally coherent.
 *
 * @author Bruno Salmon
 */
public class EntityStore {

    private final Map<EntityID, Entity> entities = new HashMap<>();
    private final Map<Object, EntityList> entityLists = new HashMap<>();

    // ID management

    public EntityID getEntityID(Object domainClassId, Object primaryKey) {
        return new EntityID(domainClassId, primaryKey);
    }

    // Entity management

    public Entity getEntity(EntityID entityId) {
        return entities.get(entityId);
    }

    public Entity getOrCreateEntity(Object domainClassId, Object primaryKey) {
        if (primaryKey == null)
            return null;
        return getOrCreateEntity(getEntityID(domainClassId, primaryKey));
    }

    public Entity getOrCreateEntity(EntityID id) {
        Entity entity = getEntity(id);
        if (entity == null)
            entities.put(id, entity = createEntity(id));
        return entity;
    }

    protected Entity createEntity(EntityID id) {
        return new DynamicEntity(id, this);
    }

    // EntityList management

    public EntityList getEntityList(Object listId) {
        return entityLists.get(listId);
    }

    public EntityList getOrCreateEntityList(Object listId) {
        EntityList entityList = getEntityList(listId);
        if (entityList == null)
            entityLists.put(listId, entityList = new EntityList(listId, this));
        return entityList;
    }
}
