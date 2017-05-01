package naga.framework.orm.entity.impl;


import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.domainmodel.DomainClass;
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

    protected final DataSourceModel dataSourceModel;
    private final Map<EntityId, Entity> entities = new HashMap<>();
    private final Map<Object, EntityList> entityLists = new HashMap<>();
    private final EntityDataWriter entityDataWriter = new EntityDataWriter(this);

    public EntityStoreImpl(DataSourceModel dataSourceModel) {
        this.dataSourceModel = dataSourceModel;
    }

    @Override
    public DataSourceModel getDataSourceModel() {
        return dataSourceModel;
    }

    // EntityId management

    DomainClass getDomainClass(Object domainClassId) {
        return domainClassId instanceof DomainClass ? (DomainClass) domainClassId : dataSourceModel.getDomainModel().getClass(domainClassId);
    }

    @Override
    public EntityId getEntityId(Object domainClassId, Object primaryKey) {
        return EntityId.create(getDomainClass(domainClassId), primaryKey);
    }

    @Override
    public EntityId getEntityId(DomainClass domainClass, Object primaryKey) {
        return EntityId.create(domainClass, primaryKey);
    }

    // Entity management

    @Override
    public <E extends Entity> E getEntity(EntityId entityId) {
        return (E) entities.get(entityId);
    }

    @Override
    public <E extends Entity> E getOrCreateEntity(EntityId id) {
        if (id == null)
            return null;
        E entity = getEntity(id);
        if (entity == null)
            entities.put(id, entity = createEntity(id));
        return entity;
    }

    protected <E extends Entity> E createEntity(EntityId id) {
        EntityFactory<E> entityFactory = id == null ? null : EntityFactoryRegistry.getEntityFactory(id.getDomainClass().getModelId());
        if (entityFactory != null)
            return entityFactory.createEntity(id, this);
        return (E) new DynamicEntity(id, this);
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
