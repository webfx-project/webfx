package naga.core.orm.entity.lciimpl;

import naga.core.orm.domainmodel.DomainField;
import naga.core.orm.entity.Entity;
import naga.core.orm.entity.EntityId;
import naga.core.orm.entity.EntityStore;
import naga.core.orm.expression.lci.DataReader;
import naga.core.type.PrimType;

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
        return null;
    }

    @Override
    public Object prepareValueBeforeTypeConversion(Object value, PrimType type) {
        if (value instanceof Entity)
            value = ((Entity) value).getId().getPrimaryKey();
        else if (value instanceof EntityId)
            value = ((EntityId) value).getPrimaryKey();
        return value;
    }
}
