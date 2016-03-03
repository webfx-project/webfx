package naga.core.orm.entity.lciimpl;

import naga.core.orm.domainmodel.DomainField;
import naga.core.orm.entity.Entity;
import naga.core.orm.entity.EntityID;
import naga.core.orm.entity.EntityStore;
import naga.core.orm.expression.lci.DataReader;

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
        return entityStore.getEntity((EntityID) id);
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
}
