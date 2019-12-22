package webfx.framework.shared.orm.entity.impl;


import webfx.framework.shared.orm.entity.*;
import webfx.framework.shared.orm.entity.lciimpl.EntityDomainWriter;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;

import java.util.HashMap;
import java.util.Map;

/**
 * A store for entities that are transactionally coherent.
 *
 * @author Bruno Salmon
 */
public class EntityStoreImpl implements EntityStore {

    protected final DataSourceModel dataSourceModel;
    private final EntityStore underlyingStore;
    protected final Map<EntityId, Entity> entities = new HashMap<>();
    private final Map<Object, EntityList> entityLists = new HashMap<>();
    private final EntityDomainWriter entityDataWriter = new EntityDomainWriter(this);

    public EntityStoreImpl(DataSourceModel dataSourceModel) {
        this(dataSourceModel, null);
    }

    public EntityStoreImpl(EntityStore underlyingStore) {
        this(underlyingStore.getDataSourceModel(), underlyingStore);
    }

    private EntityStoreImpl(DataSourceModel dataSourceModel, EntityStore underlyingStore) {
        this.dataSourceModel = dataSourceModel;
        this.underlyingStore = underlyingStore;
        // Ensuring the domain model is loaded (and entities registered) otherwise further calls to store.insertEntity(MyEntity.class) will fail
        dataSourceModel.getDomainModel();
    }

    @Override
    public DataSourceModel getDataSourceModel() {
        return dataSourceModel;
    }

    @Override
    public EntityDomainWriter<Entity> getEntityDataWriter() {
        return entityDataWriter;
    }

    @Override
    public void applyEntityIdRefactor(EntityId oldId, EntityId newId) {
        Entity entity = entities.get(oldId); // entities.remove(oldId);
        if (entity != null)
            entities.put(newId, entity);
        for (Entity e : entities.values())
            ((DynamicEntity) e).refactorId(oldId, newId);
    }

    // Entity management

    @Override
    public <E extends Entity> E getEntity(EntityId entityId) {
        E entity = (E) entities.get(entityId);
        if (entity == null && underlyingStore != null)
            entity = underlyingStore.getEntity(entityId);
        return entity;
    }


    @Override
    public <E extends Entity> E createEntity(EntityId id) {
        if (id == null)
            return null;
        EntityFactory<E> entityFactory = EntityFactoryRegistry.getEntityFactory(id.getDomainClass().getModelId());
        E entity = entityFactory != null ? entityFactory.createEntity(id, this) : (E) new DynamicEntity(id, this);
        entities.put(id, entity);
        return entity;
    }

    // EntityList management

    @Override
    public <E extends Entity> EntityList<E> getEntityList(Object listId) {
        return entityLists.get(listId);
    }

    @Override
    public <E extends Entity> EntityList<E> getOrCreateEntityList(Object listId) {
        EntityList<E> entityList = getEntityList(listId);
        if (entityList == null)
            entityLists.put(listId, entityList = EntityList.create(listId, this));
        return entityList;
    }

    @Override
    public void clearEntityList(Object listId) {
        entityLists.remove(listId);
    }

    // Expression

    @Override
    public <E extends Entity> Object evaluateEntityExpression(E entity, Expression<E> expression) {
        return expression.evaluate(entity, entityDataWriter);
    }

    @Override
    public <E extends Entity> void setEntityExpressionValue(E entity, Expression<E> expression, Object value) {
        expression.setValue(entity, value, entityDataWriter);
    }

    private Map<String, Object> parameterValues;

    @Override
    public void setParameterValue(String parameterName, Object parameterValue) {
        if (parameterValues == null)
            parameterValues = new HashMap<>();
        parameterValues.put(parameterName, parameterValue);
    }

    @Override
    public Object getParameterValue(String parameterName) {
        return parameterValues == null ? null : parameterValues.get(parameterName);
    }

    // String report for debugging

    @Override
    public String getEntityClassesCountReport() {
        Map<Object, Integer> classesCount = new HashMap<>();
        for (EntityId id : entities.keySet()) {
            Integer count = classesCount.get(id.getDomainClass());
            classesCount.put(id.getDomainClass(), count == null ? 1 : count + 1);
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
