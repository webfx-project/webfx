package naga.framework.orm.entity;

import naga.framework.expression.Expression;
import naga.framework.expression.sqlcompiler.sql.SqlCompiled;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.domainmodel.DomainClass;
import naga.framework.orm.domainmodel.HasDataSourceModel;
import naga.framework.orm.entity.impl.DynamicEntity;
import naga.framework.orm.entity.impl.EntityStoreImpl;
import naga.framework.orm.mapping.QueryResultSetToEntityListGenerator;
import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.QueryService;
import naga.util.Arrays;
import naga.util.async.Batch;
import naga.util.async.Future;

/**
 * A store for entities that are transactionally coherent.
 *
 * @author Bruno Salmon
 */
public interface EntityStore extends HasDataSourceModel {

    default DomainClass getDomainClass(Object domainClassId) {
        return domainClassId instanceof DomainClass ? (DomainClass) domainClassId : getDomainModel().getClass(domainClassId);
    }

    default DomainClass getDomainClass(Class<? extends Entity> entityClass) {
        return getDomainClass(getDomainClassId(entityClass));
    }

    default Object getDomainClassId(Class<? extends Entity> entityClass) {
        return EntityDomainClassIdRegistry.getEntityDomainClassId(entityClass);
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
        if (copy.getStore() != this) // Ensuring the copy is in this store
            copy = createEntity(entity.getId());
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
        return evaluateEntityExpression(entity, getDomainModel().parseExpression(expression, entity.getDomainClass().getId()));
    }

    <E extends Entity> Object evaluateEntityExpression(E entity, Expression<E> expression);

    void setParameterValue(String parameterName, Object parameterValue);

    Object getParameterValue(String parameterName);

    // Query methods

    default <E extends Entity> Future<EntityList<E>> executeQuery(String select) {
        return executeQuery(select, select);
    }

    default <E extends Entity> Future<EntityList<E>> executeQuery(String select, Object listId) {
        return executeQuery(select, null, listId);
    }

    default <E extends Entity> Future<EntityList<E>> executeQuery(String select, Object[] parameters) {
        return executeQuery(select, parameters, select);
    }

    default <E extends Entity> Future<EntityList<E>> executeQuery(String select, Object[] parameters, Object listId) {
        SqlCompiled sqlCompiled = getDomainModel().compileSelect(select);
        return QueryService.executeQuery(new QueryArgument(sqlCompiled.getSql(), parameters, getDataSourceId()))
                .map(rs -> QueryResultSetToEntityListGenerator.createEntityList(rs, sqlCompiled.getQueryMapping(), this, listId)
        );
    }

    default <E extends Entity> Future<EntityList<E>> executeQuery(EntityStoreQuery query) {
        return executeQuery(query.select, query.parameters, query.listId);
    }

    default Future<EntityList[]> executeQueryBatch(EntityStoreQuery... queries) {
        SqlCompiled[] sqlCompileds = Arrays.map(queries, query -> getDomainModel().compileSelect(query.select), SqlCompiled[]::new);
        return QueryService.executeQueryBatch(new Batch<>(Arrays.map(queries, (i, query) -> new QueryArgument(sqlCompileds[i].getSql(), query.parameters, getDataSourceId()), QueryArgument[]::new)))
                .map(batchResult -> Arrays.map(batchResult.getArray(), (i, rs) -> QueryResultSetToEntityListGenerator.createEntityList(rs, sqlCompileds[i].getQueryMapping(), this, queries[i].listId), EntityList[]::new));
    }

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
