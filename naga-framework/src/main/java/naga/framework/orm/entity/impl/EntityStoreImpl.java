package naga.framework.orm.entity.impl;


import naga.framework.expression.Expression;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.entity.*;
import naga.framework.orm.entity.lciimpl.EntityDataWriter;

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
    private final EntityDataWriter entityDataWriter = new EntityDataWriter(this);

    public EntityStoreImpl(DataSourceModel dataSourceModel) {
        this(dataSourceModel, null);
    }

    public EntityStoreImpl(EntityStore underlyingStore) {
        this(underlyingStore.getDataSourceModel(), underlyingStore);
    }

    private EntityStoreImpl(DataSourceModel dataSourceModel, EntityStore underlyingStore) {
        this.dataSourceModel = dataSourceModel;
        this.underlyingStore = underlyingStore;
    }

    @Override
    public DataSourceModel getDataSourceModel() {
        return dataSourceModel;
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

    @Override
    public void clearEntityList(Object listId) {
        entityLists.remove(listId);
    }

    // Expression

    @Override
    public Object evaluateEntityExpression(Entity entity, Expression expression) {
        return expression.evaluate(entity, entityDataWriter);
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
