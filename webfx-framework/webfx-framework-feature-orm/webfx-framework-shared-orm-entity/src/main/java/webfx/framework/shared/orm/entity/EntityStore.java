package webfx.framework.shared.orm.entity;

import webfx.framework.shared.orm.dql.sqlcompiler.mapping.QueryRowToEntityMapping;
import webfx.framework.shared.orm.entity.query_result_to_entities.QueryResultToEntitiesMapper;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.dql.sqlcompiler.sql.SqlCompiled;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.domainmodel.DomainClass;
import webfx.framework.shared.orm.domainmodel.HasDataSourceModel;
import webfx.framework.shared.orm.entity.impl.DynamicEntity;
import webfx.framework.shared.orm.entity.impl.EntityStoreImpl;
import webfx.framework.shared.orm.entity.lciimpl.EntityDomainWriter;
import webfx.platform.shared.services.query.QueryArgument;
import webfx.platform.shared.services.query.QueryResult;
import webfx.platform.shared.services.query.QueryService;
import webfx.platform.shared.util.Arrays;
import webfx.platform.shared.util.async.Batch;
import webfx.platform.shared.util.async.Future;

/**
 * A store for entities that are transactionally coherent.
 *
 * @author Bruno Salmon
 */
public interface EntityStore extends HasDataSourceModel {

    EntityDomainWriter<Entity> getEntityDataWriter();

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
        return evaluateEntityExpression(entity, entity.parseExpression(expression));
    }

    <E extends Entity> Object evaluateEntityExpression(E entity, Expression<E> expression);

    <E extends Entity> void setEntityExpressionValue(E entity, Expression<E> expression, Object value);

    void setParameterValue(String parameterName, Object parameterValue);

    Object getParameterValue(String parameterName);

    // Query methods

    default <E extends Entity> Future<EntityList<E>> executeQuery(String dqlQuery, Object... parameters) {
        return executeListQuery(dqlQuery, dqlQuery, parameters);
    }

    default <E extends Entity> Future<EntityList<E>> executeListQuery(Object listId, String dqlQuery, Object... parameters) {
        Future<QueryResult> future = QueryService.executeQuery(createQueryArgument(dqlQuery, parameters));
        QueryRowToEntityMapping queryMapping = getDataSourceModel().parseAndCompileSelect(dqlQuery).getQueryMapping();
        return future.map(rs -> QueryResultToEntitiesMapper.mapQueryResultToEntities(rs, queryMapping, this, listId));
    }

    default <E extends Entity> Future<EntityList<E>> executeQuery(EntityStoreQuery query) {
        return executeListQuery(query.listId, query.select, query.parameters);
    }

    default QueryArgument createQueryArgument(String dqlQuery, Object[] parameters) {
        return DqlQueryArgumentHelper.createQueryArgument(dqlQuery, parameters, getDataSourceModel(), null);
    }

    default Future<EntityList[]> executeQueryBatch(EntityStoreQuery... queries) {
        Future<Batch<QueryResult>> future = QueryService.executeQueryBatch(new Batch<>(Arrays.map(queries, (i, query) -> createQueryArgument(query.select, query.parameters), QueryArgument[]::new)));
        SqlCompiled[] sqlCompileds = Arrays.map(queries, query -> getDataSourceModel().parseAndCompileSelect(query.select), SqlCompiled[]::new);
        return future.map(batchResult -> Arrays.map(batchResult.getArray(), (i, rs) -> QueryResultToEntitiesMapper.mapQueryResultToEntities(rs, sqlCompileds[i].getQueryMapping(), this, queries[i].listId), EntityList[]::new));
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
