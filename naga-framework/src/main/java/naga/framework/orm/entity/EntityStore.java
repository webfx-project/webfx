package naga.framework.orm.entity;

import naga.framework.orm.entity.impl.EntityStoreImpl;
import naga.framework.expression.Expression;

/**
 * A store for entities that are transactionally coherent.
 *
 * @author Bruno Salmon
 */
public interface EntityStore {

    // Id management

    EntityId getEntityId(Object domainClassId, Object primaryKey);


    // Entity management

    Entity getEntity(EntityId entityId);

    Entity getOrCreateEntity(EntityId id);

    Entity getOrCreateEntity(Object domainClassId, Object primaryKey);


    // EntityList management

    EntityList getEntityList(Object listId);

    EntityList getOrCreateEntityList(Object listId);


    // Expression evaluation

    Object evaluateEntityExpression(Entity entity, Expression expression);


    // String report for debugging

    String getEntityClassesCountReport();

    // Factory

    static EntityStore create() {
        return new EntityStoreImpl();
    }
}
