package naga.framework.orm.entity;

import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.domainmodel.DomainClass;
import naga.framework.orm.entity.impl.DynamicEntity;
import naga.framework.orm.entity.impl.EntityStoreImpl;
import naga.framework.expression.Expression;

/**
 * A store for entities that are transactionally coherent.
 *
 * @author Bruno Salmon
 */
public interface EntityStore {

    DataSourceModel getDataSourceModel();

    default DomainClass getDomainClass(Object domainClassId) {
        return domainClassId instanceof DomainClass ? (DomainClass) domainClassId : getDataSourceModel().getDomainModel().getClass(domainClassId);
    }

    default DomainClass getDomainClass(Class<? extends Entity> entityClass) {
        return getDomainClass(getDomainClassId(entityClass));
    }

    default Object getDomainClassId(Class<? extends Entity> entityClass) {
        return EntityFactoryRegistry.getEntityDomainClassId(entityClass);
    }


    // EntityId management

    default EntityId getEntityId(Object domainClassId, Object primaryKey) {
        return EntityId.create(getDomainClass(domainClassId), primaryKey);
    }

    default EntityId getEntityId(DomainClass domainClass, Object primaryKey) {
        return EntityId.create(domainClass, primaryKey);
    }

    void applyEntityIdRefactor(EntityId oldId, EntityId newId);


    // Entity management

    default <E extends Entity> E createEntity(Class<E> entityClass) {
        return createEntity(getDomainClass(entityClass));
    }

    default <E extends Entity> E createEntity(Object domainClassId) {
        return createEntity(getDomainClass(domainClassId));
    }

    default <E extends Entity> E createEntity(DomainClass domainClass) {
        return createEntity(EntityId.create(domainClass));
    }

    default <E extends Entity> E createEntity(Class<E> entityClass, Object primaryKey) {
        return createEntity(getDomainClass(entityClass), primaryKey);
    }

    default <E extends Entity> E createEntity(Object domainClassId, Object primaryKey) {
        return primaryKey == null ? null : createEntity(getEntityId(domainClassId, primaryKey));
    }

    default <E extends Entity> E createEntity(DomainClass domainClass, Object primaryKey) {
        return primaryKey == null ? null : createEntity(getEntityId(domainClass, primaryKey));
    }

    <E extends Entity> E createEntity(EntityId id);

    default <E extends Entity> E getEntity(Class<E> entityClass, Object primaryKey) {
        return getEntity(getDomainClass(entityClass), primaryKey);
    }

    default <E extends Entity> E getEntity(Object domainClassId, Object primaryKey) {
        return primaryKey == null ? null : getEntity(getEntityId(domainClassId, primaryKey));
    }

    default <E extends Entity> E getEntity(DomainClass domainClass, Object primaryKey) {
        return primaryKey == null ? null : getEntity(getEntityId(domainClass, primaryKey));
    }

    <E extends Entity> E getEntity(EntityId entityId);

    default <E extends Entity> E getOrCreateEntity(Class<E> entityClass, Object primaryKey) {
        return getOrCreateEntity(getDomainClass(entityClass), primaryKey);
    }

    default <E extends Entity> E getOrCreateEntity(Object domainClassId, Object primaryKey) {
        return primaryKey == null ? null : getOrCreateEntity(getEntityId(domainClassId, primaryKey));
    }

    default <E extends Entity> E getOrCreateEntity(DomainClass domainClass, Object primaryKey) {
        return primaryKey == null ? null : getOrCreateEntity(getEntityId(domainClass, primaryKey));
    }

    default <E extends Entity> E getOrCreateEntity(EntityId id) {
        if (id == null)
            return null;
        E entity = getEntity(id);
        if (entity == null)
            entity = createEntity(id);
        return entity;
    }

    default <E extends Entity> E copyEntity(E entity) {
        if (entity == null)
            return null;
        E copy = getOrCreateEntity(entity.getId());
        if (copy != entity)
            ((DynamicEntity) copy).copyAllFieldsFrom(entity);
        return copy;
    }


    // EntityList management

    <E extends Entity> EntityList<E> getEntityList(Object listId);

    <E extends Entity> EntityList<E> getOrCreateEntityList(Object listId);

    void clearEntityList(Object listId);


    // Expression evaluation

    default Object evaluateEntityExpression(Entity entity, String expression) {
        return evaluateEntityExpression(entity, getDataSourceModel().getDomainModel().parseExpression(expression, entity.getDomainClass().getId()));
    }

    Object evaluateEntityExpression(Entity entity, Expression expression);

    void setParameterValue(String parameterName, Object parameterValue);

    Object getParameterValue(String parameterName);


    // String report for debugging

    String getEntityClassesCountReport();


    // Factory

    static EntityStore create(DataSourceModel dataSourceModel) {
        return new EntityStoreImpl(dataSourceModel);
    }

    static EntityStore createAbove(EntityStore underlyingStore) {
        return new EntityStoreImpl(underlyingStore);
    }
}
