package webfx.framework.orm.entity.lciimpl;

import webfx.framework.orm.domainmodel.DomainField;
import webfx.framework.orm.entity.Entity;
import webfx.framework.orm.entity.EntityId;
import webfx.framework.orm.entity.EntityStore;
import webfx.framework.expression.lci.DataReader;
import webfx.type.PrimType;

/**
 * @author Bruno Salmon
 */
public class EntityDataReader<E extends Entity> implements DataReader<E> {

    private final EntityStore entityStore;

    public EntityDataReader(EntityStore entityStore) {
        this.entityStore = entityStore;
    }

    @Override
    public E getDomainObjectFromId(Object id, Object src) {
        if (id instanceof Entity)
            return (E) id;
        E entity = entityStore.getEntity((EntityId) id);
        if (entity == null && src instanceof Entity)
            entity = ((Entity) src).getStore().getEntity((EntityId) id);
        return entity;
    }

    @Override
    public Object getDomainObjectId(Entity entity) {
        return entity == null ? null : entity.getId();
    }

    @Override
    public Object getDomainFieldValue(Entity entity, Object fieldId) {
        if (fieldId instanceof DomainField)
            fieldId = ((DomainField) fieldId).getId();
        return entity.getFieldValue(fieldId);
    }

    @Override
    public Object getParameterValue(String name) {
        return entityStore.getParameterValue(name);
    }

    @Override
    public Object prepareValueBeforeTypeConversion(Object value, PrimType type) {
        if (value instanceof Entity)
            value = ((Entity) value).getPrimaryKey();
        else if (value instanceof EntityId)
            value = ((EntityId) value).getPrimaryKey();
        return value;
    }
}
