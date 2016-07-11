package naga.framework.orm.entity.impl;


import naga.framework.orm.entity.*;
import naga.framework.orm.entity.lciimpl.EntityDataWriter;
import naga.framework.expression.Expression;

import java.util.HashMap;
import java.util.Map;

/**
 * A store for entities that are transactionally coherent.
 *
 * @author Bruno Salmon
 */
public class EntityStoreImpl implements EntityStore {

    private final Map<EntityId, Entity> entities = new HashMap<>();
    private final Map<Object, EntityList> entityLists = new HashMap<>();
    private final EntityDataWriter entityDataWriter = new EntityDataWriter(this);

    // Id management

    @Override
    public EntityId getEntityId(Object domainClassId, Object primaryKey) {
        return EntityId.create(domainClassId, primaryKey);
    }

    // Entity management

    @Override
    public Entity getEntity(EntityId entityId) {
        return entities.get(entityId);
    }

    @Override
    public Entity getOrCreateEntity(EntityId id) {
        Entity entity = getEntity(id);
        if (entity == null)
            entities.put(id, entity = createEntity(id));
        return entity;
    }

    @Override
    public Entity getOrCreateEntity(Object domainClassId, Object primaryKey) {
        if (primaryKey == null)
            return null;
        return getOrCreateEntity(getEntityId(domainClassId, primaryKey));
    }

    protected Entity createEntity(EntityId id) {
        return new DynamicEntity(id, this);
    }

    // EntityList management

    @Override
    public EntityList getEntityList(Object listId) {
        return entityLists.get(listId);
    }

    @Override
    public EntityList getOrCreateEntityList(Object listId) {
        EntityList entityList = getEntityList(listId);
        if (entityList == null)
            entityLists.put(listId, entityList = EntityList.create(listId, this));
        return entityList;
    }

    // Expression

    @Override
    public Object evaluateEntityExpression(Entity entity, Expression expression) {
        return expression.evaluate(entity, entityDataWriter);
    }

    // String report for debugging

    @Override
    public String getEntityClassesCountReport() {
        Map<Object, Integer> classesCount = new HashMap<>();
        for (EntityId id : entities.keySet()) {
            Integer count = classesCount.get(id.getDomainClassId());
            classesCount.put(id.getDomainClassId(), count == null ? 1 : count + 1);
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Object, Integer> entry : classesCount.entrySet()) {
            if (sb.length() > 0)
                sb.append(", ");
            sb.append(entry.getValue()).append(' ').append(entry.getKey());
        }
        return sb.toString();
    }
}
