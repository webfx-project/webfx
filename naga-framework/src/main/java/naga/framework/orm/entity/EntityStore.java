package naga.framework.orm.entity;

import naga.framework.orm.entity.impl.EntityStoreImpl;
import naga.framework.expression.Expression;

/**
 * A store for entities that are transactionally coherent.
 *
 * @author Bruno Salmon
 */
public interface EntityStore {

    // EntityId management

    EntityId getEntityId(Object domainClassId, Object primaryKey);


    // Entity management

    <E extends Entity> E getEntity(EntityId entityId);

    <E extends Entity> E getOrCreateEntity(Class<E> entityClass, Object primaryKey);

    <E extends Entity> E getOrCreateEntity(Object domainClassId, Object primaryKey);

    <E extends Entity> E getOrCreateEntity(EntityId id);



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
