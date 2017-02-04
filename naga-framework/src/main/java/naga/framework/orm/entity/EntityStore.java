package naga.framework.orm.entity;

import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.domainmodel.DomainClass;
import naga.framework.orm.entity.impl.EntityStoreImpl;
import naga.framework.expression.Expression;

/**
 * A store for entities that are transactionally coherent.
 *
 * @author Bruno Salmon
 */
public interface EntityStore {

    DataSourceModel getDataSourceModel();

    // EntityId management

    EntityId getEntityId(DomainClass domainClass, Object primaryKey);

    EntityId getEntityId(Object domainClassId, Object primaryKey);


    // Entity management

    <E extends Entity> E getEntity(EntityId entityId);

    <E extends Entity> E getOrCreateEntity(EntityId id);

    default <E extends Entity> E getEntity(Class<E> entityClass, Object primaryKey) {
        return getEntity(EntityFactoryRegistry.getEntityDomainClassId(entityClass), primaryKey);
    }

    default <E extends Entity> E getEntity(DomainClass domainClass, Object primaryKey) {
        return primaryKey == null ? null : getEntity(getEntityId(domainClass, primaryKey));
    }

    default <E extends Entity> E getEntity(Object domainClassId, Object primaryKey) {
        return primaryKey == null ? null : getEntity(getEntityId(domainClassId, primaryKey));
    }

    default <E extends Entity> E getOrCreateEntity(Class<E> entityClass, Object primaryKey) {
        return getOrCreateEntity(EntityFactoryRegistry.getEntityDomainClassId(entityClass), primaryKey);
    }

    default <E extends Entity> E getOrCreateEntity(DomainClass domainClass, Object primaryKey) {
        return primaryKey == null ? null : getOrCreateEntity(getEntityId(domainClass, primaryKey));
    }

    default <E extends Entity> E getOrCreateEntity(Object domainClassId, Object primaryKey) {
        return primaryKey == null ? null : getOrCreateEntity(getEntityId(domainClassId, primaryKey));
    }



    // EntityList management

    EntityList getEntityList(Object listId);

    EntityList getOrCreateEntityList(Object listId);

    void clearEntityList(Object listId);


    // Expression evaluation

    Object evaluateEntityExpression(Entity entity, Expression expression);


    // String report for debugging

    String getEntityClassesCountReport();

    // Factory

    static EntityStore create(DataSourceModel dataSourceModel) {
        return new EntityStoreImpl(dataSourceModel);
    }
}
