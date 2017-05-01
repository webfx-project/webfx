package naga.framework.orm.entity.lciimpl;

import naga.framework.orm.domainmodel.DomainField;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.EntityStore;
import naga.framework.expression.lci.DataReader;
import naga.commons.type.PrimType;

/**
 * @author Bruno Salmon
 */
public class EntityDataReader implements DataReader<Entity> {

    private final EntityStore entityStore;

    public EntityDataReader(EntityStore entityStore) {
        this.entityStore = entityStore;
    }

    @Override
    public Entity getDomainObjectFromId(Object id) {
        if (id instanceof Entity)
            return (Entity) id;
        return entityStore.getEntity((EntityId) id);
    }

    @Override
    public Object getDomainObjectId(Entity entity) {
        return entity.getId();
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
